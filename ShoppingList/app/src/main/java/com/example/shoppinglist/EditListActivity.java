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
import com.example.shoppinglist.TouchHelper.RecyclerItemTouchHelperEdit;
import com.example.shoppinglist.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.shoppinglist.Adapter.EditAdapter;
import com.example.shoppinglist.Model.ToDoModel;

import java.util.Collections;
import java.util.List;

// TODO: CANNOT ADD,  FINISH BUTTON NEEDS CHANGE

public class EditListActivity extends AppCompatActivity implements DialogCloseListener {

    private DataBaseHelper db;

    private RecyclerView tasksRecyclerView;
    private EditAdapter tasksAdapter;
    private ListToDoAdapter listsAdapter;
    public TextView listName;
    private ExtendedFloatingActionButton addTaskButton;
    private ExtendedFloatingActionButton addItemButton;
    private Button finishButton;

    // extras
    public int listID;
    public String listNameEx;


    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        // EXTRAAAAAAAAAAS
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            listID = extras.getInt("listID");
            listNameEx = extras.getString("listName");
        }

        db = new DataBaseHelper(this);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter = new EditAdapter(db, EditListActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperEdit(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        addTaskButton = findViewById(R.id.addTask);
        addItemButton = findViewById(R.id.addItem);
        listName = findViewById(R.id.listName);
        finishButton = findViewById(R.id.finishButton);

        // set listName text to match
        // listName = listNameEx
        listName.setText(listNameEx);

        // need to put in EXTRA for ID
        taskList = db.getAllTasks(listID);
        Collections.reverse(taskList); // newest on top
        tasksAdapter.setTasks(taskList);


        // ADD TASK BUTTON FUNCTION
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewTaskEdit.newInstance().show(getSupportFragmentManager(), AddNewTaskEdit.TAG);
                // THESE NEED NEW INSTANCES (AddNewTaskEdit)

            }
        });

        // ADD ITEM BUTTON FUNCTION
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // duplicated the code for new task to separate new task and item
                AddNewItemEdit.newInstance().show(getSupportFragmentManager(), AddNewItemEdit.TAG);
                // replace with AddNewItemEdit

            }
        });

        // MAKE TEXT VIEW CLICKABLE with editing layout (cleaner than generic edittext widget)
        listName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // duplicated the code for new task to separate new task and item
                AddNewNameEdit.newInstance().show(getSupportFragmentManager(), AddNewNameEdit.TAG);

            }
        });

        // FINISH LIST BUTTON FUNCTION
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // EDIT version of this function just updates name of list instead of creating new one


                // prevent creating empty list
                if (tasksAdapter.getItemCount() != 0) {
                    // need code here to add LIST NAME to parent database before finishing and returning to List Activity screen
                    String text = listName.getText().toString(); // fetches TextView and sets as name in database

                    // update list name with what is currently in the top textview
                    db.updateListName(listID,text);

                    finish();

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
    public void handleDialogClose(DialogInterface dialog) {
        /*
        taskList = db.getAllNewTasks(); // needs to be getAllNEWTasks
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();

         */

        // this needs to be alltasksoflistID
        taskList = db.getAllTasks(listID);
        Collections.reverse(taskList); // newest on top
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}
