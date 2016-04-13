package com.mapfromscratch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class SimpleLocationTracker extends AppCompatActivity implements  LocationListener {
/*
* Collects only lat and long
*
* */

    private LocationManager locationManager;

    private EditText edtLat;
    private EditText edtLng;

    private TextView txtLat;
    private TextView txtLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_location_tracer);

        edtLat = (EditText) findViewById(R.id.edt_latitude);
        edtLng = (EditText) findViewById(R.id.edt_longitude);
        txtLat = (TextView) findViewById(R.id.current_lat);
        txtLong = (TextView) findViewById(R.id.current_long);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

    }


    @Override
    public void onLocationChanged(Location location) {
        txtLat.setText(String.valueOf(location.getLatitude()));
        txtLong.setText(String.valueOf(location.getLongitude()));
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

    public void backToMap(View view) {
        Intent i = new Intent(SimpleLocationTracker.this,MainActivity.class);
        startActivity(i);
    }
}