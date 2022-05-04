package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserProfile extends AppCompatActivity {
    TextView firstName, lastName, email, comments;
    Button signOut, backToLists;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    GoogleSignInClient signInClient;
    private static final String TAG = "UserProfile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        firstName = findViewById(R.id.first_name_display);
        lastName = findViewById(R.id.last_name_display);
        email = findViewById(R.id.display_user_email);
        comments = findViewById(R.id.comments);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        fStore.collection("users").document(userID).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentSnapshot userSnapshot = task.getResult();
               if(userSnapshot.exists()){
                   String userFirstName =  userSnapshot.get("firstName").toString();
                   firstName.setText(userFirstName);
                   lastName.setText(userSnapshot.get("lastName").toString());
                   email.setText(userSnapshot.get("email").toString());
               }
           }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeSignOut();
            }
        });

        backToLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListsActivity.class));
            }
        });
    }

    // Creating the sign out for all Accounts.
    private void completeSignOut(){
        // Firebase signout
        fAuth.getInstance().signOut();

        //Google client sign out
        signInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserProfile.this, "You have been signed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } else {
                    Log.w(TAG, "Error signing out!");
                }
            }
        });
    }
}
