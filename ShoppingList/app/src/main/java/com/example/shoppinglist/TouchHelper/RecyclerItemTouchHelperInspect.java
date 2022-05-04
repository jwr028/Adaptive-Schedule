package com.example.shoppinglist.TouchHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Adapter.InspectItemsAdapter;
import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.ItemsFragment;
import com.example.shoppinglist.ListsActivity;
import com.example.shoppinglist.PlaceholderActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.WebScrape;

// USED IN INSPECT LIST ITEM fragment
public class RecyclerItemTouchHelperInspect extends ItemTouchHelper.SimpleCallback{

    //private ToDoAdapter adapter;
    private InspectItemsAdapter itemsAdapter;

    public RecyclerItemTouchHelperInspect(InspectItemsAdapter itemsAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.itemsAdapter = itemsAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        // used for specify
        final int position = viewHolder.getAdapterPosition();
         String itemName = itemsAdapter.todoList.get(position).getTask();

        if (direction == ItemTouchHelper.LEFT) {
            // SWIPE LEFT
            AlertDialog.Builder builder = new AlertDialog.Builder(itemsAdapter.getContext());
            builder.setTitle("Specify Item");
            builder.setMessage("Search for specific item?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // INSERT SPECIFY HERE ///////////////////////////////////////////////////////////////
                            itemsAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                            //itemName = ItemsFragment.itemList.get(position).getTask();
                            Intent intent = new Intent(itemsAdapter.getContext(), WebScrape.class);
                            intent.putExtra("ItemName", itemName);
                            itemsAdapter.getContext().startActivity(intent);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    itemsAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // SWIPE RIGHT
            AlertDialog.Builder builder = new AlertDialog.Builder(itemsAdapter.getContext());
            builder.setTitle("Specify Item");
            builder.setMessage("Search for specific item?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // INSERT SPECIFY HERE ///////////////////////////////////////////////////////////////
                            itemsAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                            //itemName = ItemsFragment.itemList.get(position).getTask();
                            Intent intent = new Intent(itemsAdapter.getContext(), WebScrape.class);
                            intent.putExtra("ItemName", itemName);
                            itemsAdapter.getContext().startActivity(intent);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    itemsAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(itemsAdapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(itemsAdapter.getContext(), R.color.colorPrimaryDark));
        } else {
            icon = ContextCompat.getDrawable(itemsAdapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(itemsAdapter.getContext(), R.color.colorPrimaryDark));
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }

}
