package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Set's the activity's View.
     * @param savedInstanceState a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    /**
     * Starts LandingActivity.
     * @param view signUpSignUpBtn is passed when clicked
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

        // If the user did not enter the same name Toast them and do not proceed
        if (name == null || name.isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the user did not enter the same username Toast them and do not proceed
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the user did not enter the same password Toast them and do not proceed
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The passwords you entered did not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store the information the user used to sign up
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("NAME", name);
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.commit();
        String pwd = userInfo.getString("PASSWORD", "None");

        // Move to the next activity and pass the user's Name
        Intent toLandingActivity = new Intent(this, LandingActivity.class);
        toLandingActivity.putExtra("NAME", name);
        startActivity(toLandingActivity);
    }

    /**
     * Starts the SignInActivity.
     *
     * @param view signUpSignInBtn is passed when clicked
     */
    public void signInClicked(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }
}