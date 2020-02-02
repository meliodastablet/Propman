package com.example.propman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestQueue extends AppCompatActivity {
    RecyclerView recyclerView;
    static boolean active = false;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public ArrayList<String> userDetails=new ArrayList<>();

    private String propuid,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_queue);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        propuid = intent.getExtras().getString("propuid");
        uid = intent.getExtras().getString("uid");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //This will loop through all items. Add variables to arrays or lists as required
                DataSnapshot qusers = dataSnapshot.child("requestq").child(uid).child(propuid);

                Iterable<DataSnapshot> userdetails=qusers.getChildren();

                for (DataSnapshot snap :userdetails)
                {
                    String p=snap.getValue().toString();

                    userDetails.add(p);



                }
                custom(userDetails);




            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public void custom(ArrayList<String> a){
        recyclerView = findViewById(R.id.recylerque);


        LinearLayoutManager layoutManaget = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManaget);
        if(a.isEmpty() && active){
            Toast.makeText(RequestQueue.this, "There are no rent/reserve requests",
                    Toast.LENGTH_SHORT).show();
        }
        RecyclerAdapter adapter = new RecyclerAdapter(this, a, uid, propuid);
        recyclerView.setAdapter(adapter);


    }
}
