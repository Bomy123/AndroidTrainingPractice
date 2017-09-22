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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zmb.androidtrainingpractice.MainActivity;
import com.zmb.androidtrainingpractice.R;

import java.util.List;

public class BaiduMapActivity extends AppCompatActivity {
    private final String TAG = "BaiduMapActivity";
    private BMapManager bMapManager;
    private MapView mapView;
    private BaiduMap baiduMap = null;
    private ImageView ptimg, lkimg, wximg;
    private LocationManager manager;
    private Location location;
    private String provider;
    private List<String> providerlist;
    private FloatingActionButton floatingActionButton;
    private MyLocationData data;
    private BitmapDescriptor mMarker;
    private MyLocationConfiguration config;
    private LocationClient client;
    private BDLocation bdlocation;
    private float priRotation = 0;
    private float newRotation = 0;
    private boolean isFold = true;
    private float DIS = 150;
    private boolean isFirstIn = true;
    private boolean ispermissionchecked = true;
    private final int READ_PHONE_STATE = 100;
    private final int STORAGE_STATE = 101;
    private final int LOCATION_STATE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.seltype);
        mapView = (MapView) findViewById(R.id.map_view);
        ptimg = (ImageView) findViewById(R.id.normalmap);
        lkimg = (ImageView) findViewById(R.id.roadmap);
        wximg = (ImageView) findViewById(R.id.wxmap);
        ptimg.setScaleX(0.75f);
        ptimg.setScaleY(0.75f);
        lkimg.setScaleX(0.75f);
        lkimg.setScaleY(0.75f);
        wximg.setScaleX(0.75f);
        wximg.setScaleY(0.75f);
        baiduMap = mapView.getMap();

        baiduMap.setIndoorEnable(true);
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.pos);
        config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mMarker);
        baiduMap.setMyLocationConfiguration(config);
        client = new LocationClient(getApplicationContext());
        initLocation();
        client.registerLocationListener(bdllistaener);
        newRotation = 45f;
        client.start();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: client.requestLocation():" + client.requestLocation());
                Log.d(TAG, "onClick: client is started:" + client.isStarted());
                DIS = floatingActionButton.getMeasuredHeight() * 0.75f + ptimg.getMeasuredHeight() * 0.5f;
                Log.e(TAG, "onCreate: DIS = " + DIS);
                Toast.makeText(BaiduMapActivity.this, "floatActionBar", Toast.LENGTH_LONG).show();
                rotateAnim(floatingActionButton, priRotation, newRotation);

                if (isFold) {
                    Log.e(TAG, "onClick: isFold = " + isFold);
                    unfold(ptimg, 180);
                    unfold(lkimg, 225);
                    unfold(wximg, 270);
                    isFold = false;
                    newRotation = 0f;
                } else {
                    Log.e(TAG, "onClick: isFold = " + isFold);
                    isFold = true;
                    fold(ptimg, 180);
                    fold(lkimg, 225);
                    fold(wximg, 270);
                    newRotation = 45f;
                }

            }
        });
        ptimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                //mapView.renderMap();
            }
        });
        wximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                //mapView.renderMap();
            }
        });
        lkimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negateTo();
                //mapView.renderMap();
            }
        });
    }

    private void rotateAnim(View view, float fromangle, float toangle) {
        RotateAnimation animation = new RotateAnimation(fromangle, toangle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        priRotation = newRotation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFold) {
            Log.e(TAG, "onTouchEvent: isFold = " + isFold);
            rotateAnim(floatingActionButton, priRotation, newRotation);
            isFold = true;
            fold(ptimg, 180);
            fold(lkimg, 225);
            fold(wximg, 270);
            newRotation = 45f;
        }
        return super.onTouchEvent(event);
    }


    private void fold(View v, double angle) {
        float x = (float) Math.abs(DIS * Math.sin(Math.toRadians(angle)));
        float y = (float) Math.abs(DIS * Math.cos(Math.toRadians(angle)));
        Log.d(TAG, "fold: x=" + x);
        Log.d(TAG, "fold: y=" + y);
        ObjectAnimator tx = ObjectAnimator.ofFloat(v, "translationX", 0);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v, "translationY", 0);
//        ObjectAnimator alphaanim = ObjectAnimator.ofInt(v,"alpha",0);
//        ObjectAnimator sx = ObjectAnimator.ofFloat(v,"scaleX",0.75f);
//        ObjectAnimator sy = ObjectAnimator.ofFloat(v,"scaleY",0.75f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(tx).with(ty);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();


    }

    private void unfold(View v, double angle) {
        float x = (float) Math.abs(DIS * Math.sin(Math.toRadians(angle)));
        float y = (float) Math.abs(DIS * Math.cos(Math.toRadians(angle)));
        Log.d(TAG, "fold: x=" + x);
        Log.d(TAG, "fold: y=" + y);
        ObjectAnimator tx = ObjectAnimator.ofFloat(v, "translationX", -x);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v, "translationY", y);
//        ObjectAnimator alphaanim = ObjectAnimator.ofInt(v,"alpha",0);
//        ObjectAnimator sx = ObjectAnimator.ofFloat(v,"scaleX",0.75f);
//        ObjectAnimator sy = ObjectAnimator.ofFloat(v,"scaleY",0.75f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(tx).with(ty);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFold) {
            Log.d(TAG, "onClick: isFold = " + isFold);
            rotateAnim(floatingActionButton, priRotation, newRotation);
            isFold = true;
            fold(ptimg, 180);
            fold(lkimg, 225);
            fold(wximg, 270);
            newRotation = 45f;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "onResume: " + Build.VERSION.SDK_INT + ":" + Build.VERSION_CODES.M);
            checkPermissions();
        }
//        getProvider();
//        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            manager.requestLocationUpdates(provider, 5000, 1, this);
//        }

        baiduMap.setMyLocationEnabled(true);


//        MyLocationData myLocationData = new MyLocationData.Builder().accuracy()
//        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
//        config = new MyLocationConfiguration(baiduMap.getLocationConfiguration().locationMode,true,mMarker);
//        baiduMap.setMyLocationConfiguration(config);
//        baiduMap.setMyLocationEnabled(false);
//        baiduMap.setBuildingsEnabled(true);

        mapView.onResume();
//        LatLng latlng = new LatLng(115.2, 80.4);
//        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latlng);
//        baiduMap.animateMapStatus(update);
//        update = MapStatusUpdateFactory.zoomTo(16f);
//        baiduMap.animateMapStatus(update);
//        data = new MyLocationData.Builder().latitude(115.2).longitude(80.4).build();
//        baiduMap.setMyLocationData(data);
//        config = new MyLocationConfiguration(baiduMap.getLocationConfiguration().locationMode, true, mMarker);
//        baiduMap.setMyLocationConfiguration(config);


    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {

        String[] permissions = {Manifest.permission_group.LOCATION, Manifest.permission_group.STORAGE, Manifest.permission_group.SENSORS, Manifest.permission.READ_PHONE_STATE};
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaiduMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_STATE);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaiduMapActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_STATE);
        }
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaiduMapActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_STATE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: LOCATION_STATE faile");
                }
                break;
            case STORAGE_STATE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: STORAGE_STATE fail");
                    //finish();
                }
                break;
            case READ_PHONE_STATE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: READ_PHONE_STATE fail");
                    //finish();
                }
                break;
        }
    }

    private void initLocation() {
        LocationClientOption opt = new LocationClientOption();
        opt.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        opt.setCoorType("bd09ll");
        opt.setIsNeedAddress(true);
        opt.setIsNeedLocationDescribe(true);
        opt.setOpenGps(true);
        opt.setScanSpan(2000);
        opt.setIsNeedLocationPoiList(true);
        opt.setIgnoreKillProcess(true);
        opt.setWifiCacheTimeOut(5 * 60 * 1000);
        client.setLocOption(opt);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "onMarkerClick: ");
                return false;
            }
        });
    }

    BDAbstractLocationListener bdllistaener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "onReceiveLocation: LocationDescribe:" + bdLocation.getLocationDescribe());
            Log.d(TAG, "onReceiveLocation: Address:" + bdLocation.getAddress());
            Log.d(TAG, "onReceiveLocation: addr:"+bdLocation.getAddrStr());
            if (isFirstIn) {
                bdlocation = bdLocation;
                negateTo();
                isFirstIn = false;
            }
            data = new MyLocationData.Builder().direction(bdLocation.getDirection()).longitude(bdLocation.getLongitude()).latitude(bdLocation.getLatitude()).accuracy(bdLocation.getRadius()).build();
            baiduMap.setMyLocationData(data);

        }
    };

    private void negateTo() {
        if (bdlocation != null) {
            LatLng latlng = new LatLng(bdlocation.getLatitude(), bdlocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latlng);
            baiduMap.animateMapStatus(update);

        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();

        mapView.onDestroy();
    }

}