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
import java.util.HashMap;
import java.util.Map;

public class ViewProperty extends AppCompatActivity implements View.OnClickListener {
    Uri filePath;
    TextView tw4,tw5,tw6,tw7,tw8,tw9;
    ImageView iw2;
    Button delete;
    Button request;
    Button edit;
    boolean flag=false;
    boolean q = false;
    int val;
    String value="";
    int queue= 0;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAERSaSd4:APA91bFoMzFZ8RnrhePzG8a36Cujw9T-ZvH16vzsyGuzHurYIKOOwU8dV6kGaF0ghlFbJ5zO5ljar6lwI5NbdWmk06ek8p3jEKrcZJ8lKdh8t6buk_UcNJew5Zj6R2FYpzp7zFeWda6g";
    final private String contentType = "application/json";
    FirebaseStorage storage;
    DatabaseReference propertydatabase;
    DatabaseReference propertyref;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Property property;
    String uid,uid2;
    String ruid="";
    String useruid="";
    public static boolean solver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        setTitle("View");
        storage=FirebaseStorage.getInstance();
        propertydatabase = FirebaseDatabase.getInstance().getReference();
        StorageReference storageRef = storage.getReference();
        tw4 = findViewById(R.id.textView4);
        request = findViewById(R.id.request);
        tw5 = findViewById(R.id.textView5);
        tw6 = findViewById(R.id.textView6);
        tw7 = findViewById(R.id.textView7);
        tw8 = findViewById(R.id.textView8);
        tw9 = findViewById(R.id.textView9);
        iw2 = findViewById(R.id.imageView2);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.request).setOnClickListener(this);
        findViewById(R.id.smess).setOnClickListener(this);
        findViewById(R.id.accept).setOnClickListener(this);
        findViewById(R.id.deny).setOnClickListener(this);
        findViewById(R.id.show_profile).setOnClickListener(this);
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

        propertyref = propertydatabase.child("propertylist");
        propertyref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid2)) {
                    queue = Integer.parseInt(dataSnapshot.child(uid2).child(property.getUniquepropertyid()).child("queue").getValue().toString());
                    value = dataSnapshot.child(uid2).child(property.getUniquepropertyid()).child("rvalue").getValue().toString();
                    val = Integer.parseInt(value);
                    useruid=dataSnapshot.child(uid2).child(property.getUniquepropertyid()).child("uid").getValue().toString();
                    System.out.println(val);

                    ruid = dataSnapshot.child(uid2).child(property.getUniquepropertyid()).child("ruid").getValue().toString();
                    if(ruid.equals("X")){
                        flag = false;
                    }else{
                        flag = true;
                    }



                    if(val == 0){

                        request.setText("Send rent request");
                    }
                    else if(val == 1 && ruid.equals(uid)){

                        request.setText("Rent request sent");

                    }else if(val == 1 && uid2.equals(uid)){
                        findViewById(R.id.accept).setVisibility(View.VISIBLE);
                        findViewById(R.id.deny).setVisibility(View.VISIBLE);
                        findViewById(R.id.acde).setVisibility(View.VISIBLE);
                    }
                    else if(val == 1 && !ruid.equals(uid)){
                        request.setText("Reserve Property");
                    }
                    else if(val == 2 && ruid.equals(uid2)){
                        request.setText("Rented Property");
                    }
                    else if(flag){

                        request.setText("Reserve Property");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tw4.setText(property.getTitle());
        tw5.setText("Price: " + property.getPrice());
        tw6.setText("Number of rooms: " +property.getRooms());
        tw9.setText("Size: " + property.getarea() + "");
        tw8.setText("Description: " + property.getDescription());
        tw7.setText("Address: " + property.getAddress());

        //image

        try{
            storageRef.child("images").child(property.getUniquepropertyid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    custom(uri);
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
        }
        else if(v.getId() == R.id.request){



            if(val == 0){
                System.out.println("UID 3 APPERIANCE"+ uid2);
                propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("rvalue").setValue("1");
                propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("ruid").setValue(uid);
                notification(uid2,"Rent Request Received","You got a rent request for your property.","RENT");
                request.setText("Request Sent.");

            }
            else if(val == 1 && flag){
                if(ruid.equals(uid)){
                    Toast.makeText(ViewProperty.this,
                            "You already sent rent request",
                            Toast.LENGTH_SHORT).show();
                }else{


                    if(!q){
                        queue++;
                        request.setText("Added to the request queue. " + queue + ". user on the queue");
                        propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("queue").setValue(queue);
                        q = true;
                    }else{
                        Toast.makeText(ViewProperty.this,
                                "You are already in the queue",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }
            else if(val == 2 && !ruid.equals(uid)){

                if(!q){
                    queue++;
                    request.setText("Added to the request queue. " + queue + ". user on the queue");
                    propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("queue").setValue(queue);
                    q = true;
                }else{
                    Toast.makeText(ViewProperty.this,
                            "You are already in the queue",
                            Toast.LENGTH_SHORT).show();
                }


            }else if(val == 2 && ruid.equals(uid)){

                Toast.makeText(ViewProperty.this,
                        "You already rented this property",
                        Toast.LENGTH_SHORT).show();

            }
            else{

                if(!q){
                    queue++;
                    request.setText("Added to the request queue. " + queue + ". user on the queue");
                    propertydatabase.child("propertylist").child(uid2).child(property.getUniquepropertyid()).child("queue").setValue(queue);
                    q = true;
                }else{
                    Toast.makeText(ViewProperty.this,
                            "You are already in the queue",
                            Toast.LENGTH_SHORT).show();
                }

            }


        }else if(v.getId() == R.id.smess){
            Intent intent = new Intent(getApplicationContext(), Messaging.class);
            intent.putExtra("uidx", uid2);
            System.out.println("UID 4 APPERIANCE"+ uid2);
            solver = true;
            startActivity(intent);

        }
        else if(v.getId() == R.id.accept){
            propertydatabase.child("propertylist").child(uid).child(property.getUniquepropertyid()).child("rvalue").setValue("2");
            findViewById(R.id.accept).setVisibility(View.GONE);
            findViewById(R.id.deny).setVisibility(View.GONE);
            findViewById(R.id.acde).setVisibility(View.GONE);
            Toast.makeText(ViewProperty.this,
                    "Request accepted.",
                    Toast.LENGTH_SHORT).show();
            notification(ruid,"Congrats!","Your rent request is accepted.","AD");

        }else if(v.getId() == R.id.deny){
            propertydatabase.child("propertylist").child(uid).child(property.getUniquepropertyid()).child("rvalue").setValue("0");
            findViewById(R.id.accept).setVisibility(View.GONE);
            findViewById(R.id.deny).setVisibility(View.GONE);
            findViewById(R.id.acde).setVisibility(View.GONE);
            Toast.makeText(ViewProperty.this,
                    "Request denied.",
                    Toast.LENGTH_SHORT).show();
            notification(ruid,"We're sorry!","Your rent request is denied.","AD");
        }else if(v.getId() == R.id.show_profile){
            Intent i = new Intent(ViewProperty.this,Show_profile.class);
            i.putExtra("uid", useruid);
            startActivity(i);
        }
    }
    public void custom(Uri uri){
        System.out.println(uri.toString());
        Glide.with(this).load(uri).into(iw2);
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewProperty.this,Edit_profile.class);
        startActivity(i);
    }

    public void notification(String uid,String NOTIFICATION_TITLE, String NOTIFICATION_MESSAGE, String type){


        String TOPIC = "/topics/" + uid ; //topic has to match what the receiver subscribed to


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


}
