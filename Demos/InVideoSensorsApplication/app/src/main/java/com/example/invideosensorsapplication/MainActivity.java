package com.example.invideosensorsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private SensorManager mySensorManager;
    private Map<Integer, String> sensorTypes = new HashMap<>();{
        sensorTypes.put(Sensor.TYPE_ACCELEROMETER, "TYPE_ACCELEROMETER");
        sensorTypes.put(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED, "TYPE_ACCELEROMETER_UNCALIBRATED");
        sensorTypes.put(Sensor.TYPE_AMBIENT_TEMPERATURE, "TYPE_AMBIENT_TEMPERATURE");
        sensorTypes.put(Sensor.TYPE_DEVICE_PRIVATE_BASE, "TYPE_DEVICE_PRIVATE_BASE");
        sensorTypes.put(Sensor.TYPE_GAME_ROTATION_VECTOR, "TYPE_GAME_ROTATION_VECTOR");
        sensorTypes.put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, "TYPE_GEOMAGNETIC_ROTATION_VECTOR");
        sensorTypes.put(Sensor.TYPE_GRAVITY, "TYPE_GRAVITY");
        sensorTypes.put(Sensor.TYPE_GYROSCOPE, "TYPE_GYROSCOPE");
        sensorTypes.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, "TYPE_GYROSCOPE_UNCALIBRATED");
        sensorTypes.put(Sensor.TYPE_HEART_BEAT, "TYPE_HEART_BEAT");
        sensorTypes.put(Sensor.TYPE_HEART_RATE, "TYPE_HEART_RATE");
        sensorTypes.put(Sensor.TYPE_LIGHT, "TYPE_LIGHT");
        sensorTypes.put(Sensor.TYPE_LINEAR_ACCELERATION, "TYPE_LINEAR_ACCELERATION");
        sensorTypes.put(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT, "TYPE_LOW_LATENCY_OFFBODY_DETECT");
        sensorTypes.put(Sensor.TYPE_MAGNETIC_FIELD, "TYPE_MAGNETIC_FIELD");
        sensorTypes.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, "TYPE_MAGNETIC_FIELD_UNCALIBRATED");
        sensorTypes.put(Sensor.TYPE_MOTION_DETECT, "TYPE_MOTION_DETECT");
        sensorTypes.put(Sensor.TYPE_POSE_6DOF, "TYPE_POSE_6DOF");
        sensorTypes.put(Sensor.TYPE_PRESSURE, "TYPE_PRESSURE");
        sensorTypes.put(Sensor.TYPE_RELATIVE_HUMIDITY, "TYPE_RELATIVE_HUMIDITY");
        sensorTypes.put(Sensor.TYPE_PROXIMITY, "TYPE_PROXIMITY");
        sensorTypes.put(Sensor.TYPE_ROTATION_VECTOR, "TYPE_ROTATION_VECTOR");
        sensorTypes.put(Sensor.TYPE_SIGNIFICANT_MOTION, "TYPE_SIGNIFICANT_MOTION");
        sensorTypes.put(Sensor.TYPE_STATIONARY_DETECT, "TYPE_STATIONARY_DETECT");
        sensorTypes.put(Sensor.TYPE_STEP_COUNTER, "TYPE_STEP_COUNTER");
        sensorTypes.put(Sensor.TYPE_STEP_DETECTOR, "TYPE_STEP_DETECTOR");    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textViewSensor);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mySensorList = mySensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder message = new StringBuilder();
        message.append("Sensors on this device are : \n\n");
        for (Sensor oneSensor:mySensorList) {
            message.append("Name:" + oneSensor.getName() + "Type:" + sensorTypes.get(oneSensor.getType()) + "\n\n");
        }
        textView.setText(message);
    }

    public void goToSecondActivity(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }
}