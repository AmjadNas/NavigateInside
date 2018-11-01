package navigate.inside.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkConnector;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class SplashActivity extends AppCompatActivity implements NetworkResListener {

    private String id;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // initialize system class and networking class
        SysData.getInstance().initDatBase(getApplicationContext());
        NetworkConnector.getInstance().initialize(getApplicationContext());

        // check for app id if it doesn't exist generate one and download data
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        id = sharedPref.getString(getResources().getString(R.string.firstLaunch),"-1");
       if (id.equals("-1")) {
           id = System.currentTimeMillis() + "0";
           NetworkConnector.getInstance().update(id,this);

        }else{

       // SysData.getInstance().initDatBase(getApplicationContext());
            SysData.getInstance().InitializeData();
            launchActivity();
            finish();
        }
    }

    @Override
    public void onPreUpdate() {

    }

    @Override
    public void onPostUpdate(JSONObject res, String req, ResStatus status) {
        if(status == ResStatus.SUCCESS){
            try {
                //handle requests on success
                switch (req){
                    case NetworkConnector.GET_ALL_NODES_JSON_REQ:
                        parseJson(res);
                        // save generated app id for further use
                        sharedPref.edit()
                                .putString(getResources().getString(R.string.firstLaunch), id)
                                .apply();
                        NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.UPDATE_REQ, id, this);
                        launchActivity();
                        break;
                        default:
                            break;
                }

            } catch (JSONException e) {
                SysData.getInstance().closeDatabase();
                Toast.makeText(getApplicationContext(), "Incorrect data form", Toast.LENGTH_SHORT).show();
            }

        }else if (status == ResStatus.FAIL && req.equals(NetworkConnector.GET_ALL_NODES_JSON_REQ)){
            //temporary
            SysData.getInstance().closeDatabase();
            //launchActivity();
            Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

        }
      /*  if (firstInit && status == ResStatus.FAIL)
            Toast.makeText(this, "couldn't initialize app.", Toast.LENGTH_SHORT).show();*/
       // else if (!firstInit && status == ResStatus.FAIL){}
         //


        finish();
    }

    /**
     * handle parsing the json
     * @param res
     * @throws JSONException
     */
    private void parseJson(JSONObject res) throws JSONException{
        JSONArray arr = res.getJSONArray(Constants.Node), nbers, rooms, imgs;
        JSONObject o, nbr, img;
        Node n;
        // fill in nodes and rooms
        for(int i = 0; i < arr.length(); i++) {
            o = arr.getJSONObject(i);
            n = Node.parseJson(o);
            if (n != null) {
                if (SysData.getInstance().insertNode(n)) {
                    ///  NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_NODE_IMAGE, n, this);
                    rooms = o.getJSONArray(Constants.ROOMS);
                    Room r;
                    for (int j = 0; j < rooms.length(); j++) {
                        r = Room.parseJson(rooms.getJSONObject(j));
                        SysData.getInstance().insertRoomToNode(r, n);
                    }
                }
            }

        }
        //fill in neighbours 
        for(int i = 0; i < arr.length(); i++) {
            o = arr.getJSONObject(i);
            nbers = o.getJSONArray(Constants.Node);
            for (int j = 0; j < nbers.length(); j++) {
                nbr = nbers.getJSONObject(j);
                SysData.getInstance().insertNeighbourToNode(o.getString(Constants.BEACONID), nbr.getString(Constants.BEACONID), nbr.getInt(Constants.Direction));

            }
        }

    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.ID, id);
        startActivity(intent);
    }

    @Override
    public void onPostUpdate(Bitmap res, String id, String id2, ResStatus status) {
    }
}
