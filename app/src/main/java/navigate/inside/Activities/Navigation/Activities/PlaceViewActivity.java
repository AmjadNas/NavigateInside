package navigate.inside.Activities.Navigation.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.recognition.packets.Beacon;

import org.json.JSONObject;

import java.util.ArrayList;

import navigate.inside.Activities.PanoramicImageActivity;
import navigate.inside.Logic.Listeners.BeaconListener;
import navigate.inside.Logic.GridSpacingItemDecoration;
import navigate.inside.Logic.Listeners.ImageLoadedListener;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.PageAdapter;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;
import navigate.inside.Utills.Converter;
import navigate.inside.Utills.ImageLoader;

public class PlaceViewActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, BeaconListener, NetworkResListener, ImageLoadedListener{
    // layout containers
    private RecyclerView list;
    private PageAdapter listAdapter;
    // device sensor manager
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float[] rMat = new float[9];
    private float[] orientation = new float[3];
    // azimuth and current page position
    private int mAzimuth, position;
    // bottom sheet params
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout sheetLayout;
    // delay handlers
    private boolean flag;
    private Handler handler;
    private final Runnable processSensors = new Runnable() {
        private final int interval = 1000;
        @Override
        public void run() {

            flag = true;
            // The Runnable is posted to run again here:
            handler.postDelayed(this, interval);
        }
    };
    private ArrayList<Pair<Node,Integer>> itemList;
    private ArrayList<String> roomRange;
    private TextView name, direction;
    private CheckBox checkBox;
    private ImageView panoWidgetView;
    private BeaconID currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);
        roomRange = new ArrayList<>();
        position = getIntent().getIntExtra(Constants.INDEX, -1);

        if (position >= 0){
            itemList =  PathFinder.getInstance().getPath();
            initSensor();
            handler = new Handler();
            initView();
            bindPage();
            initBottomSheet();
            initRecyclerViews();

        }else
            finish();
    }

    @Override
    public void onBackPressed() {

        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }


    private void initView(){
        name = (TextView) findViewById(R.id.node_name);
        direction = (TextView) findViewById(R.id.node_direct);
        checkBox = (CheckBox) findViewById(R.id.arrive_check);
        checkBox.setOnCheckedChangeListener(this);
        panoWidgetView = (ImageView) findViewById(R.id.thumb_place_activity);
    }

    private void bindPage(){

        Node temp;

        temp = itemList.get(position).first;
        temp.isElevator();
        currentID = itemList.get(position).first.get_id();

        /*Message for Elevator*/
        if(itemList.size() > position+1){
            if(temp.isElevator() && itemList.get(position+1).first.isElevator()){
                name.setText("Next Step : Go to Floor "+itemList.get(position+1).first.getFloor()+" by elevator");
                position++;
            }
        }
        //Two Staris solution !
        /*Message for Stairs*/
        if(itemList.size() > position+1) {
            if (temp.isJunction() && itemList.get(position + 1).first.isJunction()) {
                if (Integer.parseInt(temp.getFloor()) < Integer.parseInt(itemList.get(position + 1).first.getFloor())) {
                    name.setText("Next Step : Go up the stairs to Floor " + itemList.get(position + 1).first.getFloor());
                } else {
                    name.setText("Next Step : Go down the stairs to Floor " + itemList.get(position + 1).first.getFloor());
                }
                position++;
            }
        }
        /*Getting the rooms range and display for Regular Node */
        if(!temp.isJunction() && !temp.isElevator()){
            roomRange = temp.getRoomsNumbers();
            if(roomRange.size() == 1 ){
                name.setText("Next Step : Room "+String.valueOf(roomRange.get(0)));
            }
            if(roomRange.size() > 1){
                name.setText("Next Step : Rooms "+String.valueOf(roomRange.get(0))+" - "+String.valueOf(roomRange.get(1)));
            }
            if(roomRange == null){
                Toast.makeText(this,"Room Range is null !! ",Toast.LENGTH_SHORT).show();
            }
            roomRange.clear();
        }
      //  name.setText(String.valueOf(itemList.get(position).first.get_id().getMajor()));
        direction.setText(getDirection(mAzimuth, itemList.get(position).second));
        Bitmap image = PathFinder.getInstance().getImage(position);
        if (image != null)
            new ImageLoader(currentID, this, false).execute(image);
           // loadImageto3D(image, false);

    }

    private void initBottomSheet() {
        sheetLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        final TextView textView = (TextView) sheetLayout.findViewById(R.id.path_show_lbl) ;
        textView.setOnClickListener(this);
        sheetBehavior = BottomSheetBehavior.from(sheetLayout);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    textView.setText(R.string.Showless);
                if(newState == BottomSheetBehavior.STATE_COLLAPSED)
                    textView.setText(R.string.show_path);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initSensor(){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR );
    }

    private void initRecyclerViews(){
        // list RecyclerView for bottom sheet
        list = (RecyclerView) findViewById(R.id.pathlist);

        // list adapter for bottom sheet
        listAdapter = new PageAdapter(this, itemList);

        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setAdapter(listAdapter);

        list.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        list.setItemAnimator(new DefaultItemAnimator());
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register beacon listener
        ((MyApplication)getApplication()).registerListener(this);
        ((MyApplication)getApplication()).startRanging();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        // register handler
        handler.post(processSensors);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister beacon listeners
        ((MyApplication)getApplication()).stopRanging();
        ((MyApplication)getApplication()).unRegisterListener(this);
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
        // unregister handler
        handler.removeCallbacks(processSensors);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // calculate th rotation matrix
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                // get the azimuth value (orientation[0]) in degree
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
                direction.setText(getDirection(mAzimuth, itemList.get(position).second));
                flag = false;
            }

        }
    }

    private String getDirection(int mAzimuth, int direction){
        int diff = mAzimuth - direction;
        String dir = null;

        if(diff < 44 && diff > (-44) ){
            dir = getString(R.string.GoForward);
        }else if( diff < (359) && diff > (315) ){
            dir = getString(R.string.GoForward);
        }else if( diff < (-45) && diff > (-135) ){
            dir = getString(R.string.GoRight);
        }else if( diff < (315) && diff > 225 ){
            dir = getString(R.string.GoRight);
        }else if( diff < (-135) && diff > (-225) ){
            dir = getString(R.string.TurnAround);
        }else if( diff < (225) && diff > (135) ){
            dir = getString(R.string.TurnAround);
        }else if( diff < (135) && diff > (45) ){
            dir = getString(R.string.GoLeft);
        }else if( diff < (-225) && diff > (-315) ){
            dir = getString(R.string.GoLeft);
        }
        return dir;

    }

    public void setPage(int page) {
        if(position != page) {
            if (page < itemList.size()){
                position = page;

                bindPage();
                if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }else if (page == itemList.size()){
                Toast.makeText(this, R.string.you_have_arrived, Toast.LENGTH_LONG).show();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void loadImageto3D(final Bitmap res, final boolean downloaded) {
        new AsyncTask<Bitmap,Bitmap,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Bitmap... params) {
                if (downloaded)
                    SysData.getInstance().insertImageToDB(currentID, res);

                return Converter.getImageTHumbnail(res);
            }

            @Override
            protected void onPostExecute(Bitmap aVoid) {
                if (aVoid != null)
                    panoWidgetView.setImageBitmap(aVoid);
               /* else
                    NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_ALL_NODES_JSON_REQ, itemList.get(position).first, this);
                    */
            }
        }.execute();

    }

    @Override
    public void onClick(View v) {
        if(v instanceof TextView) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ((TextView) v).setText(R.string.Showless);
            } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                ((TextView) v).setText(R.string.show_path);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /*
    * triggered when a beacon event happens
    * */
    @Override
    public void onBeaconEvent(Beacon beacon) {
        BeaconID tempID = new BeaconID(beacon.getProximityUUID(),
                beacon.getMajor(), beacon.getMinor());
        if (!currentID.equals(tempID)) {
            int index = PathFinder.getInstance().getIndexOfNode(tempID);

            if (index >= 0) {
                setPage(++index);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                new ImageLoader(currentID, this, true).execute(res);
              // loadImageto3D(res, true);
            }

        }else
            Toast.makeText(this,R.string.loadfailed, Toast.LENGTH_SHORT).show();
    }


    public void viewPanorama(View view) {
        Intent intent = new Intent(this, PanoramicImageActivity.class);
        intent.putExtra(Constants.ID, currentID);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setPage(position+1);
            checkBox.setChecked(false);
        }
    }

    @Override
    public void onImageLoaded(Bitmap image) {
        if (image != null)
            panoWidgetView.setImageBitmap(image);
               /* else
                    NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_ALL_NODES_JSON_REQ, itemList.get(position).first, this);
                    */

    }
}
