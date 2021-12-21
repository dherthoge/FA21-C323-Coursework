package com.example.project6dylanherthoge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class NewEventActivity extends AppCompatActivity implements DatePicker.Observer {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvDate, tvLocation;
    private EditText etEventInformation;
    private Geocoder geocoder;
    private Address address;
    private EventManager eventManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;

    /**
     * Initializes Objects needed for the Event List.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                          may be null.
     */
    @SuppressLint("MissingPermission") // Permission is requested before location accessed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        tvDate = findViewById(R.id.tv_date);
        tvLocation = findViewById(R.id.tv_location);
        etEventInformation = findViewById(R.id.et_event_information);

        eventManager = EventManager.getInstance(getApplicationContext());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
            }
        };

        geocoder = new Geocoder(this);
        address = null;

        requestPermissions();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    /**
     * Determines what permissions the user has granted the application and requests updates from
     * from the managers of the granted permissions
     * @param requestCode Identifies what result is given
     * @param permissions The permissions requested
     * @param grantResults The permissions granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 13:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
                Toast.makeText(this, "You must grant location permissions!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Request location permissions from the user.
     * @return If the user granted the permissions
     */
    public boolean requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
            return false;
        }
        return true;
    }



    // Btn listeners

    /**
     * Gets the location of the user and
     */
    @SuppressLint("MissingPermission") // Permission is requested before location accessed
    public void getCurrentLocation() {

        requestPermissions();
        if (currentLocation != null) {
            try {
            address = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1).get(0);
            tvLocation.setText(address.getAddressLine(0));
            return; // since location updates are working we do not need to get the location from FusedLocationClient
            } catch (Exception e) { e.printStackTrace(); }
        }


        // Only gets location from FusedLocationClient if LocationManager is not working
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            try {
                                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                                tvLocation.setText(address.getAddressLine(0));
                                Log.i("getLocation", "Got the user's location!");
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    }
                });
    }

    /**
     * Adds an Event to the list with the provided information, date, and location.
     * @param view The clicked View
     */
    public void addEvent(View view) {

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.i("addEvent", "Keyboard not open!");
        }

        String tvDateText = tvDate.getText().toString();
        String etEventInformationText = etEventInformation.getText().toString();
        String tvLocationText = tvLocation.getText().toString();
        if (tvLocationText.isEmpty()) {
            Toast.makeText(this, "You must enter a valid location!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etEventInformationText.isEmpty()) {
            Toast.makeText(this, "Event information cannot be blank!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvDateText.isEmpty()) {
            Toast.makeText(this, "You must enter a valid date!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            eventManager.addEvent(new Event(etEventInformationText, tvDateText, address.getLatitude(), address.getLongitude()));
        } catch (Exception e) {
            Toast.makeText(this, "Event not added!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        tvDate.setText("");
        etEventInformation.setText("");
        tvLocation.setText("");
        Toast.makeText(this, "Event added!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets the user's current location.
     * @param view The clicked View
     */
    public void getLocation(View view) {
        getCurrentLocation();
    }

    /**
     * Starts a DatePicker to get a date from the user
     * @param view The clicked View
     */
    public void pickDate(View view) {
        DialogFragment datePicker = DatePicker.getInstance(this);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Starts an Activity to display all stored Events.
     * @param view The clicked View
     */
    public void startEventListActivity(View view) {
        startActivity(new Intent(this, EventListActivity.class));
    }

    /**
     * Sets the displayed date.
     * @param year the selected year
     * @param month the selected month 1-12
     * @param day the selected day of the month (1-31, depending on month)
     */
    @Override
    public void newDate(int year, int month, int day) {
        tvDate.setText(day + "/" + month + "/" + year);
    }
}