package com.zmb.androidtrainingpractice.baidumappractice;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.FloatArrayEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.zmb.androidtrainingpractice.MainActivity;
import com.zmb.androidtrainingpractice.R;

import java.util.List;

public class BaiduMapActivity extends AppCompatActivity implements LocationListener{
    private final   String TAG = "BaiduMapActivity";
    private BMapManager bMapManager;
    private MapView mapView;
    private BaiduMap baiduMap = null;
    private ImageView ptimg,lkimg,wximg;
    private LocationManager manager;
    private Location location;
    private String provider;
    private List<String> providerlist;
    private FloatingActionButton floatingActionButton;
    private float priRotation = 0;
    private float newRotation = 0;
    private boolean isFold = true;
    private final int DIS = 100;
    private boolean isFirstIn = true;
    private final int READ_PHONE_STATE = 100;
    private final int STORAGE_STATE = 101;
    private final int LOCATION_STATE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapView = (MapView) findViewById(R.id.map_view);
        ptimg = (ImageView) findViewById(R.id.normalmap);
        lkimg = (ImageView) findViewById(R.id.roadmap);
        wximg = (ImageView) findViewById(R.id.wxmap);
        baiduMap = mapView.getMap();
        baiduMap.setIndoorEnable(true);

        //baiduMap.setBaiduHeatMapEnabled(true);
//        baiduMap.setCompassEnable(true);
        baiduMap.setMyLocationEnabled(true);
//        MyLocationData myLocationData = new MyLocationData.Builder().accuracy()
        baiduMap.setBuildingsEnabled(true);

        newRotation = 45f;
        floatingActionButton = (FloatingActionButton) findViewById(R.id.seltype);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaiduMapActivity.this, "floatActionBar", Toast.LENGTH_LONG).show();
                RotateAnimation animation = new RotateAnimation(priRotation, newRotation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                floatingActionButton.startAnimation(animation);
                priRotation = newRotation;

                if(isFold){
                    Log.d(TAG, "onClick: isFold = " + isFold);
                    unfold(ptimg,180);
                    unfold(lkimg,225);
                    unfold(wximg,270);
                    isFold = false;
                    newRotation = 0f;
                }
                else {
                    Log.d(TAG, "onClick: isFold = " + isFold);
                    isFold = true;
                    fold(ptimg,180);
                    fold(lkimg,225);
                    fold(wximg,270);
                    newRotation = 45f;
                }

            }
        });
        ptimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

                mapView.renderMap();
            }
        });
        wximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                mapView.renderMap();
            }
        });
        lkimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                baiduMap.setTrafficEnabled(true);
                mapView.renderMap();
            }
        });
    }
private void refresh()
{
    getProvider();
    if(provider == null)
        return;

}
    private void fold(View v,double angle) {
        float x = (float) Math.abs(DIS*Math.sin(Math.toRadians(angle)));
        float y = (float) Math.abs(DIS*Math.cos(Math.toRadians(angle)));
        Log.d(TAG, "fold: x="+x);
        Log.d(TAG, "fold: y="+y);
        ObjectAnimator tx = ObjectAnimator.ofFloat(v,"translationX",0);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v,"translationY",0);
//        ObjectAnimator alphaanim = ObjectAnimator.ofInt(v,"alpha",0);
        ObjectAnimator sx = ObjectAnimator.ofFloat(v,"scaleX",0.75f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(v,"scaleY",0.75f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(tx).with(ty).with(sx).with(sy);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();


    }

    private void unfold(View v,double angle) {
        float x = (float) Math.abs(DIS*Math.sin(Math.toRadians(angle)));
        float y = (float) Math.abs(DIS*Math.cos(Math.toRadians(angle)));
        Log.d(TAG, "fold: x="+x);
        Log.d(TAG, "fold: y="+y);
        ObjectAnimator tx = ObjectAnimator.ofFloat(v,"translationX",-x);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v,"translationY",y);
//        ObjectAnimator alphaanim = ObjectAnimator.ofInt(v,"alpha",0);
        ObjectAnimator sx = ObjectAnimator.ofFloat(v,"scaleX",0.75f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(v,"scaleY",0.75f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(tx).with(ty).with(sx).with(sy);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

private void getProvider()
{
    providerlist = manager.getProviders(true);
    if(providerlist.contains(LocationManager.GPS_PROVIDER)){
        provider = LocationManager.GPS_PROVIDER;
        return;
    }
    if(providerlist.contains(LocationManager.NETWORK_PROVIDER)){
        provider = LocationManager.NETWORK_PROVIDER;
        return;
    }
    if(isFirstIn){

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("请打开位置服务，推荐使用GPS！").setCancelable(true).setTitle("请打开位置服务：");
        builder.setNegativeButton("谢谢，不用了",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("去打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        checkPermissions();
        getProvider();
    }
@TargetApi(Build.VERSION_CODES.M)
private void checkPermissions()
{

    String[] permissions = {Manifest.permission_group.LOCATION,Manifest.permission_group.STORAGE,Manifest.permission_group.SENSORS,Manifest.permission_group.PHONE};
    if(checkSelfPermission(Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission_group.LOCATION},LOCATION_STATE);
    }
    if(checkSelfPermission(Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission_group.STORAGE},STORAGE_STATE);
    }
    if(checkSelfPermission(Manifest.permission_group.PHONE) != PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission_group.PHONE},READ_PHONE_STATE);
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_STATE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    finish();
                }
                break;
            case STORAGE_STATE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    finish();
                }
                break;
            case READ_PHONE_STATE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
