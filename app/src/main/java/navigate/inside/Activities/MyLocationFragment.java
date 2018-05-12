package navigate.inside.Activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.recognition.packets.Beacon;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import org.json.JSONObject;

import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationFragment extends Fragment implements BeaconListener, NetworkResListener{
    private TextView name, direction;
    private VrPanoramaView panoWidgetView;

    public MyLocationFragment() {
        // Required empty public constructor
    }

    public static MyLocationFragment newInstance() {
        return new MyLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_location, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        name = (TextView) view.findViewById(R.id.node_name);
        direction = (TextView) view.findViewById(R.id.node_direct);
        panoWidgetView = (VrPanoramaView) view.findViewById(R.id.frag_vr_view);
    }

    private void bindPage(Node node){

        name.setText(node.get_id().getMajor());
        /*Bitmap image = PathFinder.getInstance().getImage(position);
        if (image == null){
            NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_ALL_NODES_JSON_REQ, node, this);
        }else{
            loadImageto3D(image);
        }*/
        if(node.getImage() != null) {
            VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
            viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            panoWidgetView.loadImageFromBitmap(node.getImage(), viewOptions);
        }
    }

    @Override
    public void onBeaconEvent(Beacon beacon) {
        BeaconID bid = new BeaconID(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
        Node node = SysData.getInstance().getNodeByBeaconID(bid);
        if (node != null)
            bindPage(node);
    }

    private void loadImageto3D(Bitmap res) {
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
        panoWidgetView.loadImageFromBitmap(res, viewOptions);
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
            Toast.makeText(getContext(), R.string.loadfailed, Toast.LENGTH_SHORT).show();
    }
}
