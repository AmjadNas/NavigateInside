package navigate.inside.Activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import navigate.inside.Logic.PageAdapter;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class PlaceViewActivity extends AppCompatActivity implements SensorEventListener/*, ViewPager.OnPageChangeListener*/ {
    // layout containers
    private RecyclerView pager,list;
    private PageAdapter pageAdapter,listAdapter;
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
    // scrolling listener for finding current view position
    private SnapHelper snapHelper;
    private final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                View centerView = snapHelper.findSnapView(hLayoutManager);
                position = hLayoutManager.getPosition(centerView);
            }
        }
    };
    private RecyclerView.LayoutManager hLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        position = getIntent().getIntExtra(Constants.INDEX, -1);

        if (position >= 0){
            // initialize your android device sensor capabilities
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR );

            handler = new Handler();
            // bottom sheet init
            sheetLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
            sheetBehavior = BottomSheetBehavior.from(sheetLayout);

            pager = (RecyclerView) findViewById(R.id.path_pages);
            // list RecyclerView for bottom sheet
            list = (RecyclerView) findViewById(R.id.pathlist);

            pageAdapter = new PageAdapter(this, PathFinder.getInstance().getPath(), true);
            // list adapter for bottom sheet
            listAdapter = new PageAdapter(this, PathFinder.getInstance().getPath(), false);

            hLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            pager.setLayoutManager(hLayoutManager);

            list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

            list.setAdapter(listAdapter);
            pager.setAdapter(pageAdapter);

            snapHelper = new LinearSnapHelper(){};
            snapHelper.attachToRecyclerView(pager);
            pager.addOnScrollListener(listener);

        }else
            finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        // register handler
        handler.post(processSensors);
    }

    @Override
    protected void onPause() {
        super.onPause();

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
                pageAdapter.reBindElementAt(mAzimuth, position);

                flag = false;
            }

        }
    }

    public void setPage(int page) {
        position = page;
        pager.scrollToPosition(page);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
