package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void signInClicked(View view) {

        // Get's references to the input fields
        EditText usernameEdtTxt = findViewById(R.id.signInUsernameEdtTxt);
        EditText passwordEdtTxt = findViewById(R.id.signInPasswordEdtTxt);

        // Used for storing prospective users' sign up information
        String username = usernameEdtTxt.getText().toString();
        String password = passwordEdtTxt.getText().toString();

        // Store the information the user used to sign up
        SharedPreferences userDatabase = getSharedPreferences("userDatabase", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDatabase.edit();
        editor.putString("NAME", name);
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.commit();
        Log.i("EditorCommit", "Name: " + name + ", Username: " + username + ", Password: " + password);

        // Move to the next activity and pass the user's Name
        Intent toLandingActivity = new Intent(this, LandingActivity.class);
        toLandingActivity.putExtra("NAME", name);
        startActivity(toLandingActivity);
    }
}