package com.example.propman;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Propertydatabase extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference propertyref;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference photoReference;
    ArrayList<Property> propertylist;
    Property property=new Property();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        setTitle("View");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        propertyref = mDatabase.child("propertylist");
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid=user.getUid();
        propertyref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String address = ds.child("address").getValue(String.class);
                    String area = ds.child("area").getValue(String.class);
                    String desc = ds.child("desc").getValue(String.class);
                    String price = ds.child("price").getValue(String.class);
                    String rooms = ds.child("rooms").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    String filePath = ds.child("imagefilepath").getValue(String.class);
                    String coordinate = ds.child("coordinate").getValue(String.class);
                    String uniquepropertyid = ds.child("uniquepropertyid").getValue(String.class);
                    String garage = ds.child("garage").getValue(String.class);
                    String garden = ds.child("garden").getValue(String.class);
                    String security = ds.child("security").getValue(String.class);
                    String pool = ds.child("pool").getValue(String.class);
                    String dues = ds.child("dues").getValue(String.class);
                    String filepath_count = ds.child("filepath_count").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    Property property=new Property(title,price,rooms,area,desc,address,uid,filePath,uniquepropertyid,coordinate,garage,garden,security,pool,dues,filepath_count,time);
                    ArrayList<Property> propertylist=new ArrayList<>();
                    propertylist.add(property);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}
}
