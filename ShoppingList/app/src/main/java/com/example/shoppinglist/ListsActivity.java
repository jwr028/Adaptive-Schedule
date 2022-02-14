package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.List;

public class ListsActivity extends AppCompatActivity implements DialogCloseListener, ListToDoAdapter.OnListListener {

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

        listsAdapter = new ListToDoAdapter(db,ListsActivity.this, this);
        listsRecyclerView.setAdapter(listsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperLists(listsAdapter));
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

    // button function to move to InspectList screen
    public void openInspectListActivity() {
        Intent intent = new Intent(this, InspectListActivity.class);
        startActivity(intent);
    }

    // button function to move to CreateList screen
    public void openCreateListActivity() {
        Intent intent = new Intent(this, CreateListActivity.class);
        startActivity(intent);
    }

    // clicking on lists
    @Override
    public void onListClick(int position){
        //Log.d(TAG, "onListClick: clicked");
        listOfLists.get(position); // will be used to load proper info in list inspection
        Intent intent = new Intent(this, InspectListActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        listOfLists = db.getAllLists(); // refreshing Lists from database
        Collections.reverse(listOfLists);
        listsAdapter.setLists(listOfLists);
        listsAdapter.notifyDataSetChanged();
    }
}