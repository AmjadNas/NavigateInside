package navigate.inside.Activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import navigate.inside.Logic.PageAdapter;
import navigate.inside.Logic.PathFinder;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class PlaceViewActivity extends AppCompatActivity implements SensorEventListener/*, ViewPager.OnPageChangeListener*/ {

    private ViewPager pager;
    private PageAdapter adapter;

    private float[] rMat = new float[9];
    private float[] orientation = new float[3];
    // device sensor manager
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mAzimuth, position;
    private TextView direction, title;
    private Node currentNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        int index = getIntent().getIntExtra(Constants.INDEX, -1);

        if (index >= 0){
            // initialize your android device sensor capabilities
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR );

            currentNode = PathFinder.getInstance().getPath().get(index);

            direction = (TextView)findViewById(R.id.node_direct);
            title = (TextView)findViewById(R.id.node_name);
            title.setText(currentNode.toString());

            Log.i("Degree", index + String.valueOf(currentNode.getDirection()));

            //pager = (ViewPager) findViewById(R.id.viewpager);
            //adapter = new PageAdapter(this, PathFinder.getInstance().getPath());

            /*pager.setAdapter(adapter);
            pager.setCurrentItem(index, true);
            pager.addOnPageChangeListener(this);*/

        }else
            finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if( event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR ){
            // calculate th rotation matrix
            SensorManager.getRotationMatrixFromVector( rMat, event.values );
            // get the azimuth value (orientation[0]) in degree
            mAzimuth = (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
            setText();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
/*
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }*/

    private void setText() {

        int diff = mAzimuth - currentNode.getDirection();

        if(diff < 44 && diff > (-44) ){
            direction.setText("Go forward " + diff);
        }else if( diff < (359) && diff > (315) ){
            direction.setText("Go forward " + diff);
        }else if( diff < (-45) && diff > (-135) ){
            direction.setText("Go Right " + diff);
        }else if( diff < (315) && diff > 225 ){
            direction.setText("Go Right " + diff);
        }else if( diff < (-135) && diff > (-225) ){
            direction.setText("Turn around " + diff);
        }else if( diff < (225) && diff > (135) ){
            direction.setText("Turn around " + diff);
        }else if( diff < (135) && diff > (45) ){
            direction.setText("Go Left " + diff);
        }else if( diff < (-225) && diff > (-315) ){
            direction.setText("Go Left " + diff);
        }
    }
}
