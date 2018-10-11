package navigate.inside.Activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity implements NetworkResListener,ActivityCompat.OnRequestPermissionsResultCallback {
    // notification ID
    private static final int MY_NOTIFICATION_ID = 22;
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
        // check if the data has changed in the online database
        NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.CHECK_FOR_UPDATE, appID, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // render the custom menu icons
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
    protected void onStop() {
        super.onStop();
        // make sure database is saved and closed when the app stops
        data.closeDatabase();
    }

    /**
     * helper method to launch other activities
     * @param cls the destination activity class
     */
    private void launchActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * handle button click
     * @param view
     */
    public void get_directions(View view) {
        launchActivity(GetDirectionsActivity.class);
    }


    @Override
    public void onPreUpdate() {
        // show dialog and prevent user from caneling the update
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Updating App..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onPostUpdate(JSONObject res,String req, ResStatus status) {

        if (status == ResStatus.SUCCESS){
            try {
                // handle request statuses when the operation is a success
                switch (req){
                    case NetworkConnector.GET_ALL_NODES_JSON_REQ:
                        data.clearData();
                        parseJson(res);
                        NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.UPDATE_REQ, appID, this);
                        break;
                    case NetworkConnector.CHECK_FOR_UPDATE:
                        // notify user by showing a notification
                        notifyUser();
                        break;

                        default:
                            break;
                }

           } catch (JSONException e) {
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();

            }
        }else if (status == ResStatus.FAIL){
            if (req.equals(NetworkConnector.GET_ALL_NODES_JSON_REQ))
                Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void notifyUser() {
        // Notification Action Elements
        Intent mNotificationIntent;
        PendingIntent mContentIntent;

        mNotificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mNotificationIntent,
                Intent.FILL_IN_ACTION);

        // Define the Notification's expanded message and Intent:
        // Notification Sound and Vibration on Arrival
        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] mVibratePattern = { 0, 200, 200, 300 };
        // Notification Text Elements
        String contentTitle = getResources().getString(R.string.app_name);

        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext());

        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle(contentTitle);
        notificationBuilder.setContentText(getResources().getString(R.string.clickToUpdate));
        notificationBuilder.setContentIntent(mContentIntent).setSound(soundURI);
        notificationBuilder.setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());


    }

    @Override
    public void onPostUpdate(Bitmap res, String id, String id2, ResStatus status) {
        if (status == ResStatus.SUCCESS){
            if (res != null){
                data.updateImage(BeaconID.from(id), BeaconID.from(id2), res);
            }
        }
    }

    /**
     * handle json parsing and insert data into the local database
     * @param res
     * @throws JSONException
     */
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
