package com.example.sih;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Loggedin extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    String conv="";
    private Button button2;
    private ViewFlipper viewFlipper;
    private ImageView imageView4,imageView5;
    private TextView textView3,textView4;
    GoogleSignInClient mGoogleSignInClient;

    public void onBackPressed() {


            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);


        firebaseAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menubar , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.post:
                startActivity(new Intent(Loggedin.this,CropPost.class));
                break;
            case R.id.logout:

                if(firebaseAuth.getCurrentUser()!= null){
                    new AlertDialog.Builder(this)
                            .setTitle("LOGOUT")
                            .setMessage("Are you sure you want to logout ?  I suggest spend some more time :) ")

                            .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.signOut();
                                    mGoogleSignInClient.signOut();
                                    finish();
                                    startActivity(new Intent(Loggedin.this,Login.class));
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                            .setIcon(android.R.drawable.ic_lock_power_off)
                            .show();}
                else{
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Loggedin.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Loggedin.this, Login.class));
                                    finish();
                                }
                            });

                }
                break;

        }
        return true;
    }

}
