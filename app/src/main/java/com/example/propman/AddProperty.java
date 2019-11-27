package com.example.propman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddProperty extends AppCompatActivity implements View.OnClickListener {
    boolean flag = false;
    private DatabaseReference propertydatabase;
    private DatabaseReference uniqueproperty;
    FirebaseStorage storage;
    StorageReference storageReference;
    private EditText inputtitle;
    private EditText inputprice;
    private EditText inputrooms;
    private EditText inputarea;
    private EditText inputaddress;
    private EditText inputdescription;
    private Button addimage;
    private Button addproperty;
    private Uri filePath;
    private String uid;
    Context context;
    Property viewproperty;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproperty);
        mAuth = FirebaseAuth.getInstance();
        propertydatabase = FirebaseDatabase.getInstance().getReference();
        uid=user.getUid();
        context = this.context;
        final FirebaseUser user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        inputtitle = (EditText) findViewById(R.id.propertytitle);
        inputprice = (EditText) findViewById(R.id.price);
        inputrooms = (EditText) findViewById(R.id.rooms);
        inputaddress = (EditText) findViewById(R.id.address);
        inputdescription = (EditText) findViewById(R.id.description);
        inputarea = (EditText) findViewById(R.id.area);

        findViewById(R.id.addproperty).setOnClickListener(this);
        findViewById(R.id.addimage).setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.addproperty) {
            if(flag){
                try {
                    returnproperty();
                }catch (Exception e){
                    Toast.makeText(AddProperty.this, "Choose an image to add first.",
                            Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(AddProperty.this, "Choose an image to add first.",
                        Toast.LENGTH_SHORT).show();
            }


                               }


        else if (v.getId() == R.id.addimage) {
            chooseImage();
            flag = true;

        }


    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

        }
    }
    private void uploadImage(String uniq) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ uniq);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            viewproperty.setUid(user.getUid());
                            progressDialog.dismiss();
                            Toast.makeText(AddProperty.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ParcelableProperty parcelableProperty = new ParcelableProperty(viewproperty);
                            Intent mIntent = new Intent(AddProperty.this, ViewProperty.class);

                            mIntent.putExtra("property", parcelableProperty);
                            startActivity(mIntent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProperty.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }

                    });

        }
    }
    private Property returnproperty(){
        if(!validateForm()){
            return null;
        }
        String title=inputtitle.getText().toString();
        String price=inputprice.getText().toString();
        String rooms=inputrooms.getText().toString();
        String address=inputaddress.getText().toString();
        String description=inputdescription.getText().toString();
        String area=inputarea.getText().toString();
        uniqueproperty = propertydatabase.child("propertylist").child(user.getUid()).push();
        String uniquepropertyid=uniqueproperty.getKey();
        uniqueproperty.child("title").setValue(title);
        uniqueproperty.child("price").setValue(price);
        uniqueproperty.child("rooms").setValue(rooms);
        uniqueproperty.child("address").setValue(address);
        uniqueproperty.child("description").setValue(description);
        uniqueproperty.child("area").setValue(area);
        uniqueproperty.child("uid").setValue(user.getUid());
        uniqueproperty.child("rvalue").setValue(0);
        uniqueproperty.child("ruid").setValue("X");
        uniqueproperty.child("queue").setValue(0);
        uniqueproperty.child("imagefilepath").setValue(filePath.toString());
        uniqueproperty.child("uniquepropertyid").setValue(uniquepropertyid);

        uploadImage(uniquepropertyid);
        viewproperty=new Property(title,price,rooms,area,description,address,user.getUid(),filePath.toString(),uniquepropertyid);
        return viewproperty;
    }

    private boolean validateForm(){

        boolean valid = true;
        inputtitle.setError(null);
        inputprice.setError(null);
        inputrooms.setError(null);
        inputaddress.setError(null);
        inputdescription.setError(null);
        inputarea.setError(null);




        if(TextUtils.isEmpty(inputtitle.getText().toString())){
            inputtitle.setError("This field is required.");
            valid = false;

        }else if(TextUtils.isEmpty(inputprice.getText().toString())){
            inputprice.setError("This field is required.");
            valid = false;
        }
        else if (TextUtils.isEmpty(inputrooms.getText().toString())) {
            inputrooms.setError("This field is required.");
            valid = false;
        } else if(TextUtils.isEmpty(inputaddress.getText().toString())){
            inputaddress.setError("This field is required.");
            valid = false;
        }
        else if(TextUtils.isEmpty(inputdescription.getText().toString())){
            inputdescription.setError("This field is required.");
            valid = false;
        }
        else if(TextUtils.isEmpty(inputarea.getText().toString())){
            inputarea.setError("This field is required.");
            valid = false;
        }


        return valid;
    }
}


