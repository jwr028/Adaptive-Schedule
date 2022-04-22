package com.example.shoppinglist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.Model.WebScrapeItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WebScrapeAdapter extends RecyclerView.Adapter<WebScrapeAdapter.ViewHolder> {

    private ArrayList<WebScrapeItem> recyclerViewWeb;

    private OnWebScrapeListener mOnWebScrapeListener;

    public WebScrapeAdapter(ArrayList<WebScrapeItem> recyclerViewWeb, OnWebScrapeListener onWebScrapeListener){
        this.recyclerViewWeb = recyclerViewWeb;

        this.mOnWebScrapeListener = onWebScrapeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.web_scrape_list, parent, false);
        return new ViewHolder(v,mOnWebScrapeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WebScrapeItem item = recyclerViewWeb.get(position);

        holder.getTextView().setText(item.getItemName());
        Picasso.get().load(item.getImageURL()).into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        if (recyclerViewWeb!= null) {
            return recyclerViewWeb.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View view;
        public final TextView itemName;
        public final ImageView image;

        OnWebScrapeListener onWebScrapeListener;

        public ViewHolder(View view, OnWebScrapeListener onWebscrapeListener) {
            super(view);
            this.view = view;
            itemName = view.findViewById(R.id.webText);
            image = view.findViewById(R.id.webImage);
        }

        public TextView getTextView(){
            return itemName;
        }

        public ImageView getImageView(){
            return image;
        }

        @Override
        public void onClick(View view)
        {
            onWebScrapeListener.onWebScrapeClick(getAdapterPosition());
        }
    }

    // detect clicking WebScrapes
    public interface OnWebScrapeListener{
        void onWebScrapeClick(int position);
    }
}