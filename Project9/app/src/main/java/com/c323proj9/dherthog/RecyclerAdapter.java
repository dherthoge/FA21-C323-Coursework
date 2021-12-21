package com.c323proj9.dherthog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Manages the lifecycle of a RecyclerView
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Item> items;
    private EventObserver eventObserver;

    public interface EventObserver {
        public void editClicked(Item item);
        public void deleteClicked(Item item);
    }

    /**
     * Creates a RecyclerAdapter
     * @param items The Item to display in the list
     * @param eventObserver A listener for edit or delete button events
     */
    public RecyclerAdapter(ArrayList<Item> items, EventObserver eventObserver) {
        this.items = items;
        this.eventObserver = eventObserver;
    }

    /**
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryIconIv;
        private TextView expenseTitleTv, expenseCostTv, expenseCategoryTv, expenseDateTv;
        private ImageButton editItemBtn, deleteItemBtn;
        private int position;

        /**
         * Creates a holder for the Item's information.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            categoryIconIv = view.findViewById(R.id.iv_category_icon);
            expenseTitleTv = view.findViewById(R.id.tv_expense_title);
            expenseCostTv = view.findViewById(R.id.tv_expense_cost);
            expenseCategoryTv = view.findViewById(R.id.tv_expense_category);
            expenseDateTv = view.findViewById(R.id.tv_expense_date);
            editItemBtn = view.findViewById(R.id.btn_edit_item);
            deleteItemBtn = view.findViewById(R.id.btn_delete_item);
        }

        /**
         * Sets the position of the holder's Item
         * @param position The position of the holder's Item
         */
        public void setPosition(int position) {
            this.position = position;
        }
    }

    /**
     * Inflates the View hierarchy and returns it.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The inflated View
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Binds an Item to the given ViewHolder
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Item item = items.get(position);
        holder.expenseTitleTv.setText(item.getExpense());
        holder.expenseCostTv.setText(String.format("$%.2f", item.getCost()));
        holder.expenseDateTv.setText(item.getDate());

        // Capitalizes just the first letter of the category
        String temp = item.getCategory().toLowerCase(Locale.ROOT);
        holder.expenseCategoryTv.setText(temp.substring(0, 1).toUpperCase() + temp.substring(1));


        ImageView categoryIconIv = holder.categoryIconIv;
        switch (item.getCategory()) {
            case "FOOD":
                categoryIconIv.setImageResource(R.drawable.food);
                break;
            case "TRANSPORTATION":
                categoryIconIv.setImageResource(R.drawable.transportation);
                break;
            case "HOUSING":
                categoryIconIv.setImageResource(R.drawable.housing);
                break;
            case "ENTERTAINMENT":
                categoryIconIv.setImageResource(R.drawable.entertainment);
                break;

        }


        holder.editItemBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies the EventObserver edit was clicked.
             * @param view The clicked button
             */
            @Override
            public void onClick(View view) {
                eventObserver.editClicked(item);
            }
        });
        holder.deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies the EventObserver delete was clicked.
             * @param view The clicked button
             */
            @Override
            public void onClick(View view) {
                eventObserver.deleteClicked(item);
            }
        });

        holder.setPosition(position);
    }

    /**
     * Gets the number of items in the RecyclerView.
     * @return The number of items in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return items.size();
    }
}
