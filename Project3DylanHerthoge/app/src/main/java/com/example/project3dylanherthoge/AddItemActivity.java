package com.example.project3dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        registerCheckBoxClicks();
    }

    private void registerCheckBoxClicks() {
        findViewById(R.id.lowPriorityChkBox).setOnClickListener(this::checkBoxClicked);
        findViewById(R.id.highPriorityChkBox).setOnClickListener(this::checkBoxClicked);
    }

    public void checkBoxClicked(View v) {
        if (v.getId() == R.id.lowPriorityChkBox) ((CheckBox) findViewById(R.id.highPriorityChkBox)).setChecked(false);
        else ((CheckBox) findViewById(R.id.lowPriorityChkBox)).setChecked(false);
    }

    public void addBtnClicked(View view) {

        EditText titleEdtTxt = findViewById(R.id.titleEdtTxt);
        EditText dateEdtTxt = findViewById(R.id.dateEdtTxt);
        CheckBox lowPriorityChkBox = findViewById(R.id.lowPriorityChkBox);
        CheckBox highPriorityChkBox = findViewById(R.id.highPriorityChkBox);
        String titleInput = titleEdtTxt.getText().toString();
        String dateInput = dateEdtTxt.getText().toString();


        if (titleInput.isEmpty()) {
            Toast.makeText(this, "You must enter a title!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!lowPriorityChkBox.isChecked() && !highPriorityChkBox.isChecked()) {
            Toast.makeText(this, "You must select a priority level!", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("TITLE", titleInput);
        myIntent.putExtra("DATE", dateInput);
        if (lowPriorityChkBox.isChecked()) myIntent.putExtra("PRIORITY", "LOW");
        else myIntent.putExtra("PRIORITY", "HIGH");
        setResult(Activity.RESULT_OK,myIntent);
        finish();
    }
}