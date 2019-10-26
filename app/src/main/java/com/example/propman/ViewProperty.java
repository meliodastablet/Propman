package com.example.propman;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class ViewProperty extends LoginActivity {
    Uri filePath;
    TextView tw4,tw5,tw6,tw7,tw8,tw9;
    ImageView iw2;
    Button delete;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference propertydatabase;
    DatabaseReference propertyref;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Property property;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        setTitle("View");
        propertydatabase = FirebaseDatabase.getInstance().getReference();
        tw4 = findViewById(R.id.textView4);
        tw5 = findViewById(R.id.textView5);
        tw6 = findViewById(R.id.textView6);
        tw7 = findViewById(R.id.textView7);
        tw8 = findViewById(R.id.textView8);
        tw9 = findViewById(R.id.textView9);
        iw2 = findViewById(R.id.imageView2);
        findViewById(R.id.delete).setOnClickListener(this);


            uid=user.getUid();
            Intent intent = getIntent();
            ParcelableProperty parcelableProperty = (ParcelableProperty) intent
                    .getParcelableExtra("property");
            property = parcelableProperty.getProperty();




        tw4.setText(property.getTitle());
        tw5.setText("Price: " + property.getPrice());
        tw6.setText("Number of rooms: " +property.getRooms());
        tw7.setText("Size: " + property.getarea() + "");
        tw8.setText("Description: " + property.getDescription());
        tw9.setText("Address: " + property.getAddress());
        filePath=Uri.parse(property.getFilepath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        iw2.setImageBitmap(bitmap);


    }
    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.delete) {
            propertydatabase.child("propertylist").child(uid).child(property.getUniqueproperty()).removeValue();
            Intent intent = new Intent(getApplicationContext(), Addproperty.class);
            startActivity(intent);
        }
    }
}
