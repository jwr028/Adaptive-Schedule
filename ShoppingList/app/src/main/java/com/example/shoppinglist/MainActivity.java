package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.shoppinglist.Utils.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // big START button
        Button button = (Button) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListsActivity();
            }
        });

    }

    // button function to move to Lists screen
    public void openListsActivity() {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    // button function to clear database for debugging
    //public void clearDatabase(){
    //    DataBaseHelper.clearDatabase();

    //}

}