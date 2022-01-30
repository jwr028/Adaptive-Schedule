package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Button button = (Button) findViewById(R.id.newListButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListActivity();
            }
        });

    }

    // button function to move to CreateList screen
    public void openCreateListActivity() {
        Intent intent = new Intent(this, CreateListActivity.class);
        startActivity(intent);

    }
}