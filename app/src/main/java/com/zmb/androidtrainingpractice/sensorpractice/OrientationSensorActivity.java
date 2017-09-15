package com.zmb.androidtrainingpractice.sensorpractice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zmb.androidtrainingpractice.R;

public class OrientationSensorActivity extends AppCompatActivity {
    private static final String TAG = "OrientationSensor";
    private Sensor accSensor = null;
    private Sensor magSensor = null;
    private SensorManager sensorManager = null;
    private float[] r = new float[9];
    private float[] accVal = new float[3];
    private float[] magVal = new float[3];
    private float[] vals = new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_sensor);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener,magSensor,SensorManager.SENSOR_DELAY_NORMAL);

    }
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                accVal = event.values.clone();
            }
            if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                magVal = event.values.clone();
            }
            SensorManager.getRotationMatrix(r,null,accVal,magVal);
            SensorManager.getOrientation(r,vals);
            Log.d(TAG, "onSensorChanged: x:"+Math.toDegrees(vals[0])+"  y:"+Math.toDegrees(vals[1])+"  z:"+Math.toDegrees(vals[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(listener);
    }
}
