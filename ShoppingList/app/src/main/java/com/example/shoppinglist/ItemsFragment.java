package com.example.shoppinglist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppinglist.Adapter.InspectItemsAdapter;
import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.List;


public class ItemsFragment extends Fragment {

    private DataBaseHelper db;
    private RecyclerView itemsRecyclerView;
    private InspectItemsAdapter itemsAdapter;
    private List<ToDoModel> itemList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DataBaseHelper(getActivity()); // context is inspectlistActivity?

        View view = inflater.inflate(R.layout.fragment_items, container, false);

        RecyclerView itemsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewItems);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        InspectItemsAdapter itemsAdapter = new InspectItemsAdapter(db,ItemsFragment.this);
        itemsRecyclerView.setAdapter(itemsAdapter);

        // fetches items and displays them
        itemList = db.getAllTasks();
        //Collections.reverse(taskList); // newest on top
        itemsAdapter.setTasks(itemList);

        return view;
    }
}