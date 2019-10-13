package com.example.propman;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.security.spec.ECField;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.createAccount).setOnClickListener(this);
        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.signOut).setOnClickListener(this);
        findViewById(R.id.verifyEmail).setOnClickListener(this);
        findViewById(R.id.forgetPassword).setOnClickListener(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            //user is signed in

        }else{
            //user is not signed in

        }
    }

    private void createAccount(String email, String password) {


        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Account created, you can now sign in",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {

                            Toast.makeText(LoginActivity.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });


    }

    private boolean validateForm(){

        boolean valid = true;
        email.setError(null);
        password.setError(null);

        String email2 = email.getText().toString();
        if (TextUtils.isEmpty(email2)) {
            email.setError("This field is required.");
            valid = false;
        } else if(!isEmailValid(email2)){
            email.setError("This email address is invalid.");
            valid = false;
        }
        else {
            email.setError(null);
        }

        String password2 = password.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            password.setError("This field is required.");
            valid = false;
        } else if (!isPasswordValid(password2)) {
            password.setError("This password is too short");
            valid = false;
        }
        else {
            password.setError(null);
        }

        return valid;
    }


    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,
                                    "Successfully signed in",
                                    Toast.LENGTH_SHORT).show();
                            findViewById(R.id.signIn).setVisibility(View.GONE);
                            findViewById(R.id.signOut).setVisibility(View.VISIBLE);
                            findViewById(R.id.forgetPassword).setVisibility(View.GONE);
                            final FirebaseUser user2 = mAuth.getCurrentUser();
                            if(user2.isEmailVerified()) {
                                findViewById(R.id.verifyEmail).setVisibility(View.GONE);
                            }
                            else{
                                findViewById(R.id.verifyEmail).setVisibility(View.VISIBLE);
                            }
                            findViewById(R.id.createAccount).setVisibility(View.GONE);
                            findViewById(R.id.email).setVisibility(View.GONE);
                            findViewById(R.id.password).setVisibility(View.GONE);
                            findViewById(R.id.textView).setVisibility(View.VISIBLE);
                            updateUI(user);
                        } else {


                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                        /*if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"Auth failed",Toast.LENGTH_LONG).show();
                        }*/


                    }
                });

    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(LoginActivity.this,
                "Successfully signed out.",
                Toast.LENGTH_SHORT).show();
        findViewById(R.id.signIn).setVisibility(View.VISIBLE);
        findViewById(R.id.signOut).setVisibility(View.GONE);
        findViewById(R.id.forgetPassword).setVisibility(View.VISIBLE);
        findViewById(R.id.verifyEmail).setVisibility(View.GONE);
        findViewById(R.id.createAccount).setVisibility(View.VISIBLE);
        findViewById(R.id.textView).setVisibility(View.GONE);
        findViewById(R.id.email).setVisibility(View.VISIBLE);
        findViewById(R.id.password).setVisibility(View.VISIBLE);
        updateUI(null);

    }

    private void sendEmailVerification() {


        findViewById(R.id.verifyEmail).setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        if(user.isEmailVerified()){
            Toast.makeText(LoginActivity.this,
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
                                Toast.makeText(LoginActivity.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(LoginActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }


    }


    private void resetPassword(String email){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(email == "") {
            Toast.makeText(LoginActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            this.email.setError("This field is required.");
        }
        else {

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(LoginActivity.this, "Reset password email sent",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Unknown error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.createAccount){

            String email2 = email.getText().toString();
            String pass2 = password.getText().toString();
            createAccount(email2,pass2);
        }
        else if(v.getId() == R.id.signIn){

            String email2 = email.getText().toString();
            String pass2 = password.getText().toString();
            signIn(email2,pass2);
        }
        else if(v.getId() == R.id.signOut){
            signOut();
        }
        else if(v.getId() == R.id.verifyEmail){
            sendEmailVerification();
        }
        else if(v.getId() == R.id.forgetPassword){

            try {
                String email2 = email.getText().toString();
                resetPassword(email2);
            }catch (Exception e){
             String email2 = "";
                resetPassword(email2);
            }


        }



    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 7;
    }

}
