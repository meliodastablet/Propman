package com.example.propman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class Payment_History extends AppCompatActivity  {
    TextView tv;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = mAuth.getCurrentUser();
    final String uid = user.getUid();
    private DatabaseReference propref;
    int total=0;
    Button payb;
    private ArrayList<String> dues=new ArrayList<>();
    private ArrayList<String> rent=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__history);
        tv=findViewById(R.id.payment_hist);
        propref = FirebaseDatabase.getInstance().getReference();
        propref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot d=  dataSnapshot.child("paymenthistory").child(uid);

                Iterable<DataSnapshot> userdetails=d.getChildren();

                for (DataSnapshot snap :userdetails)
                {
                    System.out.println("1"+snap.getKey());
                    String p=snap.getValue().toString();
                    DataSnapshot rentsnap=  dataSnapshot.child("paymenthistory").child(uid).child("rent");
                    Iterable<DataSnapshot> rentdetails=rentsnap.getChildren();

                    for (DataSnapshot snap2 :rentdetails) {
                         rent.add(snap2.getKey());
                        System.out.println("2"+snap2.getKey());
                    }

                    DataSnapshot duessnap=  dataSnapshot.child("paymenthistory").child(uid).child("dues");
                    Iterable<DataSnapshot> duesdetails=duessnap.getChildren();

                    for (DataSnapshot snap3 :duesdetails) {

                         dues.add(snap3.getKey());
                        System.out.println("3"+snap3.getKey());
                       // total+=Integer.valueOf(snap.getKey());
                    }

                  //  tv.append("Total:"+total);
                } for (int z=0;z<dues.size();z++) {
                    System.out.println("size"+dues.size());
                    tv.append( "Rental Price: \n" + rent.get(z) + "\nApartment Dues: \n" + dues.get(z) + "\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
