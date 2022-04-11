package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends AppCompatActivity {
    EditText firstName, lastName, rEmail, rPassword, rConfirmPassword;
    TextView backToLogin;
    Button accountRegister;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        accountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nEmail = rEmail.getText().toString();
                String nPassword = rPassword.getText().toString();
                String nConfirmPassword = rConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(nEmail)) {
                    rEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(nPassword)) {
                    rPassword.setError("Password is required.");
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
                            Toast.makeText(RegisterUser.this, "User created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterUser.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
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
}