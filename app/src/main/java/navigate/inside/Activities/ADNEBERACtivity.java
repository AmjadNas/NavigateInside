package navigate.inside.Activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import navigate.inside.Logic.MyApplication;
import navigate.inside.Logic.SysData;
import navigate.inside.Objects.Node;
import navigate.inside.R;

public class ADNEBERACtivity extends AppCompatActivity implements SensorEventListener {
    private Spinner node1, node2;
    private SysData data;
    private TextView text;
    private CheckBox box;

    // device sensor manager
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float[] rMat = new float[9];
    private float[] orientation = new float[3];
    // azimuth and current page position
    private int mAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adneberactivity);

        node1 = (Spinner) findViewById(R.id.spinner1);
        node2 = (Spinner) findViewById(R.id.spinner2);

        data = SysData.getInstance();

        text = (TextView)findViewById(R.id.textView);
        box = (CheckBox)findViewById(R.id.checkBox);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR );

        ArrayList<String> strings = new ArrayList<>();

        for(Node node : data.getAllNodes()){
            strings.add(node.get_id().toString());
        }

        node1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings));
        node2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings));


    }

    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    public void Save(View view) {

        String s1 = (String) node1.getSelectedItem();
        String s2 = (String) node2.getSelectedItem();


        data.linkNodes(s1,s2, mAzimuth, box.isChecked());

        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // calculate th rotation matrix
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            // get the azimuth value (orientation[0]) in degree
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            text.setText(String.valueOf(mAzimuth));


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
