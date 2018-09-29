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

    private boolean firstInit;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SysData.getInstance();
        SysData.getInstance().initDatBase(getApplicationContext());
        NetworkConnector.getInstance().initialize(getApplicationContext());

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        firstInit = sharedPref.getBoolean(getResources().getString(R.string.firstLaunch), true);

       if (firstInit) {
            NetworkConnector.getInstance().update(this);

        }else{

       // SysData.getInstance().initDatBase(getApplicationContext());
            SysData.getInstance().InitializeData();
            launchActivity();
            finish();
        }
    }

    @Override
    public void onPreUpdate(String str) {

    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {
        if(status == ResStatus.SUCCESS){
            try {
                JSONArray arr = res.getJSONArray(Constants.Node), nbers, rooms;
                JSONObject o, nbr;
                Node n;
//todo: needs rework
                for(int i = 0; i < arr.length(); i++){
                    o = arr.getJSONObject(i);
                    n = Node.parseJson(o);
                    if(SysData.getInstance().insertNode(n)) {
                        NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_NODE_IMAGE, n, this);
                        rooms = o.getJSONArray(Constants.ROOMS);
                        Room r;

                        for (int j = 0; j < rooms.length(); j++) {
                            r = Room.parseJson(rooms.getJSONObject(j));
                            SysData.getInstance().insertRoomToNode(r, n);
                        }
                    }
                }
                for(int i = 0; i < arr.length(); i++){
                    o = arr.getJSONObject(i);
                    nbers = o.getJSONArray(Constants.Node);

                    for(int j = 0; j < nbers.length(); j++){
                        nbr = nbers.getJSONObject(j);
                        SysData.getInstance().insertNeighbourToNode(o.getString(Constants.BEACONID), nbr.getString(Constants.BEACONID), nbr.getInt(Constants.Direction));
                    }
                }
                sharedPref.edit()
                        .putBoolean(getResources().getString(R.string.firstLaunch), false)
                        .apply();
                launchActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            //temporary
            SysData.getInstance().InitializeData();
            Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

        }
      /*  if (firstInit && status == ResStatus.FAIL)
            Toast.makeText(this, "couldn't initialize app.", Toast.LENGTH_SHORT).show();*/
       // else if (!firstInit && status == ResStatus.FAIL){}
         //


        finish();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPostUpdate(Bitmap res, String id, ResStatus status) {
        if (status == ResStatus.SUCCESS){
            if (res != null){
                SysData.getInstance().insertImageToDB(BeaconID.from(id), res);
            }
        }

    }
}
