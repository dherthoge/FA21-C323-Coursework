package com.c323.midtermproject.dherthog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Manages the lifecycle of a RecyclerView
 */
public class RecyclerAdapterFavorites extends RecyclerView.Adapter<RecyclerAdapterFavorites.MyViewHolder> {

    private Context applicationContext;
    private ArrayList<Item> items;
    private ActionObserver actionObserver;
    private ItemManager itemManager;

    /**
     * Creates a RecyclerAdapterFavorites
     * @param applicationContext The context of the application
     * @param actionObserver The Observer for item deletion
     */
    public RecyclerAdapterFavorites(Context applicationContext, ActionObserver actionObserver) {
        this.applicationContext = applicationContext;
        this.actionObserver = actionObserver;

        itemManager = ItemManager.getInstance(applicationContext);
        populateItems();
    }

    /**
     * Populates the items ArrayList with favorite Items.
     */
    private void populateItems() {
        ItemManager itemManager = ItemManager.getInstance(applicationContext);
        items = itemManager.getFavoriteItemsCopy();
    }

    /**
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        // Non-unique fields for card view
        private ImageView imageIv;
        private TextView nameTv;
        private TextView timeToVisitTv;
        private TextView locationTv;
        private Button removeFromFavoritesBtn;
        private Button mapDisplayBtn;

        // The position of the CardView
        private int position;

        /**
         * Creates a holder for the Item.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            imageIv = view.findViewById(R.id.cv_iv_favorite_image);
            nameTv = view.findViewById(R.id.cv_tv_favorite_name);
            timeToVisitTv = view.findViewById(R.id.cv_tv_favorite_time_to_visit);
            locationTv = view.findViewById(R.id.cv_tv_favorite_location);
            removeFromFavoritesBtn = view.findViewById(R.id.cv_btn_favorite_favorite);
            mapDisplayBtn = view.findViewById(R.id.cv_btn_favorite_map_display);

            view.setOnTouchListener(new OnTouchListener(position, applicationContext, actionObserver));
            removeFromFavoritesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemManager.removeFavorite(items.get(position));
                }
            });
            mapDisplayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionObserver.displayItemOnMap(position);
                }
            });
        }

        /**
         * @param position The position of the MyViewHolder
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite, parent, false);

        return new MyViewHolder(view);
    }

    /**
     * Binds an Item to the given ViewHolder
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setPosition(position);

        // Set non-unique fields
        Item item = items.get(position);
        holder.imageIv.setImageBitmap(item.getBitmap());
        holder.nameTv.setText(item.getName());
        holder.timeToVisitTv.setText(item.getBestTimeToVisit());
        holder.locationTv.setText(item.getLocation());
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
