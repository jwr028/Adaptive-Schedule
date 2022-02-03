package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private DataBaseHelper db;

    private RecyclerView listsRecyclerView;
    private ListToDoAdapter listsAdapter;

    private Button newListButton;

    private List<ParentToDoModel> listOfLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        db = new DataBaseHelper(this);

        listsRecyclerView = findViewById(R.id.listsRecyclerView);
        listsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listsAdapter = new ListToDoAdapter(db,ListsActivity.this);
        listsRecyclerView.setAdapter(listsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(listsAdapter));
        itemTouchHelper.attachToRecyclerView(listsRecyclerView);

        newListButton = findViewById(R.id.newListButton);

        // fetches items and displays them
        listOfLists = db.getAllLists();
        Collections.reverse(listOfLists); // newest on top
        listsAdapter.setLists(listOfLists);

        // NEW LIST BUTTON FUNCTION
        newListButton.setOnClickListener(new View.OnClickListener() {
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