package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class CreateListActivity extends AppCompatActivity implements DialogCloseListener{

    private DataBaseHelper db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private EditText listName;
    private FloatingActionButton addTaskButton;
    private FloatingActionButton addItemButton;
    private Button finishButton;

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        Objects.requireNonNull(getSupportActionBar()).hide(); // this is the command to remove the name of the app at the top??

        db = new DataBaseHelper(this);
        //db.openDatabase(); database should only be opened within db methods?

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter = new ToDoAdapter(db,CreateListActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        addTaskButton = findViewById(R.id.addTask);
        addItemButton = findViewById(R.id.addItem);
        listName = findViewById(R.id.listName);
        finishButton = findViewById(R.id.finishButton);

        // fetches items and displays them
        taskList = db.getAllNewTasks();
        Collections.reverse(taskList); // newest on top
        tasksAdapter.setTasks(taskList);

        // ADD TASK BUTTON FUNCTION
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);

            }
        });

        // ADD ITEM BUTTON FUNCTION
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // duplicated the code for new task to separate new task and item
                AddNewItem.newInstance().show(getSupportFragmentManager(), AddNewItem.TAG);

            }
        });

        // FINISH LIST BUTTON FUNCTION
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need code here to add LIST NAME to parent database before finishing and returning to List Activity screen
                String text = listName.getText().toString(); // fetches edittext and sets as name in database

                // inserting a new list
                ParentToDoModel list = new ParentToDoModel();
                // inserting the info for database
                list.setName(text);

                db.insertList(list);
                // need to pull ID here?
                // need to set ID of new list to parentID of all list items currently displayed
                //int newListID = list.getId(); // THIS IS SETTING to 0 FOR SOME REASON!!!
                int newListID = db.getLastInsert(); // method uses last_insert_rowid in SQlite

                int i = 0;
                while (i < taskList.size()){

                    taskList.get(i).setAge(1); // set age of each item in current display list to 1
                    db.updateAge(taskList.get(i).getId(),1); // update database items with age of 1

                    taskList.get(i).setParentID(newListID); // update list items parentID
                    db.updateParent(taskList.get(i).getId(),newListID); // attaches new entry items to parent list in DB

                    i++;
                }

                finish(); // end activity

            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllNewTasks(); // needs to be getAllNEWTasks
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}