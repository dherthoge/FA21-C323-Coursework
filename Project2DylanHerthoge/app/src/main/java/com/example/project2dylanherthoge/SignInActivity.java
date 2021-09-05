package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    /**
     * Set's the activity's View. Get's the name
     * @param savedInstanceState a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    /**
     * Determines if the user entered the correct login information and starts LandingActivity if
     * the login was correct.
     * @param view signInSignInBtn is passed when clicked
     */
    public void signInClicked(View view) {

        // Get's references to the input fields
        EditText usernameEdtTxt = findViewById(R.id.signInUsernameEdtTxt);
        EditText passwordEdtTxt = findViewById(R.id.signInPasswordEdtTxt);

        // Used for storing prospective users' sign up information
        String username = usernameEdtTxt.getText().toString();
        String password = passwordEdtTxt.getText().toString();

        // Retrieve the user's stored sign in info
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String realUsername = userInfo.getString("USERNAME", "None");
        String realPassword = userInfo.getString("PASSWORD", "None");

        if (!username.equals(realUsername)) {
            Toast.makeText(this, "You did not enter the correct username!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(realPassword)) {
            Toast.makeText(this, "You did not enter the correct password!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, LandingActivity.class));
    }
}