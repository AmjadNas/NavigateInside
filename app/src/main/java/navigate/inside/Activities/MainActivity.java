package navigate.inside.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import navigate.inside.Activities.Navigation.Activities.GetDirectionsActivity;
import navigate.inside.Activities.Navigation.Activities.MyLocationActivity;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkConnector;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class MainActivity extends AppCompatActivity implements NetworkResListener {

    private ProgressDialog progressDialog;
    private SysData data;
    private String appID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appID = getIntent().getStringExtra(Constants.ID);
        data = SysData.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acctivity_main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionRefresh) {
            // check for new changes in data
            NetworkConnector.getInstance().update(appID,this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        data.closeDatabase();
    }

    private void launchActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void get_directions(View view) {
        launchActivity(GetDirectionsActivity.class);
    }


    @Override
    public void onPreUpdate(String str) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Updating App..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {
        if (status == ResStatus.SUCCESS){
            try {
                data.clearData();
                parseJson(res);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.UPDATE_REQ, appID, this);
            } catch (JSONException e) {
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();

            }

        }else {
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onPostUpdate(Bitmap res, String id, String id2, ResStatus status) {
        if (status == ResStatus.SUCCESS){
            if (res != null){
                data.updateImage(BeaconID.from(id), BeaconID.from(id2), res);
            }
        }
    }

    private void parseJson(JSONObject res) throws JSONException {
        JSONArray arr = res.getJSONArray(Constants.Node), nbers, rooms, imgs;
        JSONObject o, nbr, img;
        Node n;

        for(int i = 0; i < arr.length(); i++) {
            o = arr.getJSONObject(i);
            n = Node.parseJson(o);
            if (n != null) {
                if (data.insertNode(n)) {
                    ///  NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_NODE_IMAGE, n, this);
                    rooms = o.getJSONArray(Constants.ROOMS);
                    Room r;
                    for (int j = 0; j < rooms.length(); j++) {
                        r = Room.parseJson(rooms.getJSONObject(j));
                        data.insertRoomToNode(r, n);
                    }
                }
            }

        }
        for(int i = 0; i < arr.length(); i++) {
            o = arr.getJSONObject(i);
            nbers = o.getJSONArray(Constants.Node);
            for (int j = 0; j < nbers.length(); j++) {
                nbr = nbers.getJSONObject(j);
                if(data.insertNeighbourToNode(o.getString(Constants.BEACONID), nbr.getString(Constants.BEACONID), nbr.getInt(Constants.Direction))){
                    NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_NODE_IMAGE,
                            o.getString(Constants.BEACONID), nbr.getString(Constants.BEACONID), this);
                }
            }
        }

    }
}
