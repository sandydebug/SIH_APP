package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.blurry.Blurry;

public class Login extends AppCompatActivity {

    TextView textView,textView1,textView2;
    EditText editText,editText1;
    Button button;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private DatabaseReference databaseReference;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private SharedPreferences sharedPreferences;
    private String username , password;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);

      /*  View view = findViewById(R.id.conlay);
        Drawable back = view.getBackground();
        back.setAlpha(100);*/

        editText=(EditText)findViewById(R.id.name);
        editText1=(EditText)findViewById(R.id.password);
        button=(Button)findViewById(R.id.login);
        signInButton = findViewById(R.id.sign_in_button);
        textView=(TextView)findViewById(R.id.textView);
        textView1=(TextView)findViewById(R.id.forgot);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("PROFILES");

        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkbox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editText.setText(loginPreferences.getString("username", ""));
            editText1.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        if(firebaseUser!=null){
            startActivity(new Intent(Login.this, Loggedin.class));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty() || editText1.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.name));
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.password));
                }
                else{
                    username = editText.getText().toString();
                    password = editText1.getText().toString();

                    if (saveLoginCheckBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                    validate(editText.getText().toString(),editText1.getText().toString());
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(Login.this,"Please enter your registered mail id and the press forgot password ", Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(editText.getText().toString());
                    Toast.makeText(Login.this,"Mail with rest link to your registered mail ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void validate(String username, String password){

        progressDialog.setMessage("Hang on while we connect you ");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    emailVerification();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this,"Login Failed!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void emailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean email=firebaseUser.isEmailVerified();
        if(email){
            finish();
            startActivity(new Intent(Login.this,Dashboard.class));
        }
        else{
            Toast.makeText(Login.this,"Verify your mail", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }
    }
    private void signIn() {
        progressDialog.setMessage("Hang on while we connect you ");
        progressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            /*Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
               /* //TO USE
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                String personPhoneURL = acct.getPhotoUrl().toString();*/
                firebaseAuthWithGoogle(acct);
            } else {
                Toast.makeText(Login.this,"There was a trouble signing in-Please try again", Toast.LENGTH_SHORT).show();;
            }
        }
    }

    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(Login.this, Loggedin.class));
        }
        super.onStart();
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            //databaseReference.child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new ProfileData(acct.getEmail(),acct.getDisplayName()));
                            Toast.makeText(Login.this, "Authentication pass.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(Login.this, Loggedin.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
