package  com.example.propman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button verifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        findViewById(R.id.createAccount).setOnClickListener(this);
        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.signOut).setOnClickListener(this);
        verifyEmail=findViewById(R.id.verifyEmail);
        findViewById(R.id.verifyEmail).setOnClickListener(this);
        findViewById(R.id.forgetPassword).setOnClickListener(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }



    private void createAccount(String email, final String password) {

        final String email2 = email;
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Firebase reference = new Firebase("https://propman-de374.firebaseio.com/userlist");
                            reference.child(user.getUid()).child("password").setValue(password);
                            reference.child(user.getUid()).child("name").setValue("");
                            reference.child(user.getUid()).child("surname").setValue("");
                            reference.child(user.getUid()).child("phone").setValue("");
                            reference.child(user.getUid()).child("address").setValue("");
                            reference.child(user.getUid()).child("bdate").setValue("");
                            reference.child(user.getUid()).child("uid").setValue(user.getUid());

                            Toast.makeText(LoginActivity.this, "Account created, you can now sign in.",
                                Toast.LENGTH_SHORT).show();

                    } else

                    {

                        Toast.makeText(LoginActivity.this, "Account creation failed.",
                                Toast.LENGTH_SHORT).show();

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
        final String email2 = email;
        final String pass2 = password;
        if (!validateForm()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDetails.username = user.getUid();
                            UserDetails.password = pass2;

                            Toast.makeText(LoginActivity.this,
                                    "Successfully signed in",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Edit_profile.class);
                            startActivity(intent);

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



                        } else {


                            Toast.makeText(LoginActivity.this, "Authentication failed, wrong email or password.",
                                    Toast.LENGTH_SHORT).show();

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
        findViewById(R.id.email).setVisibility(View.VISIBLE);
        findViewById(R.id.password).setVisibility(View.VISIBLE);


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

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 7;
    }



}
