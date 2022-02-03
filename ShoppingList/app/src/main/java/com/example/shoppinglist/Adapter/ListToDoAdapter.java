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

import com.example.shoppinglist.ListsActivity;

import com.example.shoppinglist.AddNewTask;
import com.example.shoppinglist.AddNewItem;

import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.Utils.DataBaseHelper;
//import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.List;

// used for the recycler view in createListActivity

public class ListToDoAdapter extends RecyclerView.Adapter<ListToDoAdapter.ViewHolder> {

    private List<ParentToDoModel> listOfLists;
    private DataBaseHelper db;
    private ListsActivity activity;

    public ListToDoAdapter(DataBaseHelper db, ListsActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entry_layout, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ParentToDoModel list = listOfLists.get(position);
        holder.name.setText(list.getName());

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

    @Override
    public int getItemCount() {
        return listOfLists.size();
    }

    public Context getContext() {
        return activity;
    }


    public void setLists(List<ParentToDoModel> listOfLists) {
        this.listOfLists = listOfLists;
        notifyDataSetChanged();
    }

    public void deleteList(int position) {
        ParentToDoModel list = listOfLists.get(position);
        db.deleteList(list.getId());
        listOfLists.remove(position);
        notifyItemRemoved(position);
    }

    public void editNameOfList(int position) {
        ParentToDoModel list = listOfLists.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("name", list.getName());

        // might need to add new fragment to replace this one
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // (Caleb) removed checkbox check for entry layout
        //CheckBox task;
        TextView name;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.listName);
        }
    }
}
