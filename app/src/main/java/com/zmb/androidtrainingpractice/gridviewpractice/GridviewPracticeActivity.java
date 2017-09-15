package com.zmb.androidtrainingpractice.gridviewpractice;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.zmb.androidtrainingpractice.R;
import com.zmb.androidtrainingpractice.adapters.MPhotoWallAdapter;
import com.zmb.androidtrainingpractice.adapters.MPhotoWallAdapterWithOkhttp;
import com.zmb.androidtrainingpractice.source.UrlSource;

import java.util.Set;

public class GridviewPracticeActivity extends AppCompatActivity{

    private GridView mPhotoWall = null;
    //private MPhotoWallAdapter adapter = null;
    private MPhotoWallAdapterWithOkhttp<GridView> adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview_practice);
        mPhotoWall = (GridView) findViewById(R.id.photowall);
        adapter = new MPhotoWallAdapterWithOkhttp<>(this,0,UrlSource.imageThumbUrls,mPhotoWall);
        //adapter = new MPhotoWallAdapter(this,0,UrlSource.imageThumbUrls,mPhotoWall);
        mPhotoWall.setAdapter(adapter);
        mPhotoWall.setOnScrollListener(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //adapter.cancelAllTask();
    }
}
