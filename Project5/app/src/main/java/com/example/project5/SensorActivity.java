package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * The main Activity of the application. Displays sensor/GPS data and launches GestureActivity.
 */
public class SensorActivity extends AppCompatActivity implements SensorEventListener, GestureDetector.OnGestureListener {

    // Global variables for storing various objects
    private TextView textCity, textState, textLight, textPressure;
    private Button btnGesturePlay;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor lightSensor, pressureSensor;
    private LocationListener locationListener;
    private Geocoder geocoder;
    private GestureDetector gestureDetector;


    /**
     * Initializes all View variables and sets up the light sensor, pressure sensor, and location
     * manager.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize global variables
        textCity = findViewById(R.id.textCity);
        textState = findViewById(R.id.textState);
        textLight = findViewById(R.id.textLight);
        textPressure = findViewById(R.id.textPressure);
        btnGesturePlay = findViewById(R.id.btnGesturePlay);
        geocoder = new Geocoder(this);

        // Detects a fling on the button
        btnGesturePlay.setOnTouchListener(new View.OnTouchListener() {
            /**
             * Passes the MotionEvent to gestureDetector.
             * @param view The View the event originated from
             * @param motionEvent The user's MotionEvent
             * @return true if the event is consumed, else false
             */
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector != null) gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        // Gets instance of sensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        // Determines if the device has the required sensors, tells user if it lacks them
        if (lightSensor == null) textLight.setText("Light: You do not have a light sensor!");
        if (pressureSensor == null) textPressure.setText("Pressure: You do not have a barometer!");

        // Setup location manager and listener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            /**
             * Asks the user for location permission.
             * @param provider
             */
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            /**
             * Updates the displayed location.
             * @param location The user's location
             */
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                try {
                    List<Address> address = geocoder.getFromLocation(lat, lon, 1);
                    textCity.setText("City: " + address.get(0).getLocality());
                    textState.setText("State: " + address.get(0).getAdminArea());
                } catch (Exception e) {e.printStackTrace();}
            }
        };

        // Set up gesture detector
        gestureDetector = new GestureDetector(this, this);


        // Check if location permissions are granted. If they aren't, ask
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 42);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
        }


    }

    /**
     * Registers listeners for the needed sensors.
     */
    @Override
    protected void onResume() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }

    /**
     * Unregisters listeners for the needed sensors.
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);

        super.onPause();
    }

    /**
     * Determines what permissions the user has granted the application and requests updates from
     * from the managers of the granted permissions
     * @param requestCode Identifies what result is given
     * @param permissions The permissions requested
     * @param grantResults The permissions granted.
     */
    @SuppressLint({"MissingSuperCall", "MissingPermission"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 42:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
                else {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 42);
                }
                break;
        }
    }

    /**
     * Changes the displayed value of the given sensor.
     * @param sensorEvent The change of the sensor's value
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
            textLight.setText("Light: " + sensorEvent.values[0]);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE)
            textPressure.setText("Pressure: " + sensorEvent.values[0]);
    }

    /**
     * Starts GestureActivity.
     * @param motionEvent The first down motion event that started the fling.
     * @param motionEvent1 The move motion event that triggered the current onFling.
     * @param v The velocity of this fling measured in pixels per second along the x axis.
     * @param v1 The velocity of this fling measured in pixels per second along the y axis.
     * @return
     */
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        startActivity(new Intent(this, GestureActivity.class));
        return false;
    }



    // Methods that needed to be overridden but have no implementation
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}