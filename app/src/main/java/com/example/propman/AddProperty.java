package com.example.senior1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddProperty extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7;
    TextView tw;
    int x = 1;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        setTitle("Add Property");
    }

    public void onClick(View view) {

        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        e4 = findViewById(R.id.editText4);
        e5 = findViewById(R.id.editText5);
        e6 = findViewById(R.id.editText6);
        e7 = findViewById(R.id.editText7);
        tw = findViewById(R.id.textView3);

        if(view.getId() == R.id.button){
            String s = e4.getText().toString();
            int e4 = Integer.parseInt(s);

            System.out.println(e1.getText().toString()+e2.getText().toString()+e3.getText().toString()+e4 + e5.getText().toString()+e6.getText().toString());
            db = new DatabaseHelper(this);
            PropertyDB.insertProperty(db,x,e1.getText().toString(),e2.getText().toString(),e3.getText().toString(),e4,e5.getText().toString(),e6.getText().toString(),e7.getText().toString());
            HomeActivity.n= HomeActivity.n+1;
            tw.setText("Successful");

        }
        if(view.getId() == R.id.button2){
            Intent intent = new Intent(AddProperty.this, HomeActivity.class);
            startActivity(intent);
        }





    }


}
