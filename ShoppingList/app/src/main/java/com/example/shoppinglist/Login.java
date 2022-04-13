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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


public class Login extends AppCompatActivity {

    private Button loginBypass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView username = (TextView) findViewById(R.id.userEmail);
        TextView password = (TextView) findViewById(R.id.userPassword);
        TextView startRegister = (TextView) findViewById(R.id.newRegister);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.btn_login);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
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
    }

    public void registerUser(View v){
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
        finish();
    }
}
