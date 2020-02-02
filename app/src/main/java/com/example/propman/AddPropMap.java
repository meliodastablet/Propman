package com.example.propman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ZoomControls;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class AddPropMap extends AppCompatActivity
        implements OnMapReadyCallback {
    Marker marker;
LatLng coor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_addpropmap);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final Button btn_MapType=(Button) findViewById(R.id.sat);
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_MapType.setText("NORMAL");
                }else{
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText("UYDU");
                }
            }
        });

        Button btn = findViewById(R.id.ping);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coor == null){
                    Toast.makeText(AddPropMap.this, "Choose a location" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(AddPropMap.this,AddProperty.class);
                    i.putExtra("coordinate",coor.toString());
                    startActivity(i);
                }
            }
        });

        UiSettings uiSettings;
        uiSettings= googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
           googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION }, 1);

        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( 39.925533, 32.866287),10));


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Your house is marked!"));
                coor = latLng;
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(AddPropMap.this, marker.getPosition().toString() , Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                String l = googleMap.getMyLocation().getLatitude() + "," + googleMap.getMyLocation().getLongitude();
                System.out.println(l);
                return false;
            }
        });



    }




}