package com.example.project3dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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

        Intent myIntent = new Intent(this, MainActivity.class);
        EditText editText = findViewById(R.id.editText_sa);
        String myInput = editText.getText().toString();
        myIntent.putExtra("NEWDATA", myInput);
        setResult(Activity.RESULT_OK,myIntent);
        finish();
    }
}