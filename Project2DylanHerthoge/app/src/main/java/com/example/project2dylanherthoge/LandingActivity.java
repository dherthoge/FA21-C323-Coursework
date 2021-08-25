package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    /**
     * Starts BmiCalculatorActivity.
     * @param view calculateBmiIntentBtn is passed as an argument when clicked
     */
    public void toBmiCalculatorActivity(View view) {
        startActivity(new Intent(this, BmiCalculatorActivity.class));
    }
}