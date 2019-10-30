package com.example.propman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Edit_profile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference userref;
    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText inputname;
    private EditText inputsurname;
    private EditText inputphone;
    private EditText inputemail;
    private EditText inputaddress;
    private TextView inputmDateDisplay;
    private Button mPickDate;
    private Button save;
    private Button signout;
    private Button addproperty;
    private Button showproperties;
    private Button showusers;
    static final int DATE_DIALOG_ID = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid=user.getUid();
        final String mail=user.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userref = mDatabase.child("userlist");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    inputname.setText( dataSnapshot.child(uid).child("name").getValue(String.class));
                    inputsurname.setText(dataSnapshot.child(uid).child("surname").getValue(String.class));
                    inputphone.setText(dataSnapshot.child(uid).child("phone").getValue(String.class));
                    inputaddress.setText(dataSnapshot.child(uid).child("address").getValue(String.class));
                    inputmDateDisplay.setText(dataSnapshot.child(uid).child("bdate").getValue(String.class));
                    inputemail.setText(mail);

                }else{
                    inputemail.setText(mail);
                  }
                // Check if user's email is verified
                boolean emailVerified = user.isEmailVerified();
                if(emailVerified){
                    findViewById(R.id.verifyEmail).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.verifyEmail).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        inputmDateDisplay =  (TextView) findViewById(R.id.showMyDate);
        mPickDate = (Button)findViewById(R.id.myDatePickerButton);
        inputname=(EditText)findViewById(R.id.name);
        inputsurname=(EditText)findViewById(R.id.surname);
        inputphone=(EditText)findViewById(R.id.phone);
        inputemail=(EditText)findViewById(R.id.email);
        inputaddress=(EditText) findViewById(R.id.address);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.verifyEmail).setOnClickListener(this);
        findViewById(R.id.addproperty).setOnClickListener(this);
        findViewById(R.id.showprop).setOnClickListener(this);
        findViewById(R.id.showusers).setOnClickListener(this);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });





        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();

    }
    private boolean validateForm(){

        boolean valid = true;
        inputemail.setError(null);
        inputphone.setError(null);
        inputname.setError(null);
        inputsurname.setError(null);

        String email2 = inputemail.getText().toString();
        if (TextUtils.isEmpty(email2)) {
            inputemail.setError("This field is required.");
            valid = false;
        } else if(!isEmailValid(email2)){
            inputemail.setError("This email address is invalid.");
            valid = false;
        }
        else {
            inputemail.setError(null);
        }
        String phone2 = inputphone.getText().toString();
        if(TextUtils.isEmpty(inputname.getText().toString())){
            inputname.setError("This field is required.");
            valid = false;

        }else if(TextUtils.isEmpty(inputsurname.getText().toString())){
            inputsurname.setError("This field is required.");
            valid = false;
        }
        else if (TextUtils.isEmpty(phone2)) {
            inputphone.setError("This field is required.");
            valid = false;
        } else if(!isValidPhoneNumber(phone2)){
            inputphone.setError("Phone must be 10 numbers.");
            valid = false;
        }
        else {
            inputphone.setError(null);
        }

        return valid;
    }
    private static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length()< 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private boolean isEmailValid(String email) {

        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();

        findViewById(R.id.verifyEmail).setEnabled(false);


        if(user.isEmailVerified()){
            Toast.makeText(Edit_profile.this,
                    "Account is already verified.",
                    Toast.LENGTH_SHORT).show();

        }
        else {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            findViewById(R.id.verifyEmail).setEnabled(true);

                            if (task.isSuccessful()) {
                                Toast.makeText(Edit_profile.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(Edit_profile.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }


    }
    private void signout() {
        mAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);


    }




    @Override
    public void onClick(View v) {

        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

         if(v.getId() == R.id.signout){
            signout();
        }
        else if(v.getId() == R.id.verifyEmail){
            sendEmailVerification();
        }
         else if(v.getId() == R.id.addproperty){
             Intent intent = new Intent(getApplicationContext(), AddProperty.class);
             startActivity(intent);         }
         else if(v.getId() == R.id.showprop){
             Intent intent = new Intent(Edit_profile.this, DisplayProperties.class);
             intent.putExtra("uid", uid);
             startActivity(intent);
         }
         else if(v.getId() == R.id.save){

             String name=inputname.getText().toString();
             String surname=inputsurname.getText().toString();
             String phone=inputphone.getText().toString();
             String address=inputaddress.getText().toString();
             String bdate=inputmDateDisplay.getText().toString();
             if (!validateForm()) {
                 return;
             }
             else {


                 mDatabase.child("userlist").child(user.getUid()).child("name").setValue(name);
                 mDatabase.child("userlist").child(user.getUid()).child("surname").setValue(surname);
                 mDatabase.child("userlist").child(user.getUid()).child("phone").setValue(phone);
                 mDatabase.child("userlist").child(user.getUid()).child("mail").setValue(user.getEmail());
                 mDatabase.child("userlist").child(user.getUid()).child("address").setValue(address);
                 mDatabase.child("userlist").child(user.getUid()).child("bdate").setValue(bdate);
                 mDatabase.child("userlist").child(user.getUid()).child("uid").setValue(user.getUid());

                 Toast.makeText(Edit_profile.this, "Changes are saved successfully.",
                         Toast.LENGTH_SHORT).show();
             }
         }
         else if(v.getId() == R.id.showusers){
             if(!validateForm()){
                 return;

             }else if(mDatabase.child("userlist").child(uid).child("name").equals("")){
                 Toast.makeText(Edit_profile.this, "Add your name and click save button before proceeding.",
                         Toast.LENGTH_SHORT).show();
             return;
             }

             else {
                 Intent intent = new Intent(getApplicationContext(), Users.class);
                 startActivity(intent);
             }
         }

    }

    private void updateDisplay() {
        this.inputmDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        signout();
        Toast.makeText(Edit_profile.this,
                "Signed out",
                Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Edit_profile.this,LoginActivity.class);
        startActivity(i);
    }

}
