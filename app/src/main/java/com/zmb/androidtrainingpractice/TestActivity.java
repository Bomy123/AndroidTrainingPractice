package com.zmb.androidtrainingpractice;

import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        

    }
}
