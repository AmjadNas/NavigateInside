package navigate.inside.Activities.Navigation.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import org.json.JSONObject;

import navigate.inside.Activities.PanoramicImageActivity;
import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;
import navigate.inside.Utills.Converter;


public class MyLocationActivity extends AppCompatActivity implements NetworkResListener, BeaconListener{
    private TextView name, direction;
    private ImageView panoWidgetView;
    private BeaconID CurrentBeacon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_location);
        initView();
    }

    private void initView(){
        name = (TextView) findViewById(R.id.node_name);
        direction = (TextView) findViewById(R.id.node_direct);
        panoWidgetView = (ImageView) findViewById(R.id.thumb_find_location);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        ((MyApplication)getApplication()).registerListener(this);
        ((MyApplication)getApplication()).startRanging();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister beacon listeners
        ((MyApplication)getApplication()).stopRanging();
        ((MyApplication)getApplication()).unRegisterListener(this);
    }

    public void bindPage(Node node){
        int m = node.get_id().getMajor();
        name.setText(String.valueOf(m));
        Bitmap image = SysData.getInstance().getImageForNode(node.get_id());
        if (image != null)
            loadImageto3D(image, false);


    }

    private void loadImageto3D(final Bitmap res, final boolean downloaded) {
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected void onPostExecute(Bitmap aVoid) {
                if (aVoid != null)
                    panoWidgetView.setImageBitmap(aVoid);
                /* else
                    NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET, itemList.get(position).first, this);
                    */
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                if (downloaded)
                    SysData.getInstance().insertImageToDB(CurrentBeacon, res);

                return Converter.getImageTHumbnail(res);
            }
        }.execute();

    }

    @Override
    public void onPreUpdate(String str) {

    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {

    }

    @Override
    public void onPostUpdate(Bitmap res, ResStatus status) {
        if (status == ResStatus.SUCCESS){
            if (res != null){
                loadImageto3D(res, true);
            }

        }else
            Toast.makeText(this, R.string.loadfailed, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBeaconEvent(Beacon beacon) {
        BeaconID temp = new BeaconID(beacon.getProximityUUID(),beacon.getMajor(),beacon.getMinor());
        if(CurrentBeacon == null){
            CurrentBeacon = temp;
        }else{
            if(!CurrentBeacon.equals(temp)){
                CurrentBeacon = temp;
                if(SysData.getInstance().getNodeByBeaconID(CurrentBeacon) !=null){

                    bindPage(SysData.getInstance().getNodeByBeaconID(CurrentBeacon));
                }else{
                    Toast.makeText(this, R.string.cant_find_location,Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void viewPanoramic(View view) {
        Intent intent = new Intent(this,PanoramicImageActivity.class);
        intent.putExtra(Constants.ID, CurrentBeacon);
        startActivity(intent);
    }
}