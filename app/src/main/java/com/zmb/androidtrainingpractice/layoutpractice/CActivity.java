package com.zmb.androidtrainingpractice.layoutpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.R;

import java.util.ArrayList;
import java.util.List;

public class CActivity extends AppCompatActivity {
    GridView gridView = null;
    List<TextView> textViewList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        gridView = (GridView) findViewById(R.id.fileview);
        textViewList = new ArrayList<>();
        

    }
}
