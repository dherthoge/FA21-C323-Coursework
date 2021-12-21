package com.example.project6dylanherthoge;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.project6dylanherthoge.databinding.ActivityMapsBinding;

import java.util.ArrayList;

/**
 * Displays a set of markers on a Google Map.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private EventManager eventManager;
    private ArrayList<Event> events;

    /**
     * Sets up the Google Map and gets a list of Events to display on the map.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                          may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the list of events from the EventManager
        eventManager = EventManager.getInstance(getApplicationContext());
        events = eventManager.getEvents();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set UI settings
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setAllGesturesEnabled(true);
        settings.setCompassEnabled(true);

        /*
         Iterate through the events
            - adds a marker for each event
            - uses LatLngBounds.Builder to ensure all markers are visible on the screen
         */
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for (Event e : events) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getDate()).visible(true));
            boundsBuilder.include(new LatLng(e.getLatitude(), e.getLongitude()));
        }

        // determine where to move the camera
        LatLngBounds bounds = boundsBuilder.build();
        int padding = 150;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // move the camera
        mMap.animateCamera(cameraUpdate);


    }
}