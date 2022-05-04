package com.example.shoppinglist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {
    TextView firstName, lastName, email, comments;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        firstName = findViewById(R.id.first_name_display);
        lastName = findViewById(R.id.last_name_display);
        email = findViewById(R.id.display_user_email);
        comments = findViewById(R.id.comments);

        
    }
}
