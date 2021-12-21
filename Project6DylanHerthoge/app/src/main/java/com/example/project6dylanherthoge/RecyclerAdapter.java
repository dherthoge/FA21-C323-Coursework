package com.example.project6dylanherthoge;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the lifecycle of a RecyclerView
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Geocoder geocoder;
    private ArrayList<Event> events;
    private DeleteObserver deleteObserver;

    /**
     * An interface to give the deleted event to the parent Activity
     */
    public interface DeleteObserver {
        public void eventDeleted(int position);
    }

    /**
     * Creates a RecyclerAdapter
     * @param applicationContext The context of the application
     * @param events The Event to display in the list
     * @param deleteObserver The Observer for item deletion
     */
    public RecyclerAdapter(Context applicationContext, ArrayList<Event> events, DeleteObserver deleteObserver) {
        this.geocoder = new Geocoder(applicationContext);
        this.events = events;
        this.deleteObserver = deleteObserver;
    }

    /**
     * A class to hold list items
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvListItemEventInformation;
        private TextView tvListItemAddress;
        private TextView tvListItemDate;
        private int position;

        /**
         * Creates a holder for the Event's information.
         * @param view The parent View
         */
        public MyViewHolder(View view) {
            super(view);

            tvListItemEventInformation = view.findViewById(R.id.tv_list_item_event_information);
            tvListItemAddress = view.findViewById(R.id.tv_list_item_address);
            tvListItemDate = view.findViewById(R.id.tv_list_item_date);
            view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                /**
                 * Deletes the chosen item.
                 * @param view The clicked button
                 */
                @Override
                public void onClick(View view) {
                    Log.i("btn_delete", "button clicked at index: " + position);

                    deleteObserver.eventDeleted(position);
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

        Event event = events.get(position);
        holder.tvListItemEventInformation.setText(event.getEventInformation());
        holder.tvListItemDate.setText(event.getDate());
        try {
            holder.tvListItemAddress.setText(geocoder.getFromLocation(event.getLatitude(), event.getLongitude(), 1).get(0).getAddressLine(0).toString());
        } catch (IOException e) {
            Log.i("onBindViewHolder", "Address not found for given coordinates!");
            e.printStackTrace();
        }
        holder.setPosition(position);
    }

    /**
     * Gets the number of items in the RecyclerView.
     * @return The number of items in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return events.size();
    }
}
