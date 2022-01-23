package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
//import com.example.shoppinglist.Adapter.listAdapter;

import java.util.ArrayList;

public class CreateListActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private listAdapter tasksAdapter;

    private List<listModel> checkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        getSupportActionBar().hide();

        checkList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new listAdapter(this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        listModel task = new taskModel();
        task.setTask("This is a test task");
        task.setStatus(0);
        task.setId(1);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        tasksAdapter.setTasks(checkList);
}