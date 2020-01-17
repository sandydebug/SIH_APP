package com.example.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.Models.ProductModel;
import com.example.sih.Models.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class CropPost extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener,AdapterView.OnItemSelectedListener  {

    EditText editText1, editText2, editText3,editText4,editText5,editText6,editText7,editText8;
    String date, name,price,prodate,maxquantity,about1,extra1,category;
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
    long count=0;
    Boolean res=true;


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
        setContentView(R.layout.activity_crop_post);

        intialize();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getInstance().getReference("PRODUCTS");
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            databaseReference.child("POSTS").child(String.valueOf(count)).setValue(new ProductModel(name,price,maxquantity,prodate,about1,extra1,category,FirebaseAuth.getInstance().getCurrentUser().getUid()));
                            Toast.makeText(CropPost.this,"Database Updated!!",Toast.LENGTH_SHORT).show();
                            count=0;
                            progressDialog.dismiss();
                            StorageReference imageref=storageReference.child("Products").child("POSTS").child(String.valueOf(count));
                            UploadTask uploadTask=imageref.putFile(uri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CropPost.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(CropPost.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 1000);
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(CropPost.this,"Fill the Details Properly",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

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
        editText4.setText(date);
    }

    private void intialize() {
        Spinner spinner = findViewById(R.id.Category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        imageView =findViewById(R.id.imageView);
        ImageButton imageButton = findViewById(R.id.button1);
        editText1 = (EditText) findViewById(R.id.ProductName);
        editText2 = (EditText) findViewById(R.id.ProductPrice);
        editText3 = (EditText) findViewById(R.id.MaxQuantity);
        editText4 = findViewById(R.id.ProductionDate);
        editText5 = findViewById(R.id.Description);
        editText6 = findViewById(R.id.Extra);
        textView = (TextView) findViewById(R.id.terms);
        button = (Button) findViewById(R.id.Post);
        progressDialog=new ProgressDialog(this);
        editText4.setShowSoftInputOnFocus(false);
        editText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new datePicker();
                dialogFragment.show(getSupportFragmentManager(),"date picker");
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new datePicker();
                dialogFragment.show(getSupportFragmentManager(),"date picker");
            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), category, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validate() {
        name = editText1.getText().toString();
        price = editText2.getText().toString();
        maxquantity = editText3.getText().toString();
        prodate = editText4.getText().toString();
        about1 = editText5.getText().toString();
        extra1 = editText6.getText().toString();

        databaseReference.child("POSTS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    count++;
                }
                Toast.makeText(CropPost.this,count+"SANDY",Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Hang on while we post your product ");
                progressDialog.show();
                if (name.isEmpty() || price.isEmpty() || maxquantity.isEmpty()|| prodate.isEmpty() || about1.isEmpty()) {
                    Toast.makeText(CropPost.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!("Set".equals(imageView.getTag()))){
                    Toast.makeText(CropPost.this, "Add a image", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    res=true;}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return res;


    }
}
