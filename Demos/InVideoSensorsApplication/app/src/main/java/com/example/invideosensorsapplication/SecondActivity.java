package com.example.invideosensorsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {

    TextView textViewX, textViewY, textViewZ, textViewLight, textViewGPS;
    private SensorManager mySensorManager;
    private Sensor sensor1, sensor2;
    LocationManager myLocationManager;
    LocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);
        textViewLight = findViewById(R.id.textViewLight);
        textViewGPS = findViewById(R.id.textViewGPS);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor1 = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorManager.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_FASTEST);
        sensor2 = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mySensorManager.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);

        myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i("SENSORAPP", "Provider enabled: " + myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            Log.i("SENSORAPP", "Location enabled: " + myLocationManager.isLocationEnabled());
        } else {
            //do soemthing about it
            Log.i("SENSORAPP", "Location/Provider disabled!");
        }
        myLocationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                textViewGPS.setText("GPS: \n" + location.getLongitude() + "\n" + location.getLatitude());
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 13:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, myLocationListener);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Log.i("SENSORAPP", "onSensorChanged");
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
          textViewX.setText("X: "+sensorEvent.values[0]);
          textViewY.setText("Y: "+sensorEvent.values[1]);
          textViewZ.setText("Z: "+sensorEvent.values[2]);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            textViewLight.setText("Light: "+sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not used at the moment
    }
}