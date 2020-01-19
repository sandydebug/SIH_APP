package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CropProfile extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4,textView5;
    ImageView imageView;
    private ProgressDialog progressDialog;
    DatabaseReference posts;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_profile);

        initialize();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        storageReference=firebaseStorage.getReference();
        if(getIntent()!=null){
            pos=getIntent().getIntExtra("Position",0);
        }

        progressDialog.setMessage("Hang on while we load the posts ");
        progressDialog.show();

        DatabaseReference databaseReference=firebaseDatabase.getReference();
        databaseReference.child("PROFILES").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                textView1.setText(userProfile.getUserEmail());
                textView3.setText(userProfile.getPhonenumber());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //  Toast.makeText(start.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child("Products").child("POSTS").child(String.valueOf(pos)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().into(imageView);
            }
        });

        posts = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   textView1.setText("Product:"+postSnapshot.child(String.valueOf(pos)).child("proname").getValue().toString()+"\nAbout:"+postSnapshot.child(String.valueOf(pos)).child("about").getValue().toString());
                   textView5.setText("Rs."+postSnapshot.child(String.valueOf(pos)).child("proprice").getValue().toString() + "/kg  Max Amount: "+postSnapshot.child(String.valueOf(pos)).child("maxquantity").getValue().toString()+"kg");
                   textView4.setText(postSnapshot.child(String.valueOf(pos)).child("proddate").getValue().toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void initialize(){
        textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.email);
        textView3 = findViewById(R.id.phone);
        textView4 = findViewById(R.id.date);
        textView5 = findViewById(R.id.price);
        imageView = findViewById(R.id.itemPic);

    }
}
