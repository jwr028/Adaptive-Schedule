package com.example.shoppinglist;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;


public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private Button loginBypass;
    SignInButton signInGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView username = (TextView) findViewById(R.id.userEmail);
        TextView password = (TextView) findViewById(R.id.userPassword);
        TextView startRegister = (TextView) findViewById(R.id.newRegister);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.btn_login);
        fAuth = FirebaseAuth.getInstance();
        signInGoogle = findViewById(R.id.googleSignIn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount != null){
            signOut();
        }

        if(fAuth.getCurrentUser() != null){
            signOut();
        }

        loginbtn.setOnClickListener(view -> {
            String loginEmail = username.getText().toString();
            String loginPassword = password.getText().toString();

            if(TextUtils.isEmpty(loginEmail)){
                username.setError("Email is required!");
                return;
            }
            if(TextUtils.isEmpty((loginPassword))){
                password.setError("Password is required.");
                return;
            }

            fAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ListsActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Invalid user! " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        loginBypass = findViewById(R.id.loginBypass);
        loginBypass.setVisibility(View.INVISIBLE);
        loginBypass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), ListsActivity.class));
               finish();
           }
        });

        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterUser.class));
                finish();
            }
        });

        // If the sign in google button is clicked, have user signed in.
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    // Creating the sign in for Google Accounts.
    private void signIn(){
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    // Creating the sign out for all Accounts.
    private void signOut(){
        // Firebase signout
        fAuth.getInstance().signOut();

        //Google client sign out
        signInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "You have been signed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } else {
                    Log.w(TAG, "Error signing out!");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(signInAccount);
            } catch(ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        fStore = FirebaseFirestore.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        fAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = fAuth.getCurrentUser();
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    if(isNew){
                        Log.d(TAG, "New google user, adding to database.");
                        final String userID = user.getUid();
                        addNewGoogleUser(fStore, user);
                    }
                    startActivity(new Intent(getApplicationContext(), ListsActivity.class));
                } else {
                    Toast.makeText(Login.this, "User does not exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewGoogleUser(FirebaseFirestore fStore, FirebaseUser user){
        final String userID = user.getUid();
        final String email = user.getEmail().toString();
        final String[] fullName = user.getDisplayName().split("\\s+");
        final String firstName = fullName[0];
        final String lastName = fullName[fullName.length-1];

        HashMap<String, Object> googleUser = new HashMap<>();
        googleUser.put("firstName", firstName);
        googleUser.put("lastName", lastName);
        googleUser.put("email", email);

        fStore.collection("users").document(userID).set(googleUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Google user successfully added.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Google user is either already added or could not be added!", e);
            }
        });
    }
}
