package navigate.inside.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.recognition.packets.Beacon;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import org.json.JSONObject;

import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationFragment extends Fragment implements NetworkResListener, View.OnClickListener{
    private TextView name, direction;
    private ImageView panoWidgetView;

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
        panoWidgetView = (ImageView) view.findViewById(R.id.sell_img);
        panoWidgetView.setOnClickListener(this);
    }

    public void bindPage(Node node){
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
            Toast.makeText(getContext(), R.string.loadfailed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        /*Intent intent = new Intent(this, PanoramicImageActivity.class);
        intent.putExtra(Constants.ID, currentID);
        startActivity(intent);*/
    }
}
