package com.example.shoppinglist.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.WebScrape;

import java.util.ArrayList;

public class WebScrapeAdapter extends RecyclerView.Adapter {

    private ArrayList<WebScrape> recyclerViewWeb;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (recyclerViewWeb!= null) {
            return recyclerViewWeb.size();
        } else {
            return 0;
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView itemName;
        public final ImageView imageURL;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            itemName = view.findViewById(R.id.webText);
            imageURL = view.findViewById(R.id.webImage);
        }
    }
}