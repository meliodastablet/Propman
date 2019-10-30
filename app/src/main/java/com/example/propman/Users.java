package com.example.propman;



import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

public class Users extends AppCompatActivity {
    ListView uList;
    TextView noUser;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> a2 = new ArrayList<>();
    int totalUsers = 0;
    private DatabaseReference mDatabase;
    private DatabaseReference userref;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        uList = findViewById(R.id.usersList);
        noUser = findViewById(R.id.noUsersText);
        mAuth = FirebaseAuth.getInstance();


        String url = "https://propman-de374.firebaseio.com/userlist.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        uList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.uid = a2.get(position);
                UserDetails.chatWith = a2.get(position);
                Intent i = new Intent(Users.this,Show_profile.class);
                i.putExtra("uid", UserDetails.uid);
                startActivity(i);


            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";


            while(i.hasNext()){
                key = i.next().toString();
                String name = obj.getJSONObject(key).getString("name");



                if(!key.equals(UserDetails.username)) {
                    if(!name.equals("")){
                        al.add(name);
                        a2.add(key);
                    }

                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUser.setVisibility(View.VISIBLE);
            uList.setVisibility(View.GONE);
        }
        else{
            noUser.setVisibility(View.GONE);
            uList.setVisibility(View.VISIBLE);
            uList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }


    }
}