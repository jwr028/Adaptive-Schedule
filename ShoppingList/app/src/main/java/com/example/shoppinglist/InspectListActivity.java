package com.example.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shoppinglist.ui.main.SectionsPagerAdapter;
import com.example.shoppinglist.databinding.ActivityInspectListBinding;

public class InspectListActivity extends AppCompatActivity implements DialogCloseListener {

    private ActivityInspectListBinding binding;
    public int listID;
    public String listName;

    public AppBarLayout appBarLayout;
    public TextView activityTitle;


    // attempt to refresh activity
    @Override
    public void onRestart(){
        super.onRestart();
        this.recreate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            listID = extras.getInt("listID");
            listName = extras.getString("listName");
        }


        binding = ActivityInspectListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        // gotta access appbarlayout to get to title text?
        appBarLayout = findViewById(R.id.appBar_Layout);

        // gotta inflate to access title text?

        activityTitle = (TextView) findViewById(R.id.titleText);
        activityTitle.setText("Inspect List: " + listName);

    }



    @Override
    public void handleDialogClose(DialogInterface dialog){
        //Log.d("thing","thingy");
        //listOfLists = db.getAllLists(); // refreshing Lists from database
        //Collections.reverse(listOfLists);
        //listsAdapter.setLists(listOfLists);
        //listsAdapter.notifyDataSetChanged();
    }

}