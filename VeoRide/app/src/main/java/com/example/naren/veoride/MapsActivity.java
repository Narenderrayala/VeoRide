package com.example.naren.veoride;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<LatLng>listPoints;
    ArrayList<LatLng>listpoints1;
    LatLng userlocation, l1;
    Boolean flag=false;
    Button B1;
    int count=0;

    private static final int LOCATION_REQUEST=500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        B1=(Button)findViewById(R.id.button);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        listPoints=new ArrayList<>();
        listpoints1=new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {

                userlocation=new LatLng(location.getLatitude(),location.getLongitude());
                if(count==0) {
                    mMap.addMarker(new MarkerOptions().position(userlocation).title("User current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,19));
                }


                listpoints1.add(userlocation);
                count++;
               PolylineOptions lineOptions = new PolylineOptions();


                lineOptions.addAll(listpoints1);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
                mMap.addPolyline(lineOptions);
                //mMap. setMyLocationEnabled(true);

                float[] distance = new float[2];

                if(flag) {
                    Location.distanceBetween(userlocation.latitude, userlocation.longitude,
                            l1.latitude, l1.longitude, distance);

                    if (distance[0] < 25) {

                        Toast.makeText(MapsActivity.this, "Reached", Toast.LENGTH_SHORT).show();
                    }
                }


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
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        else
        {
            mMap. setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                l1=latLng;
                flag=true;
                if(listPoints.size()==2)
                {
                    listPoints.clear();
                    mMap.clear();
                }
                    listPoints.add(userlocation);
                    listPoints.add(latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));





            }
        });




    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mMap. setMyLocationEnabled(true);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
                break;
        }
    }
    public void click(View view)
    {

    }
}
