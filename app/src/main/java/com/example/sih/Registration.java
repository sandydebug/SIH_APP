package com.example.sih;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class Registration extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{

    EditText editText1, editText2, editText3,editText4,editText5,editText6,editText7,editText8;
    String date, name,email,dob,password,confirmpassword,location,pincode,phone;
    TextView textView;
    Button button;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri uri;
    StorageReference storageReference;
    DatabaseReference databaseReference,check;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode== RESULT_OK && data.getData()!=null){
            uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
                imageView.setTag("Set");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        intialize();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        imageView =findViewById(R.id.imageView);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getInstance().getReference("PROFILES");
        check = FirebaseDatabase.getInstance().getReference("CHECK");
        check.setValue("1");

        storageReference=firebaseStorage.getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String username=editText2.getText().toString().trim();
                    String password =editText3.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                               sendVerification();
                            }
                            else{
                                Toast.makeText(Registration.this,"Registration Failed!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        editText5.setText(date);
    }

    private void intialize() {
        editText1 = (EditText) findViewById(R.id.fullName);
        editText2 = (EditText) findViewById(R.id.userEmailId);
        editText3 = (EditText) findViewById(R.id.password);
        editText4 = findViewById(R.id.confirmPassword);
        editText5 = findViewById(R.id.dobPicker);
        editText6 = findViewById(R.id.location);
        editText7 = findViewById(R.id.mobileNumber);
        editText8 = findViewById(R.id.pincode);
        textView = (TextView) findViewById(R.id.already_user);
        button = (Button) findViewById(R.id.signUpBtn);
        progressDialog=new ProgressDialog(this);
        editText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new datePicker();
                dialogFragment.show(getSupportFragmentManager(),"date picker");
            }
        });


    }

    private boolean validate() {
        Boolean res=false;
        name = editText1.getText().toString();
        email = editText2.getText().toString();
        password = editText3.getText().toString();
        confirmpassword = editText4.getText().toString();
        dob =editText5.getText().toString();
        location = editText6.getText().toString();
        pincode = editText8.getText().toString();
        phone = editText7.getText().toString();


        progressDialog.setMessage("Hang on while we register your profile ");
        progressDialog.show();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()|| dob.isEmpty() || location.isEmpty()|| pincode.isEmpty() || phone.isEmpty() ) {
            Toast.makeText(Registration.this, "Enter all the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(!("Set".equals(imageView.getTag()))){
            Toast.makeText(Registration.this, "Add a image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText2.setError("Invalid EmailID");
            progressDialog.dismiss();
        }
        else if(pincode.length()!=6){
            editText8.setError("Invalid PINCODE");
            editText8.requestFocus();
            progressDialog.dismiss();
        }
        else if(phone.length()!=10){
            editText7.setError("Invalid Phone Number");
            editText7.requestFocus();
            progressDialog.dismiss();
        }
        else if(!password.equals(confirmpassword)){
            Toast.makeText(Registration.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
            editText4.setError("Password Don't Match");
            editText4.requestFocus();
            progressDialog.dismiss();
        }
        else{
            res=true;}

        return res;

    }

    private void sendVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        sendData();
                        Toast.makeText(Registration.this,"Verification Mail Sent !", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(Registration.this,Login.class));
                    }
                    else {
                        Toast.makeText(Registration.this,"Mail not sent !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendData(){
        databaseReference.child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new UserProfile(email,name,dob,location,pincode,phone));
        Toast.makeText(Registration.this,"Database Updated!!",Toast.LENGTH_SHORT).show();
        StorageReference imageref=storageReference.child("Images").child(firebaseAuth.getUid());
        UploadTask uploadTask=imageref.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Registration.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        firebaseAuth.signOut();
    }

}
