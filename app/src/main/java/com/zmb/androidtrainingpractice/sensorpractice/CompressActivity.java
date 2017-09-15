package com.zmb.androidtrainingpractice.sensorpractice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.zmb.androidtrainingpractice.MainActivity;
import com.zmb.androidtrainingpractice.R;

public class CompressActivity extends AppCompatActivity {
    private float lastrotation = 0;
    private ImageView plateview = null;
    private static final String TAG = "CompressActivity";
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
        setContentView(R.layout.activity_compress);
        plateview = (ImageView) findViewById(R.id.plate);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener,magSensor,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();

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

            float rotation = -(float)Math.toDegrees(vals[0]);
            //Log.d(TAG, "onSensorChanged: "+rotation + ":" + lastrotation + ":" + Math.abs(rotation - lastrotation));
            if(Math.abs(rotation - lastrotation) >= 0.5f){
                RotateAnimation animation = new RotateAnimation(lastrotation,rotation, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);
                plateview.startAnimation(animation);
                lastrotation = rotation;
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
