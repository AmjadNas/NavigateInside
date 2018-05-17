package navigate.inside.Activities.Navigation.Activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import org.json.JSONObject;

import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;


public class MyLocationActivity extends AppCompatActivity implements NetworkResListener, BeaconListener{
    private TextView name, direction;
    private ImageView panoWidgetView;
    private BeaconID CurrentBeacon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_location);
    }

    private void initView(View view){
        name = (TextView) view.findViewById(R.id.node_name);
        direction = (TextView) view.findViewById(R.id.node_direct);
        panoWidgetView = (ImageView) view.findViewById(R.id.sell_img);
       // panoWidgetView.setOnClickListener(this);
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
        // todo needs improving, cuz keeps crashing
        int m =node.get_id().getMajor();
      //  name.setText(String.valueOf(m));
      /*  Bitmap image = SysData.getInstance().getImageForNode(node.get_id());
        if (image != null){
        //    NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_ALL_NODES_JSON_REQ, node, this);
       // }else{
            loadImageto3D(image);
        }*/
       /* if(node.getImage() != null) {
            VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
            viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            panoWidgetView.loadImageFromBitmap(node.getImage(), viewOptions);
        }*/
    }

    private void loadImageto3D(Bitmap res) {
        panoWidgetView.setImageBitmap(res);
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
                loadImageto3D(res);
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

                    //   bindPage(SysData.getInstance().getNodeByBeaconID(CurrentBeacon));
                }else{
                    Toast.makeText(this,"Failed to fetch location",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
