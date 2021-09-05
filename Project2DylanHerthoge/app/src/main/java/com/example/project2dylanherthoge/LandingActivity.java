package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class LandingActivity extends AppCompatActivity {

    /**
     * Set's the activity's View. Get's the name
     * @param savedInstanceState a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Sets the Welcome Message with the user's Name
        Intent fromSignUp = getIntent();
        String recvdName = fromSignUp.getStringExtra("NAME");
        TextView welcomeMessageTxtView = findViewById(R.id.welcomeMessageTxtView);
        welcomeMessageTxtView.setText("Hello, " + recvdName + "!");

        // Determines greeting based the current hour of the day
        int hourOfDay = Calendar.HOUR_OF_DAY;
        String greeting = "";
        if (hourOfDay <= 6) greeting = "Good Morning";
        else if (hourOfDay <= 12) greeting = "Good Afternoon";
        else if (hourOfDay <= 18) greeting = "Good Evening";
        else greeting = "Good Night";

        // Displays greeting
        TextView greetingTxtView = findViewById(R.id.greetingTxtView);
        greetingTxtView.setText(greeting);
    }

    /**
     * Starts BmiCalculatorActivity.
     * @param view calculateBmiIntentBtn is passed when clicked
     */
    public void toBmiCalculatorActivity(View view) {
        startActivity(new Intent(this, BmiCalculatorActivity.class));
    }
}