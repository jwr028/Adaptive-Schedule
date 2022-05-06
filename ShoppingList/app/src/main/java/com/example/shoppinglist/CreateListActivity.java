package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.TouchHelper.RecyclerItemTouchHelper;
import com.example.shoppinglist.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;

import java.util.Collections;
import java.util.List;

public class CreateListActivity extends AppCompatActivity implements DialogCloseListener{

    private DataBaseHelper db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private ListToDoAdapter listsAdapter;
    public TextView listName;
    private ExtendedFloatingActionButton addTaskButton;
    private ExtendedFloatingActionButton addItemButton;
    private Button finishButton;

    //for toast

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        db = new DataBaseHelper(this);

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

        // MAKE TEXT VIEW CLICKABLE with editing layout (cleaner than generic edittext widget)
        listName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // duplicated the code for new task to separate new task and item
                AddNewName.newInstance().show(getSupportFragmentManager(), AddNewName.TAG);

            }
        });

        // FINISH LIST BUTTON FUNCTION
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // prevent creating empty list
                if (tasksAdapter.getItemCount() != 0){
                    // need code here to add LIST NAME to parent database before finishing and returning to List Activity screen
                    String text = listName.getText().toString(); // fetches TextView and sets as name in database

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
                    // notify data set has changed for LISTS
                    //Intent returnIntent = new Intent();
                    //returnIntent.putExtra("result",result);
                    //setResult(ListsActivity.RESULT_OK,returnIntent);
                    //listsAdapter.deleteAllLists();
                    finish();
                    //Adapter.notifyDataSetChanged();
                } else {
                    // toast instead
                    Context context = getApplicationContext();
                    CharSequence text = "Cannot create an empty list!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });


    }

    /*
    // Call Back method  to get the Message from other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String message=data.getStringExtra("NEW NAME");
            listName.setText(message);
        }
    }

     */

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllNewTasks(); // needs to be getAllNEWTasks
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}