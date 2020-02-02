package com.example.propman;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DisplayProperties extends AppCompatActivity {
    static boolean active = false;
    RecyclerView recylerContact;
    ArrayList<Property> data=new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String title;
    private String price;
    private String rooms;
    private String area;
    private String description;
    private String address;
    private String uid;
    private String uniqueproperty;
    private ArrayList<Uri> filePath=new ArrayList<>();
    private String filepath_count;
    private String garage;
    private String pool;
    private String garden;
    private String security;
    private Property p;
    public ArrayList<Property> propertyuid=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Show All Properties");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        uid = intent.getExtras().getString("uid");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //This will loop through all items. Add variables to arrays or lists as required
                DataSnapshot userpropertys = dataSnapshot.child("propertylist").child(uid);
                Iterable<DataSnapshot> propertydetails=userpropertys.getChildren();
                for (DataSnapshot snap :propertydetails)
                {
                    Property p=snap.getValue(Property.class);
                    p.setUid(uid);
                    propertyuid.add(p);

                }
                custom(propertyuid);

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

    public void custom(ArrayList<Property> a){
        recylerContact = findViewById(R.id.recylerContact);


        LinearLayoutManager layoutManaget = new LinearLayoutManager(this);
        recylerContact.setLayoutManager(layoutManaget);
        if(a.isEmpty() && active){
            Toast.makeText(DisplayProperties.this, "This user has no properties.",
                    Toast.LENGTH_SHORT).show();
        }
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, a);
        recylerContact.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Edit_profile.class);
        startActivity(i);
    }
}
