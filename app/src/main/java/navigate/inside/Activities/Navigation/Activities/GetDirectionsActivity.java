package navigate.inside.Activities.Navigation.Activities;

import android.content.Intent;
import android.graphics.YuvImage;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import navigate.inside.Logic.Listeners.BeaconListener;
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
    private Intent nextAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);
        initView();
    }

    /**
     * helper method to initialize view references
     */
    private void initView() {

        sNode = (TextView)findViewById(R.id.start);
        gNode = (TextView)findViewById(R.id.goal);
        chElevator = (CheckBox)findViewById(R.id.elevator);
    }

    public void BindText(Node n){
        if(!n.getRooms().isEmpty()) {
            Room r = n.getRooms().get(0);
            sNode.setText(r.GetRoomName()+"-"+r.GetRoomNum());
        }
    }

    private void SetGNode(String s){
        this.gNode.setText(s);
    }

    public void StartListActivity(View view){
        LaunchActivity(DestinationActivity.class,Constants.REQUESTROOMNUMBER);
    }

    /**
     * handle search click event
     * @param view
     */
    public void Search(View view) {

        String txtSNode = sNode.getText().toString();
        String txtGNode = gNode.getText().toString();
        if (!txtGNode.isEmpty() && !txtSNode.isEmpty()) {

            // get nodes by the room that belongs to them
            BeaconID FinishNode;
            BeaconID StartNode;
            StartNode = SysData.getInstance().getNodeIdByRoom(txtSNode);
            FinishNode = SysData.getInstance().getNodeIdByRoom(txtGNode);

            if (StartNode != null && FinishNode != null) {
                PathFinder pf = PathFinder.getInstance();

                // if b is true then ignore the stairs (don't expand stairs node) else go through stairs
                boolean b = chElevator.isChecked();

                if (!pf.FindPath(StartNode, FinishNode, b).isEmpty()) {

                    Intent intent = new Intent(this, PlaceViewActivity.class);
                    intent.putExtra(Constants.INDEX, 0);
                    startActivity(intent);
                } else
                    Toast.makeText(this, R.string.no_path_found, Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(this, R.string.room_not_exist, Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        // register the activity to listen for nearby beacon events
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

    /**
     * helper method binds fields according to nearest beacon if found
     */
    private void doStuff(){
        if(SysData.getInstance().getNodeByBeaconID(CurrentBeacon) != null){
            BindText(SysData.getInstance().getNodeByBeaconID(CurrentBeacon));
        }else{
            Toast.makeText(this, R.string.cant_find_location, Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * triggered when a beacon event happens
     * */
    @Override
    public void onBeaconEvent(Beacon beacon) {
        BeaconID temp = new BeaconID(beacon.getProximityUUID(), String.valueOf(beacon.getMajor()),String.valueOf(beacon.getMinor()));
        if(CurrentBeacon == null || !CurrentBeacon.equals(temp)){
            CurrentBeacon = temp;
            doStuff();
        }
    }

    /**
     * helper method launches activities according to request and desired activity
     * @param act
     * @param request
     */
    private void LaunchActivity(Class<?> act,int request){
        nextAct = new Intent(this,act);

        if(request != Constants.NOREQUEST){
            startActivityForResult(nextAct,request);
        }else{
            startActivity(nextAct);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if desired data received back from the activity that was called
        if(resultCode == RESULT_OK && requestCode == Constants.REQUESTROOMNUMBER){
            SetGNode(data.getStringExtra("RoomNumber"));
        }
    }

    /**
     * handle lick event to launch MyLocationActivity activity
     * @param view
     */
    public void openfindmylocation(View view) {

        LaunchActivity(MyLocationActivity.class, Constants.NOREQUEST);
    }
}
