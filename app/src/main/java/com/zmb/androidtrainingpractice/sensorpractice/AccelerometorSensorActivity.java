package com.zmb.androidtrainingpractice.sensorpractice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zmb.androidtrainingpractice.R;

public class AccelerometorSensorActivity extends AppCompatActivity {
    private static final String TAG = "AccelerometorSensor";
    private Sensor sensor = null;
    private SensorManager sensorManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometor_sensor);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);

    }
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xVal = Math.abs(event.values[0]);
            float yVal = Math.abs(event.values[0]);
            float zVal = Math.abs(event.values[0]);
            if(xVal >= 15 || yVal >= 15 || zVal >= 15){
                Log.d(TAG, "onSensorChanged:hold on your phone");
            }
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
