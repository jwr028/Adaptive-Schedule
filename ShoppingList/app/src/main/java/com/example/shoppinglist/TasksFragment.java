package com.example.shoppinglist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppinglist.Adapter.InspectTasksAdapter;
import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Tasks TAB OF INSPECT LIST ACTIVITY

public class TasksFragment extends Fragment {

    private DataBaseHelper db;
    private RecyclerView tasksRecyclerView;
    private InspectTasksAdapter tasksAdapter;
    //private int listID;
    private List<ToDoModel> taskList;
    private List<ToDoModel> filteredTaskList;
    public InspectListActivity inspectListActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // need to assign onclick listener to checkbox to update database when clicked


        // declare activity to access listID variable
        inspectListActivity = (InspectListActivity) getActivity();

        db = new DataBaseHelper(getActivity()); // context is inspect list Activity?

        View view = inflater.inflate(R.layout.fragment_items, container, false);

        RecyclerView itemsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewItems);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        InspectTasksAdapter tasksAdapter = new InspectTasksAdapter(db,TasksFragment.this);
        itemsRecyclerView.setAdapter(tasksAdapter);

        // fetches items and displays them
        // needs to be unique function that is only ITEMS of SELECTED LIST
        //itemList = db.getAllTasks();
        taskList = db.getAllListTasks();
        filteredTaskList = new ArrayList<>();
        // iterate through itemList and get only items that match parent ID
        int i = 0;
        while (i < taskList.size()){
            if (taskList.get(i).getParentID() == inspectListActivity.listID){
                filteredTaskList.add(taskList.get(i)); // add item to filtered list if belongs to correct parent
            }
            i++;
        }

        Collections.reverse(taskList); // newest on top
        tasksAdapter.setTasks(filteredTaskList);

        return view;
    }
}