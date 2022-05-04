package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shoppinglist.ui.main.SectionsPagerAdapter;
import com.example.shoppinglist.databinding.ActivityInspectListBinding;

public class InspectListActivity extends AppCompatActivity {

    private ActivityInspectListBinding binding;
    public int listID;
    public String listName;

    public AppBarLayout appBarLayout;
    public TextView activityTitle;



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


}