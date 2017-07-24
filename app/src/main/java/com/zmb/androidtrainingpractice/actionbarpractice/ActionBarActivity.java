package com.zmb.androidtrainingpractice.actionbarpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zmb.androidtrainingpractice.R;

public class ActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.mytoobar);

        setSupportActionBar(myToolBar);
    }
}
