package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Utils.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    //DataBaseHelper db; // for debug button
    //ListToDoAdapter listsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db = new DataBaseHelper(this);
        //listsAdapter = new ListToDoAdapter(db,ListsActivity.this, this);

        // big START button
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListsActivity();
            }
        });

        Button navigation_btn = findViewById(R.id.launch);
        navigation_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("google.navigation:q=32.54372,-92.62258&mode=d"));
                Intent chooser = Intent.createChooser(intent,"Launch Maps");
                startActivity(chooser);
            }
        });

    }

    // button function to move to Lists screen
    public void openListsActivity() {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

}