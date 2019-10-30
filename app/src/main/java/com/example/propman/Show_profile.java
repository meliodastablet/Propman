package com.example.propman;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Show_profile extends LoginActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference userref;
    private TextView name;
    private TextView surname;
    private TextView phone;
    private TextView mail;
    private TextView address;
    private TextView bdate;
    private Button signout;
    private Button addproperty;
    private Button showproperties;
    private Button showusers;
    private String uid4sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userref = mDatabase.child("userlist");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = getIntent();
                String uid2 = intent.getExtras().getString("uid");
                uid4sp = uid2;

                if (dataSnapshot.hasChild(uid2)) {
                    if ((dataSnapshot.child(uid2).child("name")).getValue(String.class)!="") {
                        name.setText(dataSnapshot.child(uid2).child("name").getValue(String.class));
                        surname.setText(dataSnapshot.child(uid2).child("surname").getValue(String.class));
                        phone.setText(dataSnapshot.child(uid2).child("phone").getValue(String.class));
                        address.setText(dataSnapshot.child(uid2).child("address").getValue(String.class));
                        bdate.setText(dataSnapshot.child(uid2).child("bdate").getValue(String.class));
                        bdate.setText(dataSnapshot.child(uid2).child("bdate").getValue(String.class));
                        mail.setText(dataSnapshot.child(uid2).child("mail").getValue(String.class));
                    } else {
                        Toast.makeText(Show_profile.this, "Profile information is not edited yet!",
                                Toast.LENGTH_SHORT).show();
                        mail.setText(dataSnapshot.child(uid2).child("mail").getValue(String.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        bdate =  (TextView) findViewById(R.id.bdate);
        name=(TextView)findViewById(R.id.name);
        surname=(TextView)findViewById(R.id.surname);
        phone=(TextView)findViewById(R.id.phone);
        mail=(TextView)findViewById(R.id.mail);
        address=(TextView) findViewById(R.id.address);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.sendmessage).setOnClickListener(this);
        findViewById(R.id.showprop2).setOnClickListener(this);
       // findViewById(R.id.showproperties).setOnClickListener(this);
        //findViewById(R.id.showusers).setOnClickListener(this);

    }
    private void signout() {
        mAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signout){
            signout();
        }
        else if(v.getId() == R.id.sendmessage){
            Intent intent = new Intent(getApplicationContext(), Messaging.class);
            startActivity(intent);         }

        else if(v.getId() == R.id.showprop2){
            Intent i = new Intent(Show_profile.this, DisplayProperties.class);
            i.putExtra("uid", uid4sp);
            startActivity(i);
        }

       /* else if(v.getId() == R.id.showusers){
            Intent intent = new Intent(getApplicationContext(), Users.class);
            startActivity(intent);
        }*/

    }
}
