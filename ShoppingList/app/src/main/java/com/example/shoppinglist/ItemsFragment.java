package com.example.shoppinglist;

import static com.example.shoppinglist.AddNewItem.TAG;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppinglist.Adapter.InspectItemsAdapter;
import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Adapter.ToDoAdapter;
import com.example.shoppinglist.Model.ToDoModel;
import com.example.shoppinglist.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Intent;

// ITEMS TAB OF INSPECTLIST ACTIVITY

public class ItemsFragment extends Fragment implements InspectItemsAdapter.OnItemListener {

    private DataBaseHelper db;
    private RecyclerView itemsRecyclerView;
    private InspectItemsAdapter itemsAdapter;
    //private int listID;
    private List<ToDoModel> itemList;
    private List<ToDoModel> filteredItemList;
    public InspectListActivity inspectListActivity;
    private String itemName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // declare activity to access listID variable
        inspectListActivity = (InspectListActivity) getActivity();

        db = new DataBaseHelper(getActivity()); // context is inspectlistActivity?

        View view = inflater.inflate(R.layout.fragment_items, container, false);

        RecyclerView itemsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewItems);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // this needs 3 arguments to accommodate for click listener
        InspectItemsAdapter itemsAdapter = new InspectItemsAdapter(db,ItemsFragment.this, this);
        itemsRecyclerView.setAdapter(itemsAdapter);

        // fetches items and displays them
        // needs to be unique function that is only ITEMS of SELECTED LIST
        //itemList = db.getAllTasks();
        itemList = db.getAllListItems();
        filteredItemList = new ArrayList<>();
        // iterate through itemList and get only items that match parent ID
        int i = 0;
        while (i < itemList.size()){
            if (itemList.get(i).getParentID() == inspectListActivity.listID){
                filteredItemList.add(itemList.get(i)); // add item to filtered list if belongs to correct parent
            }
            i++;
        }

        //Collections.reverse(taskList); // newest on top
        itemsAdapter.setTasks(filteredItemList);

        return view;
    }

    // clicking on items (has placeholder)
    @Override
    public void onItemClick(int position){
        //Log.d(TAG, "onListClick: clicked");
        //listOfLists.get(position); // will be used to load proper info in list inspection
        // get parentID to pass to InspectActivity

        itemName = itemList.get(position).getTask();
        Intent intent = new Intent(getActivity(), WebScrape.class);
        intent.putExtra("ItemName", itemName);
        startActivity(intent);
    }
}