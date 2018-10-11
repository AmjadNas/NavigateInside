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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.Beacon;

import org.json.JSONObject;

import java.util.ArrayList;

import navigate.inside.Activities.PanoramicImageActivity;
import navigate.inside.Logic.Compass;
import navigate.inside.Logic.Listeners.BeaconListener;
import navigate.inside.Logic.GridSpacingItemDecoration;
import navigate.inside.Logic.Listeners.ImageLoadedListener;
import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.PageAdapter;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkConnector;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;
import navigate.inside.Utills.Converter;
import navigate.inside.Utills.ImageLoader;

public class PlaceViewActivity extends AppCompatActivity implements View.OnClickListener, BeaconListener, ImageLoadedListener, Compass.CompassListener {
    // layout containers
    private RecyclerView list;
    private PageAdapter listAdapter;
    // device sensor manager
    private Compass compass;
    // page position in path
    private int position;
    private int dir;
    // bottom sheet params
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout sheetLayout;

    private ArrayList<Pair<Node,Integer>> itemList;
    private TextView name;
    private ImageView panoWidgetView;
    private BeaconID currentID;
    private ImageView arrowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);
        position = getIntent().getIntExtra(Constants.INDEX, -1);
        // if position is not out of bounds
        if (position >= 0){
            itemList =  PathFinder.getInstance().getPath();
            initSensor();
            initView();
            bindPage();
            initBottomSheet();
            initRecyclerViews();

        }else
            finish();
    }

    /**
     * handle back pressed events
     */
    @Override
    public void onBackPressed() {

        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    /**
     * helper method initialize the view elements
     */
    private void initView(){
        name = (TextView) findViewById(R.id.node_name);
        panoWidgetView = (ImageView) findViewById(R.id.thumb_place_activity);
        arrowView = (ImageView) findViewById(R.id.arrow_dir);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // render the custom menu icons
        getMenuInflater().inflate(R.menu.activity_place_view_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionArrive) {
            // check for new changes in data

            setPage(position+1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * helper method binds page according to the position of the user on the path
     */
    private void bindPage(){

        Node next = itemList.get(position).first;
        currentID = next.get_id();
        dir = itemList.get(position).second;
        Node curr = itemList.get(position-1).first;

        // if (currentID == null)
        if (curr.isJunction() || curr.isElevator()){
            name.setText(String.format(getString(R.string.nextStep), "Go to Floor " + next.getFloor()));
        }else
            name.setText(String.format(getString(R.string.nextStep), next.getRoomsRange()));
      /*  if( (position-1) >=0 ){
            if(itemList.get(position).first.isJunction() && itemList.get(position-1).first.isJunction()){
                position++;
            }
            if(itemList.get(position).first.isElevator()&& itemList.get(position-1).first.isElevator()){
                position++;
            }

        }*/
        //Two Staris solution !
        /*Message for Stairs*/


        /*Getting the rooms range and display for Regular Node */
        /*if(itemList.size() > position+1) {
            if((temp.isJunction() && !itemList.get(position+1).first.isJunction()) || (temp.isElevator()&& !itemList.get(position+1).first.isElevator())){
                name.setText(String.format(getString(R.string.nextStep), itemList.get(position).first.getRoomsRange()));
            }else{
                name.setText(String.format(getString(R.string.nextStep), temp.getRoomsRange()));
            }
        }else{
            name.setText(String.format(getString(R.string.nextStep), temp.getRoomsRange()));
        }*/

          /*Message for Elevator*/
        /*if(itemList.size() > position+1){
            if(temp.isElevator() && itemList.get(position+1).first.isElevator()){

            }
        }

        if(itemList.size() > position+1) {
            if (temp.isJunction() && itemList.get(position + 1).first.isJunction()) {

                if (Integer.parseInt(temp.getFloor()) < Integer.parseInt(itemList.get(position + 1).first.getFloor())) {
                    name.setText("Next Step : Go up the stairs to Floor " + itemList.get(position + 1).first.getFloor());
                } else {
                    name.setText("Next Step : Go down  the stairs to Floor " + itemList.get(position + 1).first.getFloor());
                }

            }
        }*/


        //  name.setText(String.vaterection(mAzimuth, itemList.get(position).second));
        Bitmap image = bindImage();
        if (image != null)
            new ImageLoader(currentID, -1, this).execute(image);
        else {
            panoWidgetView.setImageDrawable(getDrawable(R.drawable.noimage));
        }

    }

    /**
     * helper method to initialize the bottom sheet layout and it's behaviour
     */
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
        compass = new Compass(this);
        compass.setListener(this);
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

    /**
     * rotates navigation arrow according to the given azimuth
     * @param azimuth given from compass range from 0 to 359
     */
    private void adjustArrow(float azimuth) {
        float d = (float) dir - azimuth;
        Animation an = new RotateAnimation(d, azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);


        an.setDuration(1000);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    /**
     * helper method that converts density pixels to pixels
     * @param dp given dp
     * @return converted pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this );

        // register beacon listener
        ((MyApplication)getApplication()).registerListener(this);
        ((MyApplication)getApplication()).startRanging();

        // for the system's orientation sensor registered listeners
        compass.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister beacon listeners
        ((MyApplication)getApplication()).stopRanging();
        ((MyApplication)getApplication()).unRegisterListener(this);
        // to stop the listener and save battery
        compass.stop();
    }

    /**
     * handle the event of discovering a new angle
     * @param azimuth from 0 to 359
     */
    @Override
    public void onNewAzimuth(float azimuth) {
        adjustArrow(azimuth);
    }

    /**
     * helper method to get the right direction call for the user
     * @param mAzimuth given direction from compass 0 to 359
     * @param direction given direction for destination
     * @return the correct call where the user should
     */
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

    /**
     * helper method to load image
     * @return
     */
    private Bitmap bindImage(){
        // sysdata get image with dir and node id
       Bitmap res =  SysData.getInstance().getImageForPair(itemList.get(position-1).first.get_id(), currentID);
       return res;
    }
    public void setPage(int page) {
        if (page == 0)
            page = 1;
        if(position != page) {
            if (page < itemList.size()){

                position = page;

                bindPage();

            }else if (page == itemList.size()){
                Toast.makeText(this, R.string.you_have_arrived, Toast.LENGTH_LONG).show();
            }
        }
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * handle click for textview for the bottom sheet
     * @param v
     */
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
    /**
    * triggered when a beacon event happens
    * */
    @Override
    public void onBeaconEvent(Beacon beacon) {
       BeaconID tempID = new BeaconID(beacon.getProximityUUID(),
                String.valueOf(beacon.getMajor()), String.valueOf(beacon.getMinor()));
        if (!currentID.equals(tempID)) {

            int index = PathFinder.getInstance().getIndexOfNode(tempID);

            if (index >= 0) {
                currentID = tempID;
                setPage(index+1);
            }
        }
    }
    /**
     * handle image click to open panoramic view
     * @param view
     */
    public void viewPanorama(View view) {
        Intent intent = new Intent(this, PanoramicImageActivity.class);
        intent.putExtra(Constants.ID, itemList.get(position-1).first.get_id());
        intent.putExtra(Constants.SecondID, currentID);
        startActivity(intent);
    }

    /**
     * implemented method listen for image loading
     * @param image
     */
    @Override
    public void onImageLoaded(Bitmap image) {
        if (image != null)
            panoWidgetView.setImageBitmap(image);

    }

     /* @SuppressLint("StaticFieldLeak")
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
            }
        }.execute();

    }*/



}
