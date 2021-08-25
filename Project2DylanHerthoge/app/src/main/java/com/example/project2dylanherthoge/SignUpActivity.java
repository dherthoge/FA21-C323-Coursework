package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts LandingActivity.
     * @param view signUpBtn is passed as an argument when clicked
     */
    public void toLandingActivity(View view) {
        startActivity(new Intent(this, LandingActivity.class));
    }
}