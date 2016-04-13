package com.mapfromscratch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MainActivity extends AppCompatActivity implements  LocationListener,OnMapReadyCallback {

    private GoogleMap map;
    private LocationManager locationManager;
    Marker marker;

    private EditText edtLat;
    private EditText edtLng;

    private TextView txtLat;
    private TextView txtLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        We should explicitly check if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map = googleMap;
//        This sets find my location icon in upper right if gps is turned on
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

//                Check if gps is turned on
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                } else {//if gps is not turned on

//                    Goes to location setting to turn on gps
                    Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);
                }

                return false;
            }
        });

        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                txtLat.setText(String.valueOf(latLng.latitude));
                txtLong.setText(String.valueOf(latLng.longitude));
//                draws a circle with radius 100 m
//                drawCircle(latLng);
//                draw image
                drawOverlay(latLng,100,100);
//                if (prevPosition != latLng) {
//                    if (prevPosition == null)
//                        prevPosition = latLng;
//                    drawLine(prevPosition,latLng);
//                }
//                prevPosition = latLng;
            }
        });
    }


    public void fly(View view) {
        double lat = Double.parseDouble(edtLat.getText().toString());
        double lng = Double.parseDouble(edtLng.getText().toString());
        LatLng latLng = new LatLng(lat, lng);

        if (marker == null) {
            marker = map.addMarker(new MarkerOptions().position(latLng));
            marker.setTitle("Your position");
            marker.setDraggable(true);
        }
        else {
            marker.setPosition(latLng);
        }
//        Set camera to new location
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

    }

    private void drawCircle( LatLng location ) {
        CircleOptions options = new CircleOptions();
        options.center(location);
        //Radius in meters
        options.radius(100);
        options.fillColor(Color.argb(55,255,0,0));
        options.strokeColor(getResources()
                .getColor(R.color.colorPrimary ) );
        options.strokeWidth( 2 );

        map.addCircle(options);
    }

    private void drawLine( LatLng point,LatLng point1){

        PolygonOptions options = new PolygonOptions();
        options.add(point,point1);

        options.strokeColor(getResources()
                .getColor(R.color.colorPrimary ) );
        options.strokeWidth( 10 );

        map.addPolygon(options);
    }

    private void drawOverlay( LatLng location, int width, int height ) {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.position(location, width, height);

        options.image(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.ic_launcher)));
        map.addGroundOverlay(options);
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

    public void goToSimpleLocation(View view) {
        Intent i = new Intent(MainActivity.this,SimpleLocationTracker.class);
        startActivity(i);
    }
}