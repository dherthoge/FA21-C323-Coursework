package com.c323proj10.dherthog;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the lifecycle of a RecyclerView
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    /**
     * Creates a RecyclerAdapter
     * @param applicationContext The context of the application
     */
    public RecyclerAdapter(Context applicationContext) {
    }

    /**
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemName;
        private ImageButton imgBtnFavorite;
        private int position;

        /**
         * Creates a holder for the Event's information.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            tvItemName = view.findViewById(R.id.tv_item_name);
            imgBtnFavorite = view.findViewById(R.id.imgBtn_favorite);
            imgBtnFavorite.setOnClickListener(new View.OnClickListener() {
                /**
                 * Deletes the chosen item.
                 * @param view The clicked button
                 */
                @Override
                public void onClick(View view) {

                }
            });
        }

        /**
         * Sets the position of the holder's Event
         * @param position The position of the holder's Event
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
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Binds an Event to the given ViewHolder
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {

//        Event event = events.get(position);
//        holder.tvListItemEventInformation.setText(event.getEventInformation());
//        holder.tvListItemDate.setText(event.getDate());
//        try {
//            holder.tvListItemAddress.setText(geocoder.getFromLocation(event.getLatitude(), event.getLongitude(), 1).get(0).getAddressLine(0).toString());
//        } catch (IOException e) {
//            Log.i("onBindViewHolder", "Address not found for given coordinates!");
//            e.printStackTrace();
//        }
//        holder.setPosition(position);
    }

    /**
     * Gets the number of items in the RecyclerView.
     * @return The number of items in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return 0;
    }
}
