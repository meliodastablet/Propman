package com.example.propman;


import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> data;
    String uidx, propid,dues,rent;
    DatabaseReference propertydatabase;
    String uname;
    String date;
    String[] date_portions;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAERSaSd4:APA91bFoMzFZ8RnrhePzG8a36Cujw9T-ZvH16vzsyGuzHurYIKOOwU8dV6kGaF0ghlFbJ5zO5ljar6lwI5NbdWmk06ek8p3jEKrcZJ8lKdh8t6buk_UcNJew5Zj6R2FYpzp7zFeWda6g";
    final private String contentType = "application/json";

    public RecyclerAdapter(Context context, List<String> data, String uidx, String propid) {
        this.context = context;
        this.data =(ArrayList<String>) data;
        this.uidx = uidx;
        this.propid = propid;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerq, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //BIND DATA
        final String i = data.get(position);
        System.out.println(" datagetpos" + i);
         holder.tw.setText("Rent Candidate");
         holder.tw.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Show_profile.class);
            intent.putExtra("uid",i);
            context.startActivity(intent);
        }
    });
        holder.ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertydatabase = FirebaseDatabase.getInstance().getReference();
                propertydatabase.child("propertylist").child(uidx).child(propid).child("rvalue").setValue("2");
                propertydatabase.child("propertylist").child(uidx).child(propid).child("ruid").setValue(i);

                propertydatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        uname = dataSnapshot.child("userlist").child(i).child("name").getValue().toString();
                        System.out.println("1"+uname);
                        date  = dataSnapshot.child("propertylist").child(uidx).child(propid).child("time").getValue().toString();
                        propertydatabase.child("requestq").child(uidx).child(propid).child(i).removeValue();
                        propertydatabase.child("tennanthistory").child(uidx).child(propid).child(i).child("name").setValue(uname);
                        propertydatabase.child("tennanthistory").child(uidx).child(propid).child(i).child("date").setValue(date);
                        dues= dataSnapshot.child("propertylist").child(uidx).child(propid).child("dues").getValue().toString();
                        rent= dataSnapshot.child("propertylist").child(uidx).child(propid).child("price").getValue().toString();
                        propertydatabase.child("paymenthistory").child(i).child("rent").child("-"+rent).setValue("X");
                        propertydatabase.child("paymenthistory").child(i).child("dues").child("-"+dues).setValue("X");
                        propertydatabase.child("paymenthistory").child(uidx).child("rent").child(rent).setValue("X");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                notification(i,"Rent Request Accepted","Click here to continue...","R-accepted");
                Toast.makeText(context,
                        "Request accepted.",
                        Toast.LENGTH_SHORT).show();
                for(int kk=0;kk<getItemCount();kk++){

                  String kk2 = data.get(kk);
                    System.out.println("kk2" + kk2);
                  if(!kk2.equals(i)) {
                      System.out.println("kk22" + kk2);
                      notification(kk2, "The property you requested has been rented out to another user.", "You can enter reserve queue if you want", "REQUEST");
                  }
                  }
                propertydatabase.child("requestq").child(uidx).child(propid).removeValue();
                propertydatabase.child("requestq").child(uidx).child(propid).setValue("X");

                Intent i = new Intent(context, Edit_profile.class);
                context.startActivity(i);


            }
        });
        holder.dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertydatabase = FirebaseDatabase.getInstance().getReference();
                System.out.println(uidx + " " + propid + " " + i);
                propertydatabase.child("requestq").child(uidx).child(propid).child(i).removeValue();
                notification(i,"Rent Request Result","Rent request denied","R-denied");
                Toast.makeText(context,
                        "Request denied.",
                        Toast.LENGTH_SHORT).show();
                Intent in = new Intent(context,Edit_profile.class);
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        Button ac,dn;
        TextView tw;


        MyViewHolder(View viewItem){
            super(viewItem);
            tw = viewItem.findViewById(R.id.rname);
            ac = viewItem.findViewById(R.id.acbut);
            dn = viewItem.findViewById(R.id.denbut);
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

                        Toast.makeText(context.getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();

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
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
