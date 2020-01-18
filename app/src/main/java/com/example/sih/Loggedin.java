package com.example.sih;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Adapters.PostAdapter;
import com.example.sih.Models.DrawerHeader;
import com.example.sih.Models.DrawerMenuItem;
import com.example.sih.Models.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.placeholderview.PlaceHolderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Loggedin extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    ArrayList<PostModel> items = new ArrayList<PostModel>();
    DatabaseReference orders,users;
    Handler mHandler;
    Uri img;
    private StorageReference storageReference;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

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

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,5000);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(this);
        storageReference=firebaseStorage.getReference();

        progressDialog.setMessage("Hang on while we load the posts ");
        progressDialog.show();

        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        setupDrawer();


        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        orders = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        orders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(int i =0;i<postSnapshot.getChildrenCount();i++) {
                        if(postSnapshot.child(String.valueOf(i)).child("cat").getValue().toString().equals("Vegetables")){
                            items.add(new PostModel(postSnapshot.child(String.valueOf(i)).child("proname").getValue().toString(),"Rs. "+postSnapshot.child(String.valueOf(i)).child("proprice").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("proddate").getValue().toString(),R.drawable.veggies ,postSnapshot.child(String.valueOf(i)).getKey().toString()));}

                        else if(postSnapshot.child(String.valueOf(i)).child("cat").getValue().toString().equals("Fruits")){
                            items.add(new PostModel(postSnapshot.child(String.valueOf(i)).child("proname").getValue().toString(),"Rs. "+postSnapshot.child(String.valueOf(i)).child("proprice").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("proddate").getValue().toString(),R.drawable.fruit ,postSnapshot.child(String.valueOf(i)).getKey().toString()));}

                        else if(postSnapshot.child(String.valueOf(i)).child("cat").getValue().toString().equals("Cereals/Pulses")){
                            items.add(new PostModel(postSnapshot.child(String.valueOf(i)).child("proname").getValue().toString(),"Rs. "+postSnapshot.child(String.valueOf(i)).child("proprice").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("proddate").getValue().toString(),R.drawable.cereals ,postSnapshot.child(String.valueOf(i)).getKey().toString()));}

                    }
                }
                if(items.isEmpty()){
                    items.add(new PostModel(null,"NO ORDERS YET.","NULL",R.drawable.ic_person_black_24dp,null));
                }
                progressDialog.dismiss();
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
                                    //mGoogleSignInClient.signOut();
                                    finish();
                                    startActivity(new Intent(Loggedin.this,Login.class));
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                            .setIcon(android.R.drawable.ic_lock_power_off)
                            .show();}
               /* else{
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Loggedin.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Loggedin.this, Login.class));
                                    finish();
                                }
                            });

                }*/
                break;

        }
        return true;
    }
    private void filter(String text) {
        ArrayList<PostModel> filteredList = new ArrayList<>();

        for (PostModel item : items) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PostAdapter(items);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(Loggedin.this,"in runnable",Toast.LENGTH_SHORT).show();

            Loggedin.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable

    private void setupDrawer() {
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_POST))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CONTACT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_ABOUT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle  drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

}