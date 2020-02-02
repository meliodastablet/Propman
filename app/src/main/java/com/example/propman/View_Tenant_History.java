package com.example.propman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Tenant_History extends AppCompatActivity {
    private DatabaseReference propref;
    TextView tenant_hist_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__tenant__history);
        tenant_hist_line=findViewById(R.id.tenant_hist_line);
        Intent intent = getIntent();
        final String propid =intent.getExtras().getString("prop");
        final String id = intent.getExtras().getString("id");

        propref = FirebaseDatabase.getInstance().getReference();
        propref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   DataSnapshot d=  dataSnapshot.child("tennanthistory").child(id).child(propid);

                Iterable<DataSnapshot> userdetails=d.getChildren();
                int i = 1;
                for (DataSnapshot snap :userdetails)
                {

                    String p=snap.getValue().toString();
                    String date = snap.child("date").getValue().toString();
                    String name = snap.child("name").getValue().toString();
                    tenant_hist_line.append(i + ".Tennant Name: \n" + name + "\nRent Date: \n" + date + "\n");
                    i++;


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}