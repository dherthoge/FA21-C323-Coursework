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
public class RecyclerAdapterCardView extends RecyclerView.Adapter<RecyclerAdapterCardView.MyViewHolder> {

    private Context applicationContext;
    private Category category;
    private ArrayList<Item> items;
    private ActionObserver actionObserver;

    /**
     * Creates a RecyclerAdapter
     * @param applicationContext The context of the application
     * @param actionObserver The Observer for item deletion
     */
    public RecyclerAdapterCardView(Context applicationContext, Category category, ActionObserver actionObserver) {
        this.applicationContext = applicationContext;
        this.category = category;
        this.actionObserver = actionObserver;

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
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        // Non-unique fields for card view
        private ImageView imageIv;
        private TextView nameTv;
        private TextView timeToVisitTv;
        private TextView locationTv;
        private Button addToFavoritesBtn;
        private Button mapDisplayBtn;

        // City specific fields for card view
        private TextView touristSpotsTv;

        // Monument specific fields for card view
        private TextView historyTv;
        private TextView ticketPriceTv;

        // Campsite specific fields for card view
        private TextView nearestLocationTv;

        // The position of the CardView
        private int position;

        /**
         * Creates a holder for the Item's name.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            // Set fields unique to the current category
            switch (category) {
                case CITY:
                    imageIv = view.findViewById(R.id.cv_iv_city_image);
                    nameTv = view.findViewById(R.id.cv_tv_city_name);
                    timeToVisitTv = view.findViewById(R.id.cv_tv_city_time_to_visit);
                    locationTv = view.findViewById(R.id.cv_tv_city_location);
                    addToFavoritesBtn = view.findViewById(R.id.cv_btn_city_favorite);
                    mapDisplayBtn = view.findViewById(R.id.cv_btn_city_map_display);
                    // City specific fields for card view
                    touristSpotsTv = view.findViewById(R.id.cv_tv_city_tourist_spots);
                    break;

                case MONUMENT:
                    imageIv = view.findViewById(R.id.cv_iv_monu_image);
                    nameTv = view.findViewById(R.id.cv_tv_monu_name);
                    timeToVisitTv = view.findViewById(R.id.cv_tv_monu_time_to_visit);
                    locationTv = view.findViewById(R.id.cv_tv_monu_location);
                    addToFavoritesBtn = view.findViewById(R.id.cv_btn_monu_favorite);
                    mapDisplayBtn = view.findViewById(R.id.cv_btn_monu_map_display);
                    // Monument specific fields for card view
                    historyTv = view.findViewById(R.id.cv_tv_monu_history);
                    ticketPriceTv = view.findViewById(R.id.cv_tv_monu_ticket_price);
                    break;

                case CAMPSITE:
                    imageIv = view.findViewById(R.id.cv_iv_campsite_image);
                    nameTv = view.findViewById(R.id.cv_tv_campsite_name);
                    timeToVisitTv = view.findViewById(R.id.cv_tv_campsite_time_to_visit);
                    locationTv = view.findViewById(R.id.cv_tv_campsite_location);
                    addToFavoritesBtn = view.findViewById(R.id.cv_btn_campsite_favorite);
                    mapDisplayBtn = view.findViewById(R.id.cv_btn_campsite_map_display);
                    // Campsite specific fields for card view
                    nearestLocationTv = view.findViewById(R.id.cv_tv_campsite_nearest_location);
                    break;
            }

            view.setOnTouchListener(new OnTouchListener(position, applicationContext, actionObserver));
            addToFavoritesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionObserver.favoriteItem(position);
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

        View view = null;
        switch (category) {
            case CITY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_city, parent, false);
                break;
            case MONUMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_monument, parent, false);
                break;
            case CAMPSITE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_campsite, parent, false);
                break;
        }

        if (view == null) {
            throw new IllegalArgumentException("Category not recognized!");
        }
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
//        holder.mapDisplayBtn.setOnClickListener();

        // Set non-unique fields
        Item item = items.get(position);
        holder.imageIv.setImageBitmap(item.getBitmap());
        holder.nameTv.setText(item.getName());
        holder.timeToVisitTv.setText(item.getBestTimeToVisit());
        holder.locationTv.setText(item.getLocation());
        // Set fields unique to the current category
        switch (category) {
            case CITY:
                City city = (City) item;
                holder.touristSpotsTv.setText(city.getTouristSpots());
                break;
            case MONUMENT:
                Monument monument = (Monument) item;
                holder.historyTv.setText(monument.getHistory());
                holder.ticketPriceTv.setText("" + monument.getTicketPrice());
                break;
            case CAMPSITE:
                Campsite campsite = (Campsite) item;
                holder.nearestLocationTv.setText(campsite.getNearestMetroLocation());
                break;
        }
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
