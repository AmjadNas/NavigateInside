package navigate.inside.Activities.Navigation.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import java.util.UUID;

import navigate.inside.Logic.BeaconListener;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Logic.SysData;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class GetDirectionsActivity extends AppCompatActivity implements BeaconListener {

    private TextView sNode,gNode;
    private CheckBox chElevator;
    private BeaconID CurrentBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);
        initView();
    }

    private void initView() {

        sNode = (TextView)findViewById(R.id.start);
        gNode = (TextView)findViewById(R.id.goal);
        chElevator = (CheckBox)findViewById(R.id.elevator);
    }

    public void BindText(Node n){
        if(!n.getRooms().isEmpty()) {
            Room r = n.getRooms().get(0);
            sNode.setText(r.GetRoomName());
        }
    }


    public void Search(View view) {
        // todo SEARCH DISABLED TEMPORARILY


        String txtSNode = sNode.getText().toString();
        String txtGNode = gNode.getText().toString();

        //temporary
        BeaconID FinishNode;
        BeaconID StartNode;
        StartNode = SysData.getInstance().getNodeIdByRoom(txtSNode);
        FinishNode = SysData.getInstance().getNodeIdByRoom(txtGNode);

        if(StartNode == null){
            Toast.makeText(this, "Start Poistion was not found !", Toast.LENGTH_SHORT).show();
        }
        if(FinishNode == null){
            Toast.makeText(this, "Finish goal was not found !", Toast.LENGTH_SHORT).show();
        }

        PathFinder pf = PathFinder.getInstance();
        boolean b = chElevator.isChecked();

        // if b is true then ignore the stairs (don't expand stairs node) else go through stairs
        if(!pf.FindPath(StartNode, FinishNode, b).isEmpty()) {

            Intent intent = new Intent(this, PlaceViewActivity.class);
            intent.putExtra(Constants.INDEX, 0);
            startActivity(intent);
        }else
            Toast.makeText(this, R.string.no_path_found, Toast.LENGTH_LONG).show();

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


    @Override
    public void onBeaconEvent(Beacon beacon) {
        BeaconID temp = new BeaconID(beacon.getProximityUUID(),beacon.getMajor(),beacon.getMinor());
        if(CurrentBeacon == null){
            CurrentBeacon = temp;
        }else{
            if(!CurrentBeacon.equals(temp)){
                CurrentBeacon = temp;
                if(SysData.getInstance().getNodeByBeaconID(CurrentBeacon) !=null){
                   BindText(SysData.getInstance().getNodeByBeaconID(CurrentBeacon));
                }else{
                    Toast.makeText(this, R.string.cant_find_location, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}
