package com.example.shoppinglist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.WebScrape;
import com.example.shoppinglist.WebScrapeItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WebScrapeAdapter extends RecyclerView.Adapter<WebScrapeAdapter.ViewHolder> {

    private ArrayList<WebScrapeItem> recyclerViewWeb;

    public WebScrapeAdapter(ArrayList<WebScrapeItem> recyclerViewWeb){
        this.recyclerViewWeb = recyclerViewWeb;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.web_scrape_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WebScrapeItem item = recyclerViewWeb.get(position);



        holder.itemName.setText(item.getItemName());
        Picasso.get().load(item.getImageURL()).into(holder.image);

        Debug.logStack("asd", item.getImageURL(), 10);
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
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            itemName = view.findViewById(R.id.webText);
            image = view.findViewById(R.id.webImage);
        }
    }
}