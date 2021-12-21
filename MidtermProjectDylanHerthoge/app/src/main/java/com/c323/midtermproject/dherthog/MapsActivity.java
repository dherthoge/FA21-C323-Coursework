package com.c323.midtermproject.dherthog;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.c323.midtermproject.dherthog.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * An Activity to display a selected Item.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Location currentLocation;

    /**
     * Creates a Map.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available. Sets map setting options and triggers marker creation.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set UI settings
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setAllGesturesEnabled(true);
        settings.setCompassEnabled(true);


        getCurrentLocation();
        createOverlay();
    }

    /**
     * Calculates the distance between two points on Earth (takes into account the curvature of
     * Earth's surface
     * @param lat1 The latitude of the first point (Must be a double between -180 and 180)
     * @param lat2 The latitude of the second point (Must be a double between -180 and 180)
     * @param lon1 The longitude of the first point (Must be a double between -90 and 90
     * @param lon2 The longitude of the second point (Must be a double between -90 and 90
     * @return
     */
    private double distanceBetweenCoordinates(double lat1, double lat2, double lon1, double lon2) {
        /* This is from https://www.geeksforgeeks.org/program-distance-two-points-earth/#:~:text=For%20this%20divide%20the%20values,is%20the%20radius%20of%20Earth.
         * I renamed the variables to make it obvious what's going on, but I didn't change the logic
         */
        // Convert degrees to radian
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double distLat = lat2 - lat1;
        double distLon = lon2 - lon1;
        double a = Math.pow(Math.sin(distLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(distLon / 2),2);

        // The
        double partialCircumference = 2 * Math.asin(Math.sqrt(a));

        // Radius in miles
        double earthRadius = 3956;

        // calculate the distance
        return(partialCircumference * earthRadius);
    }

    /**
     * Creates an overlay to display information about the distance between the selected Item and
     * the User's current location
     */
    private void createOverlay() {

        // This method gets
        if (currentLocation == null)
            return;

        double curLatitude = currentLocation.getLatitude();
        double curLongitude = currentLocation.getLongitude();

        // Get the information about the selected Item from Intent
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");
        String location = extras.getString("LOCATION");
        float latitude = extras.getFloat("LATITUDE");
        float longitude = extras.getFloat("LONGITUDE");

        // Make LatLngs for each point
        LatLng itemLatLng = new LatLng(latitude, longitude);
        LatLng curLatLng = new LatLng(curLatitude, curLongitude);

        // Create bounds for the camera
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();

        // Marker for Item
        mMap.addMarker(new MarkerOptions().position(itemLatLng).title(name + "\n" + location).visible(true));
        boundsBuilder.include(itemLatLng);

        // Marker for current location
        mMap.addMarker(new MarkerOptions().position(curLatLng).visible(true));
        boundsBuilder.include(curLatLng);

        // determine where to move the camera
        LatLngBounds bounds = boundsBuilder.build();
        int padding = 150;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // move the camera
        mMap.animateCamera(cameraUpdate);

        // Find the distance between the markers
        double distance = distanceBetweenCoordinates(latitude, curLatitude, longitude, curLongitude);

        // Display the distance between the markers

        ((TextView) findViewById(R.id.tv_distance)).setText((String.format("Distance: %.2f mi", distance)));

        // Draw line between the markers
        mMap.addPolyline(new PolylineOptions()
                .add(itemLatLng, curLatLng)
                .width(5)
                .color(Color.BLUE));
    }

    /**
     * Determines what permissions the User has allowed the application to access.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 13:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getCurrentLocation();
                break;
            default:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
                Toast.makeText(this, "You must grant location for Map Display to function properly!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Gets the user's current location an stores it as a global variable.
     */
    private void getCurrentLocation() {

        // Create a loop to force the user to give permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                createOverlay();

                locationManager.removeUpdates(this);
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 1, locationListener);
    }
}