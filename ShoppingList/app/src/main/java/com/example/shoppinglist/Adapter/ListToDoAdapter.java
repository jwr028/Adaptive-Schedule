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

import com.example.shoppinglist.EditListName;

import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.List;

// used for the recycler view in ListActivity

public class ListToDoAdapter extends RecyclerView.Adapter<ListToDoAdapter.ViewHolder> {

    public List<ParentToDoModel> listOfLists;
    private OnListListener mOnListListener;
    private DataBaseHelper db;
    private ListsActivity activity;

    public ListToDoAdapter(DataBaseHelper db, ListsActivity activity, OnListListener onListListener) {
        this.db = db;
        this.activity = activity;
        this.mOnListListener = onListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entry_layout, parent, false);
        return new ViewHolder(itemView, mOnListListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ParentToDoModel list = listOfLists.get(position);
        holder.name.setText(list.getName());

        //holder.task.setChecked(toBoolean(item.getStatus()));

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

    public void deleteAllLists() {
        int size = listOfLists.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listOfLists.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }

        db.deleteAllLists();
        //notifyDataSetChanged();
    }

    // does not work yet
    public void editNameOfList(int position) {
        ParentToDoModel list = listOfLists.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", list.getId());
        bundle.putString("name", list.getName());

        // might need to add new fragment to replace this one
        EditListName fragment = new EditListName();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), EditListName.TAG);

    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // (Caleb) removed checkbox check for entry layout
        //CheckBox task;
        TextView name;

        OnListListener onListListener;

        ViewHolder(View view, OnListListener onListListener) {
            super(view);
            name = view.findViewById(R.id.listName);
            this.onListListener = onListListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onListListener.onListClick(getAdapterPosition());
        }
    }

    // detect clicking lists
    public interface OnListListener{
        void onListClick(int position);
    }
}
