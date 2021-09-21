package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener, GestureDetector.OnGestureListener {

    private TextView textCity, textState, textLight, textAmbientTemp;
    private Button btnGesturePlay;
    private float xMin, xMax, yMin, yMax;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor lightSensor, ambientTempSensor;
    private LocationListener locationListener;
    private Geocoder geocoder;
    GestureDetector gestureDetector;

    public static class TransparentConstraintLayout extends ConstraintLayout {

        public TransparentConstraintLayout(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
//            super.onInterceptTouchEvent(ev);
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textCity = findViewById(R.id.textCity);
        textState = findViewById(R.id.textState);
        textLight = findViewById(R.id.textLight);
        textAmbientTemp = findViewById(R.id.textAmbientTemp);
        btnGesturePlay = findViewById(R.id.btnGesturePlay);
        geocoder = new Geocoder(this);


        // Some of this instruction was taken from https://stackoverflow.com/a/55308560
        btnGesturePlay.post(new Runnable() {
            @Override
            public void run() {

                int[] location = new int[2];
                btnGesturePlay.getLocationOnScreen(location);

                xMin = location[0];
                xMax = xMin + btnGesturePlay.getWidth();
                yMin = location[1];
                yMax = yMin + btnGesturePlay.getHeight();

                Log.i("dimensions", xMin + " " + xMax + " " + yMin + " " + yMax);
            }
        });
        btnGesturePlay.setEnabled(false);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        ambientTempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTempSensor, SensorManager.SENSOR_DELAY_NORMAL);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 42);
            return;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 0, locationListener);
        }


        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTempSensor, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);

        super.onPause();
    }

    @SuppressLint({"MissingSuperCall", "MissingPermission"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 42:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
                else {
                    textCity.setText("Location permission not granted!");
                    textState.setText("Location permission not granted!");
                }
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
            textLight.setText("Light: " + sensorEvent.values[0]);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
            textAmbientTemp.setText("Temp: " + sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        startActivity(new Intent(this, GestureActivity.class));
        Log.i("onFling", "button flung");
        return false;
    }




    // Unused imports
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