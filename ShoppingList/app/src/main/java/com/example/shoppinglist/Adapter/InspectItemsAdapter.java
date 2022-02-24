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

import com.example.shoppinglist.ItemsFragment;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.Utils.DataBaseHelper;
//import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.List;

// used for the inspect ITEMS fragment

public class InspectItemsAdapter extends RecyclerView.Adapter<InspectItemsAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DataBaseHelper db;
    private ItemsFragment fragment;

    public InspectItemsAdapter(DataBaseHelper db, ItemsFragment fragment) {
        this.db = db;
        this.fragment = fragment;
    }

    // override of viewtype needed to display different color layout for respective type of entry
    @Override //add this method to your adapter
    public int getItemViewType(int position) {
        final ToDoModel item = todoList.get(position);
        if(item.getType().toString().equals("task")){
            return 1; // blue layout
        }else{
            //System.out.println(); lol
            return 2; // green layout
        }
    }

    // (Caleb) here I replaced task_layout with task_entry_layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_entry_layout, parent, false);
        // need to distinguish layouts with respective colors
        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_entry_layout_blue, parent, false);
            return new ViewHolder(itemView);
        }
        else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_entry_layout_green, parent, false);
            return new ViewHolder(itemView);
        }

        //return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());

        //holder.task.setChecked(toBoolean(item.getStatus()));
        /**
         holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
        db.updateStatus(item.getId(), 1);
        } else {
        db.updateStatus(item.getId(), 0);
        }
        }
        });
         */
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

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // (Caleb) removed checkbox check for entry layout
        //CheckBox task;
        TextView task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.taskText);
        }
    }
}
