package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Sets the Welcome Message with the user's Name
        Intent fromSignUp = getIntent();
        String recvdName = fromSignUp.getStringExtra("NAME");
        TextView welcomeMessageTxtView = findViewById(R.id.welcomeMessageTxtView);
        Log.i("NAME", "Hello, " + recvdName + "!");
        welcomeMessageTxtView.setText(recvdName);

        // Sets the Greeting Message with the user's Name
        TextView greetingTxtView = findViewById(R.id.greetingTxtView);

        int hourOfDay = Calendar.HOUR_OF_DAY;
        if (hourOfDay <= 6) greetingTxtView.setText("Good Morning");
        else if (hourOfDay <= 12) greetingTxtView.setText("Good Afternoon");
        else if (hourOfDay <= 12) greetingTxtView.setText("Good Evening");
        else greetingTxtView.setText("Good Night");
    }

    /**
     * Starts BmiCalculatorActivity.
     * @param view calculateBmiIntentBtn is passed as an argument when clicked
     */
    public void toBmiCalculatorActivity(View view) {
        startActivity(new Intent(this, BmiCalculatorActivity.class));
    }
}