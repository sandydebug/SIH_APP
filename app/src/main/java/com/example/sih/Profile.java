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

    TextView textView1,textView2,textView3;
    ImageView imageView;
    private ProgressDialog progressDialog;
    DatabaseReference posts;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        storageReference=firebaseStorage.getReference();
        if(getIntent()!=null){
            pos=getIntent().getIntExtra("Position",0);
        }

        progressDialog.setMessage("Hang on while we load the posts ");
        progressDialog.show();

        if (firebaseAuth.getCurrentUser() != null) {
            textView2.setText(firebaseAuth.getCurrentUser().getEmail());
            imageView.setImageURI(firebaseAuth.getCurrentUser().getPhotoUrl());

            DatabaseReference databaseReference=firebaseDatabase.getReference();
            databaseReference.child("PROFILES").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                    textView1.setText(userProfile.getUserName());
                    textView3.setText(userProfile.getPhonenumber());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Could not load User", Toast.LENGTH_SHORT).show();
            finish();
        }

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child("Products").child("POSTS").child(String.valueOf(pos)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().into(imageView);
            }
        });

        progressDialog.dismiss();

    }
    public void initialize(){
        textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.email);
        textView3 = findViewById(R.id.phone);
        imageView = findViewById(R.id.itemPic);

    }
}
