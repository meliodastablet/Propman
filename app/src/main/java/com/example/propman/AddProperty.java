
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddProperty extends AppCompatActivity implements View.OnClickListener {
    boolean flag = false;
    private DatabaseReference propertydatabase;
    private DatabaseReference uniqueproperty;
    private DatabaseReference requestq;
    private DatabaseReference tennanth;
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
    private ArrayList<Uri> filePath=new ArrayList<>();
    private String uid;
    private CheckBox pool_cb;
    private CheckBox garage_cb;
    private CheckBox garden_cb;
    private CheckBox security_cb;
    private EditText inputdues;



    private int index;
    private int i=0;
    private String filepath_count;
    int x;
    Context context;
    Property viewproperty;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private final int PICK_IMAGE_REQUEST = 71;
    String coordinate;

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
        garage_cb = (CheckBox) findViewById(R.id.garage_cb);
        garden_cb = (CheckBox) findViewById(R.id.garden_cb);
        security_cb = (CheckBox) findViewById(R.id.security_cb);
        pool_cb = (CheckBox) findViewById(R.id.pool_cb);
        inputdues = (EditText) findViewById(R.id.dues);

        Intent intent = getIntent();
        coordinate = intent.getExtras().getString("coordinate");
        findViewById(R.id.addproperty).setOnClickListener(this);
        findViewById(R.id.addimage).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addproperty) {
            if(flag){
                try {
                    returnproperty();
                    Intent i = new Intent(this, DisplayProperties.class);
                    i.putExtra("uid",uid);
                    startActivity(i);
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath.add(data.getData()) ;

        }
    }
    private void uploadImage(String uniq) {

        if(!filePath.isEmpty())
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            for(i=0;i< filePath.size();i++) {
                StorageReference ref = storageReference.child("images/"+ uniq+"/"+i);
                ref.putFile(filePath.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                viewproperty.setUid(user.getUid());
                                progressDialog.dismiss();
                                Toast.makeText(AddProperty.this, "Uploaded", Toast.LENGTH_SHORT).show();


                                requestq = propertydatabase.child("requestq");
                                requestq.child(viewproperty.getUid()).child(viewproperty.getUniquepropertyid()).setValue("X");
                                tennanth = propertydatabase.child("tennanthistory");
                                tennanth.child(viewproperty.getUid()).child(viewproperty.getUniquepropertyid()).setValue("X");



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddProperty.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }

                        });
            }



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
        String dues=inputdues.getText().toString();
        String garage;
        String garden;
        String security;
        String pool;

        if (garage_cb.isChecked()){
            garage="1";
        }else{
            garage="0";
        }
        if (garden_cb.isChecked()){
            garden="1";
        }else{
            garden="0";
        }
        if (pool_cb.isChecked()){
            pool="1";
        }else{
            pool="0";
        }
        if (security_cb.isChecked()){
            security="1";
        }else{
            security="0";
        }
        uniqueproperty = propertydatabase.child("propertylist").child(user.getUid()).push();
        String uniquepropertyid=uniqueproperty.getKey();
        uniqueproperty.child("title").setValue(title);
        uniqueproperty.child("price").setValue(price);
        uniqueproperty.child("rooms").setValue(rooms);
        uniqueproperty.child("address").setValue(address);
        uniqueproperty.child("description").setValue(description);
        uniqueproperty.child("area").setValue(area);
        uniqueproperty.child("uid").setValue(user.getUid());
        uniqueproperty.child("rvalue").setValue("0");
        uniqueproperty.child("ruid").setValue("X");
        uniqueproperty.child("filepath_count").setValue(String.valueOf(filePath.size()));
        for (i=0;i<filePath.size();i++){
            uniqueproperty.child("imagefilepath"+i).setValue(filePath.get(i).toString()+i);
        }
        uniqueproperty.child("uniquepropertyid").setValue(uniquepropertyid);
        uniqueproperty.child("coordinate").setValue(coordinate);
        uniqueproperty.child("garage").setValue(garage);
        uniqueproperty.child("garden").setValue(garden);
        uniqueproperty.child("security").setValue(security);
        uniqueproperty.child("pool").setValue(pool);
        uniqueproperty.child("dues").setValue(dues);
        Date time = Calendar.getInstance().getTime();
        uniqueproperty.child("time").setValue(time.toString());
        uploadImage(uniquepropertyid);
        viewproperty=new Property(title,price,rooms,area,description,address,user.getUid(),filePath.get(0).toString(),uniquepropertyid, coordinate,garage,garden,security,pool,dues,filepath_count,time.toString());
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
        inputdues.setError(null);



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


