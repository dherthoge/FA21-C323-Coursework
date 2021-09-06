package com.example.project3dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] titles, dates, priorities;
    private boolean deleteMode = false;
    private ItemManager itemManager = new ItemManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecView();
    }

    private void setUpRecView() {
        RecyclerView recyclerView = findViewById(R.id.notesRemindersRecView);
        titles = getResources().getStringArray(R.array.titles);
        dates = getResources().getStringArray(R.array.dates);
        priorities = getResources().getStringArray(R.array.priorities);
        for (int i = 0; i < titles.length; i++)
            itemManager.addItem(new ListItem(titles[i], dates[i], priorities[i]));

        MyAdapter myAdapter = new MyAdapter(this, titles, dates, priorities, itemManager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.swapAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void minusClickListener(View view) {
        deleteMode = !deleteMode;
    }
    /*
    String.format("%d/%d/%d %d:%d %s",
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE), calendar.get(Calendar.AM_PM) == calendar.get(Calendar.AM) ? "AM" : "PM");
     */
}