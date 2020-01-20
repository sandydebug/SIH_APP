package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.Models.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4,textView5;
    ImageView imageView;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        storageReference = firebaseStorage.getReference();

        progressDialog.setMessage("Hang on while we load your profile ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference=firebaseDatabase.getReference();
        databaseReference.child("PROFILES").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                if (firebaseAuth.getCurrentUser() != null) {
                    textView1.setText(userProfile.getUserName());
                    textView2.setText(userProfile.getUserEmail());
                    textView3.setText(userProfile.getPhonenumber());
                    textView4.setText(userProfile.getLocation()+",PIN:"+userProfile.getPincode());
                    textView5.setText(userProfile.getdob());
                } else {
                    Toast.makeText(Profile.this, "Could not load User", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //  Toast.makeText(start.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });


        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().into(imageView);
                progressDialog.dismiss();
            }
        });


    }
    public void initialize(){
        textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.email);
        textView3 = findViewById(R.id.phone);
        textView4 = findViewById(R.id.location);
        textView5 = findViewById(R.id.dob);
        imageView = findViewById(R.id.itemPic);

    }
}
