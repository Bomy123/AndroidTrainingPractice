package com.zmb.androidtrainingpractice.baidumappractice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by zhangmingbao on 17-9-13.
 */
public class LocationUtils {
    private Context context;
    private LocationListener listener;
    private LocationManager manager;
    private Location location;
    private String provider;
    private List<String> providerlist;

    public LocationUtils(Context context) {
        this.context = context;
    }

    public void start(LocationListener locationListener) {
        this.listener = locationListener;
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(provider, 5000, 1, listener);
    }
    private void chooseBestProvider()
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


    }
}
