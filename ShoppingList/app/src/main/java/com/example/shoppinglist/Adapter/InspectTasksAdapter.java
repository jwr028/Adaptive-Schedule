package com.example.shoppinglist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.CreateListActivity;

import com.example.shoppinglist.AddNewTask;
import com.example.shoppinglist.AddNewItem;

import com.example.shoppinglist.TasksFragment;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.Utils.DataBaseHelper;
//import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.List;

// used for the inspect TASKS fragment

public class InspectTasksAdapter extends RecyclerView.Adapter<InspectTasksAdapter.ViewHolder> {

    private List<ToDoModel> todoList;

    private DataBaseHelper db;
    private TasksFragment fragment;

    // need to create onclick listener for checkbox to update database
    private CheckBox checkBox;

    public InspectTasksAdapter(DataBaseHelper db, TasksFragment fragment) {
        this.db = db;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);

        // declaration for onclick listener
        checkBox = itemView.findViewById(R.id.todoCheckBox);

        return new ViewHolder(itemView);
        
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.taskStatus.setText(item.getTask());

        // this sets the checkbox CHECKED if status == 1
        holder.taskStatus.setChecked(toBoolean(item.getStatus()));

        // trigger function on checkbox click
        //checkBox = findViewById (can't do this in fragment)
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openCreateListActivity();
                // get adapter position?

                updateStatus(holder.getAbsoluteAdapterPosition());
            }
        });


    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    // override to check if currently being created list is empty, to prevent crashing from creating empty list
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return fragment.getContext();
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    // called on checkbox click (declared in XML)
    public void updateStatus(int position) {
        ToDoModel item = todoList.get(position);
        // if status is 0, set to 1, and vice versa
        if (item.getStatus() == 0) {
            db.updateStatus(item.getId(), 1);
        }
        else {
            db.updateStatus(item.getId(), 0);
        }
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox taskStatus;
        //TextView task;

        ViewHolder(View view) {
            super(view);
            //task = view.findViewById(R.id.taskText);
            taskStatus = view.findViewById(R.id.todoCheckBox);
        }
    }
}
