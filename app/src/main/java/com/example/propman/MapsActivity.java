package com.example.propman;
import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private  GoogleMap mMap;
    private final static int REQUEST_lOCATION=90;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference propertyref;
    int i=0;
    private String uid;
    private Property p;
    public ArrayList<Property> properties=new ArrayList<>();
    public ArrayList<Property> propertyuid=new ArrayList<>();
    private List<Address> adressList=null;
    private ArrayList<LatLng> coord=new ArrayList<>();
    private  EditText etLocation;
    private Geocoder geocoder;
    private Button btn_MapType;
    private Button btnGo;
    private String location;
    private Address address;
    private LatLng latLng;
    private String[] latlong;
    private double latitude;
    private double longitude;
    private LatLng loc;
    Spinner filter_options;
    EditText filter_search;
    EditText min_price;
    EditText max_price;
    private LinearLayout minmax_layout;
    final List<String> keys=new ArrayList<>();
    private int index;
    String text;
    String regex;
    boolean matches;
    int option=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        propertyref = mDatabase.child("propertylist");
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        etLocation=(EditText)findViewById(R.id.et_location);
        btnGo=(Button) findViewById(R.id.btn_Go);
        btn_MapType=(Button) findViewById(R.id.btn_Sat);
        geocoder=new Geocoder(MapsActivity.this);
        filter_search=(EditText) findViewById(R.id.filter_search);
        min_price=(EditText) findViewById(R.id.min_price);
        max_price=(EditText) findViewById(R.id.max_price);
        filter_options=(Spinner) findViewById(R.id.filter_options);
        minmax_layout=(LinearLayout) findViewById(R.id.minmax_layout);
        List<String> categories = new ArrayList<String>();
        categories.add("Without Filter");
        categories.add("Rooms");
        categories.add("Price");
        categories.add("Size");
        categories.add("Address");
        categories.add("Description");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_options.setAdapter(dataAdapter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ZoomControls zoom=(ZoomControls)findViewById(R.id.zoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_MapType.setText("NORMAL");
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText("SATELLITE");
                }
            }
        });
        filter_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
                 index = filter_options.getSelectedItemPosition();
                if(index==2)
                {
                    minmax_layout.setVisibility(View.VISIBLE);
                    filter_search.setFocusable(false);
                    filter_search.setClickable(false);
                    option=2;

                }else if(index==1)
                {
                    minmax_layout.setVisibility(View.GONE);
                    filter_search.setFocusable(true);
                    filter_search.setClickable(true);
                    option=1;
                }else if(index==3)
                {
                    minmax_layout.setVisibility(View.GONE);
                    filter_search.setFocusable(true);
                    filter_search.setClickable(true);
                    option=3;
                }else if(index==4)
                {
                    minmax_layout.setVisibility(View.GONE);
                    filter_search.setFocusable(true);
                    filter_search.setClickable(true);
                    option=4;
                }else if(index==5)
                {
                    minmax_layout.setVisibility(View.GONE);
                    filter_search.setFocusable(true);
                    filter_search.setClickable(true);
                    option=5;
                }else if(index==0)
                {
                    minmax_layout.setVisibility(View.GONE);
                    filter_search.setFocusable(true);
                    filter_search.setClickable(true);
                    option=0;
                }
            }
            public void onNothingSelected(AdapterView<?>arg0) {}
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mMap.clear();
                if(index==2){
                    if(max_price.getText().toString().equals("") || min_price.getText().toString().equals("")){
                        mMap.clear();
                    Toast.makeText(MapsActivity.this, "Please define the minimum and the maximum price values.",
                            Toast.LENGTH_SHORT).show();
                }
                    else if(Integer.parseInt(max_price.getText().toString())<=Integer.parseInt(min_price.getText().toString())){
                        mMap.clear();
                        Toast.makeText(MapsActivity.this, "Min price value can't be greater than or equal to max price value.",
                                Toast.LENGTH_SHORT).show();
                    }}

                location=etLocation.getText().toString();
                if(location!=null && !location.equals("")){
                    try {
                        adressList=geocoder.getFromLocationName(location,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(!adressList.isEmpty()) {
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //This will loop through all items. Add variables to arrays or lists as required
                                DataSnapshot users = dataSnapshot.child("propertylist");
                                Iterable<DataSnapshot> usershaveproperties = users.getChildren();

                                for (DataSnapshot snap : usershaveproperties) {
                                    String key = snap.getKey();
                                    keys.add(key);
                                }
                                while (i < keys.size()) {
                                    if (keys.get(i).equals(uid) == false) {
                                        DataSnapshot userpropertys = dataSnapshot.child("propertylist").child(keys.get(i));
                                        Iterable<DataSnapshot> propertydetails = userpropertys.getChildren();
                                        for (DataSnapshot userproperties_snap : propertydetails) {
                                            final Property p = userproperties_snap.getValue(Property.class);
                                            p.setUid(keys.get(i));
                                            properties.add(p);
                                        }
                                    }

                                    i++;
                                }
                                address = adressList.get(0);
                                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                if(!max_price.getText().toString().equals("")&&!min_price.getText().toString().equals("")){
                                    for (int x=0;x<properties.size();x++){
                                        if(Integer.parseInt(properties.get(x).getPrice())<=Integer.parseInt(max_price.getText().toString())&&Integer.parseInt(properties.get(x).getPrice())>=Integer.parseInt(min_price.getText().toString())){
                                            propertyuid.add(properties.get(x));
                                        }
                                    }
                                }
                                if(!filter_search.getText().toString().equals("")&&option==1){

                                    for (int x=0;x<properties.size();x++){
                                        text=properties.get(x).getRooms();
                                        regex=filter_search.getText().toString();
                                        regex += "[+]\\d*";
                                        matches = Pattern.matches(regex, text);
                                        if(matches){
                                            propertyuid.add(properties.get(x));
                                        }
                                    }}
                                if(!filter_search.getText().toString().equals("")&&option==3){

                                    for (int x=0;x<properties.size();x++){
                                        text=properties.get(x).getarea();
                                        regex=filter_search.getText().toString();
                                        regex += "\\d*";
                                        matches = Pattern.matches(regex, text);
                                        if(matches){
                                            propertyuid.add(properties.get(x));
                                        }
                                    }} if(!filter_search.getText().toString().equals("")&&option==4){

                                    for (int x=0;x<properties.size();x++){
                                        text=properties.get(x).getAddress();
                                        regex=filter_search.getText().toString();
                                        regex += "\\w*";
                                        matches = Pattern.matches(regex, text);
                                        if(matches){
                                            propertyuid.add(properties.get(x));
                                        }
                                    }} if(!filter_search.getText().toString().equals("")&&option==5){

                                    for (int x=0;x<properties.size();x++){
                                        text=properties.get(x).getDescription();
                                        regex=filter_search.getText().toString();
                                        regex += "\\w*";
                                        matches = Pattern.matches(regex, text);
                                        if(matches){
                                            propertyuid.add(properties.get(x));
                                        }
                                    }}
                                 if(index!=0&&propertyuid.isEmpty()){
                                     mMap.clear();
                                    Toast.makeText(MapsActivity.this, "There are no results.",
                                            Toast.LENGTH_SHORT).show();
                                }
                              else if(propertyuid.isEmpty()){
                                     mMap.clear();
                                for (int x = 0; x < properties.size(); x++) {
                                    if (check_in_range(latLng.toString(), properties.get(x).getCoordinate())) {
                                        latlong = properties.get(x).getCoordinate().split(",");
                                        String val1 = latlong[0].replaceAll("[^0-9.]", "");
                                        String val2 = latlong[1].replaceAll("[^0-9.]", "");
                                        latitude = Double.parseDouble(val1);
                                        longitude = Double.parseDouble(val2);
                                        loc = new LatLng(latitude, longitude);
                                        coord.add(loc);
                                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(loc).title("Title: " + properties.get(x).getTitle()).snippet("Price:"+properties.get(x).getPrice()));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
                                    }
                                }}else{
                                     mMap.clear();
                                   for (int x = 0; x < propertyuid.size(); x++) {
                                       if (check_in_range(latLng.toString(), propertyuid.get(x).getCoordinate())) {
                                           latlong = propertyuid.get(x).getCoordinate().split(",");
                                           String val1 = latlong[0].replaceAll("[^0-9.]", "");
                                           String val2 = latlong[1].replaceAll("[^0-9.]", "");
                                           latitude = Double.parseDouble(val1);
                                           longitude = Double.parseDouble(val2);
                                           loc = new LatLng(latitude, longitude);
                                           coord.add(loc);
                                           mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(loc).title("Title: " + propertyuid.get(x).getTitle()).snippet("Price:"+propertyuid.get(x).getPrice()));
                                           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
                                       }
                                   }
                                   propertyuid.clear();
                               }
                                i=0;
                                for(int size=0;size<coord.size();size++){
                                    StringBuilder sbValue = new StringBuilder(sbMethod(coord.get(size).latitude,coord.get(size).longitude));
                                    PlacesTask placesTask = new PlacesTask();
                                    placesTask.execute(sbValue.toString());
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Ankara and move the camera
        LatLng ankara = new LatLng(39.9207774, 32.854067);
        mMap.setOnInfoWindowClickListener(this);
        // mMap.addMarker(new MarkerOptions().position(ankara).title("Ankara"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ankara));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                final String l = mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //This will loop through all items. Add variables to arrays or lists as required
                        DataSnapshot users = dataSnapshot.child("propertylist");
                        Iterable<DataSnapshot> usershaveproperties = users.getChildren();

                        for (DataSnapshot snap : usershaveproperties) {
                            String key = snap.getKey();
                            keys.add(key);
                        }

                        while (i < keys.size()) {
                            if (keys.get(i).equals(uid) == false) {
                                DataSnapshot userpropertys = dataSnapshot.child("propertylist").child(keys.get(i));
                                Iterable<DataSnapshot> propertydetails = userpropertys.getChildren();
                                for (DataSnapshot userproperties_snap : propertydetails) {
                                    final Property p = userproperties_snap.getValue(Property.class);
                                    p.setUid(keys.get(i));
                                    properties.add(p);
                                }
                            }
                            i++;
                        }
                        for (int x = 0; x < properties.size(); x++) {
                            if (check_in_range(l, properties.get(x).getCoordinate())) {
                                latlong = properties.get(x).getCoordinate().split(",");
                                String val1 = latlong[0].replaceAll("[^0-9.]", "");
                                String val2 = latlong[1].replaceAll("[^0-9.]", "");
                                latitude = Double.parseDouble(val1);
                                longitude = Double.parseDouble(val2);
                                loc = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(loc).title("Title: " + properties.get(x).getTitle()).snippet("Price:"+properties.get(x).getPrice()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
                            }
                        }

                        i=0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                return false;
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_lOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"User did not give permission.",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Boolean check_in_range(String search_coordinate,String property_coordinate){

        //for search coordinate
        String[] search_coordinateList=search_coordinate.split(",");
        String intValue = search_coordinateList[0].replaceAll("[^0-9.]", "");
        String intValue2 = search_coordinateList[1].replaceAll("[^0-9.]", "");
        Double min_lat=Double.parseDouble(intValue)-0.5;
        Double min_long=Double.parseDouble(intValue2)-0.5;
        Double max_lat=Double.parseDouble(intValue)+0.5;
        Double max_long=Double.parseDouble(intValue2)+0.5;
        //for property coordinate
        String[] property_coordinateList=property_coordinate.split(",");
        String intValue3 = property_coordinateList[0].replaceAll("[^0-9.]", "");
        String intValue4 = property_coordinateList[1].replaceAll("[^0-9.]", "");
        Double property_lat= Double.parseDouble(intValue3);
        Double property_long = Double.parseDouble(intValue4);

        if(property_long>=min_long && property_long<=max_long && property_lat<=max_lat && property_lat>=min_lat){
            return true;
        }else
            return false;
    }
    public StringBuilder sbMethod(double mLatitude,double mLongitude) {

        //use your current location here


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=500");
        sb.append("&types=" + "restaurant"+"airport"+"hospital"+"zoo"+"train_station+"+"tourist_attraction"+"subway_station"+"shopping_mall"+"pharmacy"+"museum"+"movie_theater");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBPMLyvda1mJbTWxhVfhnDxbFnBOORh-MQ");

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }
    public class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
                //mMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);
                // Setting the position for the marker
                markerOptions.position(latLng);

                markerOptions.title(name + " : " + vicinity);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);
            }
        }
    }
    public void onInfoWindowClick(final Marker marker) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //This will loop through all items. Add variables to arrays or lists as required
                DataSnapshot users = dataSnapshot.child("propertylist");
                Iterable<DataSnapshot> usershaveproperties = users.getChildren();

                for (DataSnapshot snap : usershaveproperties) {
                    String key = snap.getKey();
                    keys.add(key);
                }

                while (i < keys.size()) {
                    if (keys.get(i).equals(uid) == false) {
                        DataSnapshot userpropertys = dataSnapshot.child("propertylist").child(keys.get(i));
                        Iterable<DataSnapshot> propertydetails = userpropertys.getChildren();
                        for (DataSnapshot userproperties_snap : propertydetails) {
                            final Property p = userproperties_snap.getValue(Property.class);
                            p.setUid(keys.get(i));
                            properties.add(p);
                        }
                    }
                    i++;
                }
                for (int x = 0; x < properties.size(); x++) {

                    String marker_title = marker.getTitle().replaceAll("Title: ", "");
                    Double marker_lat = marker.getPosition().latitude;
                    Double marker_long = marker.getPosition().longitude;
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    latlong = properties.get(x).getCoordinate().split(",");
                    String val1 = latlong[0].replaceAll("[^0-9.]", "");
                    String val2 = latlong[1].replaceAll("[^0-9.]", "");
                    latitude = Double.parseDouble(val1);
                    longitude = Double.parseDouble(val2);

                    if ( properties.get(x).getTitle().equals(marker_title) && latitude==marker_lat && longitude==marker_long) {
                        Intent intent = new Intent(MapsActivity.this, ViewProperty.class);
                        intent.putExtra("uid",properties.get(x).getUid());
                        ParcelableProperty parcelableProperty = new ParcelableProperty(properties.get(x));
                        intent.putExtra("property", parcelableProperty);
                        startActivity(intent);
                    }
                }
                i=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}


