package com.example.propman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Property_search extends AppCompatActivity  {
    RecyclerView recylerProp;
    private Property p;
    public ArrayList<Property> propertyuid=new ArrayList<>();
    public ArrayList<Property> properties=new ArrayList<>();
    public ArrayList<String> useruid=new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference propertyref;
    private String uid;
    Spinner search_options;
    EditText input_search;
    EditText min;
    EditText max;
    Button search_prop;
    LinearLayout price_layout;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_search);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        propertyref = mDatabase.child("propertylist");
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        input_search=(EditText) findViewById(R.id.input_search);
        min=(EditText) findViewById(R.id.min);
        max=(EditText) findViewById(R.id.max);
        search_options=(Spinner) findViewById(R.id.search_options);
        price_layout=(LinearLayout) findViewById(R.id.price_layout);
        search_prop=(Button) findViewById(R.id.search_prop);
        recylerProp = findViewById(R.id.recylerProp);
        List<String> categories = new ArrayList<String>();
        categories.add("Rooms");
        categories.add("Price");
        categories.add("Area");
        categories.add("Address");
        categories.add("Description");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_options.setAdapter(dataAdapter);
        final List<String> keys=new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //This will loop through all items. Add variables to arrays or lists as required
                DataSnapshot users = dataSnapshot.child("propertylist");
                Iterable<DataSnapshot> usershaveproperties=users.getChildren();

                for (DataSnapshot snap :usershaveproperties) {
                    String key = snap.getKey();
                    keys.add(key);
                }
                   while(i<keys.size()){
                        if (keys.get(i).equals(uid)==false){
                            useruid.add(keys.get(i));
                            DataSnapshot userpropertys = dataSnapshot.child("propertylist").child(keys.get(i));
                            Iterable<DataSnapshot> propertydetails = userpropertys.getChildren();
                            for (DataSnapshot userproperties_snap : propertydetails) {
                                final Property p = userproperties_snap.getValue(Property.class);
                                p.setUid(keys.get(i));
                                properties.add(p);
                                search_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                {
                                    public void onItemSelected(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
                                        int index = search_options.getSelectedItemPosition();
                                        if(index==1)
                                        {
                                            price_layout.setVisibility(View.VISIBLE);
                                            input_search.setFocusable(false);
                                            input_search.setClickable(false);
                                            search_prop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    propertyuid.clear();
                                                    if(max.getText().toString().equals("") || min.getText().toString().equals("")){
                                                        Toast.makeText(Property_search.this, "Please define the minimum and the maximum price values.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                   else if(Integer.parseInt(max.getText().toString())<=Integer.parseInt(min.getText().toString())){
                                                        Toast.makeText(Property_search.this, "Min price value can't be greater than or equal to max price value.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }else{
                                                    for (int x=0;x<properties.size();x++){
                                                        if(Integer.parseInt(properties.get(x).getPrice())<=Integer.parseInt(max.getText().toString())&&Integer.parseInt(properties.get(x).getPrice())>=Integer.parseInt(min.getText().toString())){
                                                        propertyuid.add(properties.get(x));
                                                    }

                                                    }
                                                      if(propertyuid.isEmpty()){
                                                            Toast.makeText(Property_search.this, "There are no results.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }}
                                                    custom(propertyuid);
                                                }
                                            });

                                        }else if(index==0)
                                        {
                                            price_layout.setVisibility(View.GONE);
                                            input_search.setFocusable(true);
                                            input_search.setClickable(true);
                                            search_prop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    propertyuid.clear();
                                                    for (int x=0;x<properties.size();x++){
                                                    if(properties.get(x).getRooms().equals(input_search.getText().toString())){
                                                        propertyuid.add(properties.get(x));
                                                    }
                                                   }   if(propertyuid.isEmpty()){
                                                    Toast.makeText(Property_search.this, "There are no results.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                    custom(propertyuid);
                                                }
                                            });

                                        }else if(index==2)
                                        {
                                            price_layout.setVisibility(View.GONE);
                                            input_search.setFocusable(true);
                                            input_search.setClickable(true);
                                            search_prop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    propertyuid.clear();
                                                    for (int x=0;x<properties.size();x++){
                                                    if(properties.get(x).getarea().equals(input_search.getText().toString())){
                                                        propertyuid.add(properties.get(x));

                                                    }
                                                    }  if(propertyuid.isEmpty()){
                                                    Toast.makeText(Property_search.this, "There are no results.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                    custom(propertyuid);
                                                }
                                            });

                                        }else if(index==3)
                                        {
                                            price_layout.setVisibility(View.GONE);
                                            input_search.setFocusable(true);
                                            input_search.setClickable(true);
                                            search_prop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    propertyuid.clear();
                                                    for (int x=0;x<properties.size();x++){
                                                    if(properties.get(x).getAddress().contains(input_search.getText().toString())){
                                                        propertyuid.add(properties.get(x));

                                                    }
                                                  }   if(propertyuid.isEmpty()){
                                                    Toast.makeText(Property_search.this, "There are no results.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                    custom(propertyuid);
                                                }
                                            });

                                        }else if(index==4)
                                        {
                                            price_layout.setVisibility(View.GONE);
                                            input_search.setFocusable(true);
                                            input_search.setClickable(true);
                                            search_prop.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    propertyuid.clear();
                                                    for (int x=0;x<properties.size();x++){
                                                    if(properties.get(x).getDescription().contains(input_search.getText().toString())){
                                                        propertyuid.add(properties.get(x));

                                                    }
                                                   }   if(propertyuid.isEmpty()){
                                                    Toast.makeText(Property_search.this, "There are no results.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                    custom(propertyuid);
                                                }
                                            });

                                        }
                                  propertyuid.clear();  }
                                    public void onNothingSelected(AdapterView<?>arg0) {}
                                });
                            }
                        }
                        i++;
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void custom(ArrayList<Property> a){
        LinearLayoutManager layoutManaget = new LinearLayoutManager(this);
        recylerProp.setLayoutManager(layoutManaget);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, a);
        recylerProp.setAdapter(adapter);

    }
}
