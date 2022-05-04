package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    EditText firstName, lastName, rEmail, rPassword, rConfirmPassword;
    TextView backToLogin;
    Button accountRegister;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;
    private static final String TAG = "RegisterUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        firstName = findViewById(R.id.First_Name);
        lastName = findViewById(R.id.Last_Name);
        rEmail = findViewById(R.id.reg_email);
        rPassword = findViewById(R.id.reg_password);
        backToLogin = findViewById(R.id.alreadyRegistered);
        rConfirmPassword = findViewById(R.id.reg_confirm_password);
        accountRegister = findViewById(R.id.completeRegister);

        fAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        accountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nEmail = rEmail.getText().toString().trim();
                String nPassword = rPassword.getText().toString().trim();
                String nConfirmPassword = rConfirmPassword.getText().toString().trim();
                final String nFirstName = firstName.getText().toString().trim();
                final String nLastName = lastName.getText().toString().trim();

                if (TextUtils.isEmpty(nEmail)) {
                    rEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(nPassword)) {
                    rPassword.setError("Password is required.");
                    return;
                }
                if(nPassword.length() < 8){
                    rPassword.setError("Password must be at least 8 characters");
                    return;
                }
                if(TextUtils.isEmpty(nConfirmPassword)){
                    rConfirmPassword.setError("Password confirmed not filled");
                    return;
                }
                if(!nPassword.equals(nConfirmPassword)){
                    Toast.makeText(RegisterUser.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(nEmail, nPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(RegisterUser.this, "User created!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Starting process to add user to database!");
                        } else {
                            Toast.makeText(RegisterUser.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        AddUser(fStore,fAuth, userID, nEmail, nFirstName, nLastName);
                    }
                });
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    private void AddUser(FirebaseFirestore fStore, FirebaseAuth fAuth, String userID, String email, String firstName, String lastName){
        Log.d(TAG, "Got database instance");
        //DocumentReference user = fStore.collection("users").document(userID);
        Map<String, Object> addUser = new HashMap<>();
        addUser.put("firstName", firstName);
        addUser.put("lastName", lastName);
        addUser.put("email", email);
        fStore.collection("users").document(userID).set(addUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "User has been successfully added.");
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error! Couldn't add user!");
                DeleteUser(fAuth);
            }
        });
    }

    private void DeleteUser(FirebaseAuth fAuth){
        FirebaseUser user = fAuth.getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User has been deleted.");
                }
            }
        });
    }
}