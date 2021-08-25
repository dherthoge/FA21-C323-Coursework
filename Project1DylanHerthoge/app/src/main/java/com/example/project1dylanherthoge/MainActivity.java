package com.example.project1dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Converts minutes from the editText to seconds and sets the text of the textView.
     *
     * @param view  the view (the 'convert' button) that was clicked
     */
    public void convertClicked(View view) {

        /* The ID of the editText the user inputs minutes */
        EditText minutesEdtTxtId = findViewById(R.id.minutesEdt);
        /* The ID of the textView the program outputs the conversion */
        TextView secondsTxtViewId = findViewById(R.id.secondsTxtView);

        /* Gets the user's input to parse and integer from. If the input is malformed, outputs "Error" */
        try {

            int minutes = Integer.parseInt(minutesEdtTxtId.getText().toString());
            int seconds = minutes*60;
            secondsTxtViewId.setText(seconds + " seconds");
        } catch (NumberFormatException nfe) {
            secondsTxtViewId.setText("Error");
        }
    }
}