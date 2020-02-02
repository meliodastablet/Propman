package com.example.propman;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProperty extends AppCompatActivity implements View.OnClickListener {
    String filepath_count="";
    private ArrayList<Uri> uris=new ArrayList<>();
    TextView tw4,tw5,tw6,tw7,tw8,tw9,tv_garage,tv_garden,tv_pool,tv_security,tv_dues;
    ImageView iw2;
    Button delete;
    Button request;
    boolean ff = false;
    Button edit;
    boolean flag=false;
    boolean flag2 = false;
    boolean flag3= false, flag4 = false;
    boolean q = false;
    int val;
    String value="";
    String coordinate="";
    String title="";
    String price="";
    int queue= 0;
    int click_count=1;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAERSaSd4:APA91bFoMzFZ8RnrhePzG8a36Cujw9T-ZvH16vzsyGuzHurYIKOOwU8dV6kGaF0ghlFbJ5zO5ljar6lwI5NbdWmk06ek8p3jEKrcZJ8lKdh8t6buk_UcNJew5Zj6R2FYpzp7zFeWda6g";
    final private String contentType = "application/json";
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference propertydatabase;
    DatabaseReference propertyref;
    DatabaseReference requestq;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Property property;
    String uid,uid2;
    String ruid="";
    String useruid="";
    ArrayList<String> idlist = new ArrayList<>();
    private ArrayList<String> tenant=new ArrayList<>();
    private ArrayList<String> tenant_price=new ArrayList<>();
    public static boolean solver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        setTitle("View");

        propertydatabase = FirebaseDatabase.getInstance().getReference();

        tw4 = findViewById(R.id.textView4);
        request = findViewById(R.id.request);
        tw5 = findViewById(R.id.textView5);
        tw6 = findViewById(R.id.textView6);
        tw7 = findViewById(R.id.textView7);
        tw8 = findViewById(R.id.textView8);
        tw9 = findViewById(R.id.textView9);
        iw2 = findViewById(R.id.imageView2);
        tv_security = findViewById(R.id.tv_security);
        tv_pool = findViewById(R.id.tv_pool);
        tv_garden = findViewById(R.id.tv_garden);
        tv_garage = findViewById(R.id.tv_garage);

        tv_dues = findViewById(R.id.tv_dues);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.request).setOnClickListener(this);
        findViewById(R.id.smess).setOnClickListener(this);
        findViewById(R.id.reqq).setOnClickListener(this);
        findViewById(R.id.show_profile).setOnClickListener(this);
        findViewById(R.id.show_map).setOnClickListener(this);
        findViewById(R.id.tenant_hist).setOnClickListener(this);

        uid=user.getUid();
        Intent intent = getIntent();
        ParcelableProperty parcelableProperty = (ParcelableProperty) intent
                .getParcelableExtra("property");
        property = parcelableProperty.getProperty();

        uid2 =intent.getExtras().getString("uid");
        System.out.println("UID FIRST APPERIANCE "+ uid2);
        System.out.println(uid);
        if(uid2 == null){
            uid2 = uid;
        }
        if(uid.equals(uid2)){
            System.out.println("UID 2 APPERIANCE "+ uid2);
            findViewById(R.id.request).setVisibility(View.GONE);
            findViewById(R.id.smess).setVisibility(View.GONE);
            findViewById(R.id.show_profile).setVisibility(View.GONE);
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
            findViewById(R.id.edit).setVisibility(View.VISIBLE);



        }
        else{
            findViewById(R.id.delete).setVisibility(View.GONE);
            findViewById(R.id.edit).setVisibility(View.GONE);
            findViewById(R.id.request).setVisibility(View.VISIBLE);
            findViewById(R.id.smess).setVisibility(View.VISIBLE);
            findViewById(R.id.show_profile).setVisibility(View.VISIBLE);

        }

        propertyref = propertydatabase;

        propertyref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot qusers = dataSnapshot.child("requestq").child(uid2).child(property.getUniquepropertyid());

                System.out.println("ooooooooo" + property.getUniquepropertyid());
                Iterable<DataSnapshot> userdetails=qusers.getChildren();

                for (DataSnapshot snap :userdetails)
                {
                    System.out.println("GRfrhtjyuki");
                    String ss=snap.getValue().toString();
                    System.out.println("ss" + ss);
                    idlist.add(ss);



                }

                System.out.println("uid2" + uid2);
                System.out.println("uid" + uid);
                System.out.println(idlist);

                if (dataSnapshot.child("propertylist").hasChild(uid2)) {

                    value = dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("rvalue").getValue().toString();
                    val = Integer.parseInt(value);
                    useruid=dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("uid").getValue().toString();
                    coordinate = dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("coordinate").getValue().toString();
                    title = dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("title").getValue().toString();
                    price = dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("price").getValue().toString();
                    ruid = dataSnapshot.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("ruid").getValue().toString();
                    System.out.println("val" + val);
                    System.out.println("ruid" + ruid);


                    if(val == 0){

                        request.setText("Send rent request");
                    }
                    else if(val == 1 && uid2.equals(uid)){
                        findViewById(R.id.reqq).setVisibility(View.VISIBLE);

                    }
                    else if(val == 1){
                        flag = false;
                        for(int i=0; i<idlist.size(); i++){
                            if(idlist.get(i).equals(uid)){
                                flag = true;
                            }

                        }
                        if(flag){
                            request.setText("Cancel Rent Request");
                        }else{
                            request.setText("Send rent request");
                        }





                    }
                    else if(val == 2 && ruid.equals(uid)){
                        request.setText("Cancel rented property");
                    }
                    else if(val == 2 && !ruid.equals(uid2)){

                        for(int i=0; i<idlist.size(); i++){
                            if(idlist.get(i).equals(uid)){
                                flag2 = true;
                            }

                        }
                        if(flag2){
                            request.setText("Cancel Reserve Request");
                        }else{
                            request.setText("Enter reserve queue");
                        }



                    }
                    else{

                        request.setText("Unexpected error");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tw4.setText(property.getTitle());
        tw5.setText(property.getPrice());
        tw6.setText(property.getRooms());
        tw9.setText(property.getarea() + "");
        tw8.setText(property.getDescription());
        tw7.setText(property.getAddress());

        tv_dues.setText(property.getDues());
        if(property.getGarage().equals("1")){
            tv_garage.setText("Yes");

        }else{
            tv_garage.setText("No");

        }
        if(property.getGarden().equals("1")){
            tv_garden.setText("Yes");

        }else{
            tv_garden.setText("No");

        }
        if(property.getPool().equals("1")){
            tv_pool.setText("Yes");

        }else{
            tv_pool.setText("No");

        }
        if(property.getSecurity().equals("1")){
            tv_security.setText("Yes");
        }else{
            tv_security.setText("No");
        }

        //image

        try{
            storageRef.child("images/" + property.getUniquepropertyid() + "/" + 0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    custom(uri);
                    uris.add(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }catch(Exception e){
            Toast.makeText(ViewProperty.this,
                    "Something went wrong while trying to get Image from the database. Check your internet connection.",
                    Toast.LENGTH_SHORT).show();

        }
    }

    public void notification(String uid,String NOTIFICATION_TITLE, String NOTIFICATION_MESSAGE, String type){


        String TOPIC = "/topics/" + uid ;


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {

            notifcationBody.put("type", type);
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id", user.getUid());

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);

        } catch (JSONException e) {

        }

        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ViewProperty.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void custom(Uri uri){
        Glide.with(this).load(uri).into(iw2);
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.delete) {
            propertydatabase.child("propertylist").child(uid).child(property.getUniquepropertyid()).removeValue();
            Intent intent = new Intent(getApplicationContext(), Edit_profile.class);
            startActivity(intent);
        }else if(v.getId() == R.id.edit){
            String puid = property.getUniquepropertyid();
            Intent intent = new Intent(ViewProperty.this, EditProperty.class);
            intent.putExtra("puid", puid);
            startActivity(intent);
        }else if(v.getId() == R.id.show_profile){
            Intent i = new Intent(ViewProperty.this,Show_profile.class);
            i.putExtra("uid", useruid);
            startActivity(i);
        }else if(v.getId() == R.id.show_map){
            Intent i = new Intent(ViewProperty.this,Show_on_map.class);
            i.putExtra("coordinate", coordinate);
            i.putExtra("title", title);
            i.putExtra("price", price);
            startActivity(i);
        }
        else if(v.getId() == R.id.reqq){
            Intent i = new Intent(ViewProperty.this, RequestQueue.class);
            i.putExtra("propuid",property.getUniquepropertyid());
            i.putExtra("uid",uid2);
            startActivity(i);

        }
        else if(v.getId() == R.id.smess){
            Intent intent = new Intent(getApplicationContext(), Messaging.class);
            intent.putExtra("uidx", uid2);
            System.out.println("UID 4th"+ uid2);
            solver = true;
            startActivity(intent);

        }

        else if(v.getId() == R.id.request){



            if(val == 0){

                propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("rvalue").setValue("1");
                propertydatabase.child("requestq").child(uid2).child(property.getUniquepropertyid()).child(uid).setValue(uid);
                notification(uid2,"Rent Request Received","You got a rent request for your property.","RENT");
                request.setText("Request Sent.");

            }

            else if(val == 1){


                if(flag){
                    propertydatabase.child("requestq").child(uid2).child(property.getUniquepropertyid()).child(uid).removeValue();
                    request.setText("Canceled request.");
                }else{
                    propertydatabase.child("requestq").child(uid2).child(property.getUniquepropertyid()).child(uid).setValue(uid);
                    notification(uid2,"Rent Request Received","You got a rent request for your property.","RENT");
                    request.setText("Request Sent.");


                }


            }
            else if(val == 2){

                if(ruid.equals(uid)){

                    propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("rvalue").setValue("1");
                    propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("ruid").setValue("X");
                    notification(uid2,"Your rental is available again","Your tennant canceled his rent.","" );
                    request.setText("Canceled.");

                }else{
                    if(flag2){
                        propertydatabase.child("requestq").child(uid2).child(property.getUniquepropertyid()).child(uid).removeValue();
                        request.setText("Canceled request.");
                    }else{

                        propertydatabase.child("requestq").child(uid2).child(property.getUniquepropertyid()).child(uid).setValue(uid);
                        request.setText("Request Sent.");
                    }


                }


            }
            else{
                Toast.makeText(ViewProperty.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }


        }

        else if(v.getId() == R.id.tenant_hist){
            Intent i = new Intent(this, View_Tenant_History.class);
            i.putExtra("prop", property.getUniquepropertyid());
            i.putExtra("id",user.getUid());
            startActivity(i);



        } else if(v.getId() == R.id.right){

            try{

                storageRef.child("images/" + property.getUniquepropertyid() + "/" + click_count).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        custom(uri);
                        if(click_count < Integer.parseInt(property.getFilepath_count())-1)
                        {
                            click_count++;
                        }else{
                            click_count =0;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }catch(Exception e){
                Toast.makeText(ViewProperty.this,
                        "Something went wrong while trying to get Image from the database. Check your internet connection.",
                        Toast.LENGTH_SHORT).show();

            }

        }



    }
}
