package com.example.shoppinglist;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        TextView register = (TextView) findViewById(R.id.Register);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.btn_login);
        MaterialButton regi = (MaterialButton) findViewById(R.id.register_test);

        loginbtn.setOnClickListener(view -> {
            if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                //correct
                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                openListActivity();

            }else{
                //incorrect
                Toast.makeText(Login.this, "Incorrect credentials!", Toast.LENGTH_SHORT).show();
            }
        });

        regi.setOnClickListener(view -> {
            openRegisterUser();
        });
    }
    public void openListActivity(){
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    public void registerUser(View v){
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
        finish();
    }

    public void openRegisterUser(){
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

}
