package com.example.project6dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A class to display a list of Events.
 */
public class EventListActivity extends AppCompatActivity implements DatePicker.Observer, RecyclerAdapter.DeleteObserver {

    private String filter;
    private TextView tvFilterDate;
    private Geocoder geocoder;
    private EventManager eventManager;
    private ArrayList<Event> events;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    /**
     * Initializes Objects needed for the Event List.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                          may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        tvFilterDate = findViewById(R.id.tv_filter_date);

        geocoder = new Geocoder(this);

        eventManager = EventManager.getInstance(getApplicationContext());
        events = eventManager.getEvents();

        recyclerView = findViewById(R.id.recycler_view_events);
        setAdapter();
    }

    /**
     * Creates a RecyclerAdapter to manage list items.
     */
    private void setAdapter() {
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), events, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Starts a DatePicker to get a date to filter by.
     * @param view The clicked View
     */
    public void filterByDate(View view) {
        DialogFragment datePicker = DatePicker.getInstance(this);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Clears the filter and displays all Events.
     * @param view The clicked View
     */
    public void clearFilter(View view) {
        tvFilterDate.setText("");
        events.clear();

        events = eventManager.getEvents();
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), events, this));
    }

    /**
     * Starts a Google Maps activity to display Event locations.
     * @param view The clicked View
     */
    public void displayLocations(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    /**
     * Sets the filter date and removes list items that do not fit.
     * @param year A year
     * @param month An integer 1-12
     * @param day An integer 1-31 depending on the month
     */
    @Override
    public void newDate(int year, int month, int day) {

        filter = day + "/" + month + "/" + year;
        tvFilterDate.setText(filter);

        events.clear();
        events = eventManager.filter(filter);
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), events, this));
    }

    /**
     * Removes the Event at the given position.
     * @param position The index of the Event to remove
     */
    @Override
    public void eventDeleted(int position) {
        Event eventToDelete = events.get(position);
        eventManager.removeEvent(eventToDelete.getDate(), eventToDelete.getEventInformation(), eventToDelete.getLatitude(), eventToDelete.getLongitude());
        events = eventManager.getEvents();
        // I could also create a new method in RecyclerAdapter to update it's events
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), events, this));
    }
}