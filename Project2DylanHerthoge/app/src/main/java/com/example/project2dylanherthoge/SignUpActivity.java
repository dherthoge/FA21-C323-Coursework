package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    /**
     * Starts LandingActivity.
     * @param view signUpBtn is passed as an argument when clicked
     */
    public void signUpClicked(View view) {

        // Get's references to the input fields
        EditText nameEdtTxt = findViewById(R.id.signUpNameEdtTxt);
        EditText usernameEdtTxt = findViewById(R.id.signUpUsernameEdtTxt);
        EditText passwordEdtTxt = findViewById(R.id.signUpPasswordEdtTxt);
        EditText confirmPasswordEdtTxt = findViewById(R.id.signUpConfirmPasswordEdtTxt);

        // Used for storing prospective users' sign up information
        String name = nameEdtTxt.getText().toString();
        String username = usernameEdtTxt.getText().toString();
        String password = passwordEdtTxt.getText().toString();
        String confirmPassword = confirmPasswordEdtTxt.getText().toString();

        // If the user did not enter the same password Toast them and do not proceed
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The passwords you entered did not match!", Toast.LENGTH_LONG).show();
            Log.i("NonmatchingPasswords", password + ", " + confirmPassword);
            return;
        }

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

    public void signInClicked(View view) {
        System.out.println("hello");
    }
}