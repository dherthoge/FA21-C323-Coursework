package com.example.project2dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BmiCalculatorActivity extends AppCompatActivity {

    /**
     * Set's the activity's View.
     * @param savedInstanceState a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
    }

    /**
     * Calculates BMI of the user and displays the result.
     *
     * @param view calculateBtn is passed when clicked
     */
    public void calculateBMI(View view) {

        // Gets the height and weight of the user
        EditText weightEdtText = findViewById(R.id.weightEdtText);
        EditText heightEdtText = findViewById(R.id.heightEdtText);
        int weight = 0;
        int height = 0;
        try {
            weight = Integer.parseInt(weightEdtText.getText().toString());
            height = Integer.parseInt(heightEdtText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter whole numbers for your height and weight!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculates the user's BMI
        int bmi = 703 * weight / (int) Math.pow(height, 2);

        // Displays the user's BMI
        TextView bmiValueTxtView = findViewById(R.id.bmiValueTxtView);
        TextView bmiClassificationTxtView = findViewById(R.id.bmiClassificationTxtView);

        // Displays the categorization of the user's BMI
        bmiValueTxtView.setText("" + bmi);
        if (bmi < 18.5) bmiClassificationTxtView.setText("Underweight");
        else if (bmi < 25) bmiClassificationTxtView.setText("Healthy Weight");
        else if (bmi < 30) bmiClassificationTxtView.setText("Overweight");
        else bmiClassificationTxtView.setText("Obese");
    }
}