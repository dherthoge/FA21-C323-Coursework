package com.c323.midtermproject.dherthog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Manages the lifecycle of a RecyclerView
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context applicationContext;
    private Category category;
    private ArrayList<Item> items;
    private ItemDisplayActivity.DisplayOption displayOption;
    private ActionObserver actionObserver;
    private PopupObserver popupObserver;

    /**
     * An interface to give the deleted event to the parent Activity
     */
    public interface PopupObserver {
        public void displayPopup(int position);
    }

    /**
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvListItemEventInformation;

        /**
         * Creates a holder for the Item's name.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            tvListItemEventInformation = view.findViewById(R.id.tv_recycler_view_item_name);
        }
    }

    /**
     * Creates a RecyclerAdapter
     * @param applicationContext The context of the application
     * @param actionObserver The Observer for item deletion
     */
    public RecyclerAdapter(Context applicationContext, ItemDisplayActivity.DisplayOption displayOption,
                           Category category, ActionObserver actionObserver, PopupObserver popupObserver) {
        this.applicationContext = applicationContext;
        this.displayOption = displayOption;
        this.category = category;
        this.actionObserver = actionObserver;
        this.popupObserver = popupObserver;

        populateItems();
    }

    /**
     * Populates the items ArrayList with items from the current category.
     */
    private void populateItems() {
        ItemManager itemManager = ItemManager.getInstance(applicationContext);
        items = new ArrayList<>();
        switch (category) {
            case CITY:
                ArrayList<City> cities = itemManager.getCitiesCopy();
                for (City c: cities)
                    items.add(c);
                break;
            case MONUMENT:
                ArrayList<Monument> monuments = itemManager.getMonumentsCopy();
                for (Monument m: monuments)
                    items.add(m);
                break;
            case CAMPSITE:
                ArrayList<Campsite> campsites = itemManager.getCampsitesCopy();
                for (Campsite c: campsites)
                    items.add(c);
                break;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    /**
     * Binds an Item to the given ViewHolder
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvListItemEventInformation.setText(items.get(position).getName());

        holder.tvListItemEventInformation.setOnTouchListener(new OnTouchListener(position, applicationContext, actionObserver));
        holder.tvListItemEventInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupObserver.displayPopup(holder.getAdapterPosition());
            }
        });
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
