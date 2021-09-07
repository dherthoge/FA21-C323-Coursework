package com.example.project3dylanherthoge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * An Activity to display the log of items.
 */
public class DataLogActivity extends AppCompatActivity {

    private ArrayList<String> timestamps = new ArrayList(),
            logged_titles = new ArrayList(),
            logged_dates = new ArrayList(),
            logged_priorities = new ArrayList();
private ArrayList<Item> itemList = new ArrayList();
    private ArrayAdapter<Item> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_log);

        populateListItems();
        populateListView();
    }

    /**
     * Adds the items saved in SharedPreferences to itemList.
     */
    private void populateListItems() {
        loadListItems();
        for (int i = 0; i < logged_titles.size(); i++)
            itemList.add(new Item(logged_titles.get(i), logged_dates.get(i), logged_priorities.get(i), timestamps.get(i)));
    }

    /**
     * Binds the ListViewAdapter to the itemListView in activity_data_log.xml.
     */
    private void populateListView() {
        listViewAdapter = new DataLogActivity.ListViewAdapter();
        ListView listView = findViewById(R.id.dataLogListView);
        listView.setAdapter(listViewAdapter);
    }

    /**
     * Loads timestamps, logged_titles, logged_dates, and logged-priorities from SharedPreferences
     * and adds them to their respective ArrayList.
     */
    private void loadListItems() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String loggedTitlesAggregate = sp.getString("LOGGEDTITLES", "");
        String loggedDatesAggregate = sp.getString("LOGGEDDATES", "");
        String loggedPrioritiesAggregate = sp.getString("LOGGEDPRIORITIES", "");
        String timestampsAggregate = sp.getString("TIMESTAMPS", "");

        Scanner sc = new Scanner(loggedTitlesAggregate);
        String currentLoggedTitle = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                logged_titles.add(currentLoggedTitle.substring(1));
                currentLoggedTitle = "";
            }
            else currentLoggedTitle += " " + currentWord;
        }
        sc.close();

        sc = new Scanner(loggedDatesAggregate);
        String currentLoggedDate = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                logged_dates.add(currentLoggedDate.substring(1));
                currentLoggedDate = "";
            }
            else currentLoggedDate += " " + currentWord;
        }
        sc.close();

        sc = new Scanner(loggedPrioritiesAggregate);
        String currentLoggedPriority = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                logged_priorities.add(currentLoggedPriority.substring(1));
                currentLoggedPriority = "";
            }
            else currentLoggedPriority += " " + currentWord;
        }
        sc.close();

        sc = new Scanner(timestampsAggregate);
        String currentTimestamp = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                timestamps.add(currentTimestamp.substring(1));
                currentTimestamp = "";
            }
            else currentTimestamp += " " + currentWord;
        }
        sc.close();
    }

    /**
     * Converts stored Item information to a hierarchy of views.
     */
    private class ListViewAdapter extends ArrayAdapter<Item> {

        public ListViewAdapter() {
            super(DataLogActivity.this, R.layout.data_log_item_layout, itemList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Get the view to fill in
            View itemView = convertView;
            // If the view has not been inflated yet, do so
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.data_log_item_layout, parent, false);

            // Get the data to "fill in" the view
            Item currentItem = itemList.get(position);

            // Set the views to have the data for the current position
            TextView dataLogItemTitleTxtView = itemView.findViewById(R.id.dataLogItemTitleTxtView);
            TextView dataLogItemDateTxtView = itemView.findViewById(R.id.dataLogItemDateTxtView);
            TextView dataLogItemTimestampTxtView = itemView.findViewById(R.id.dataLogItemTimestampTxtView);
            ImageView dataLogItemPriorityImgView = itemView.findViewById(R.id.dataLogItemPriorityImgView);
            dataLogItemTitleTxtView.setText(currentItem.getTitle());
            dataLogItemDateTxtView.setText(currentItem.getDate());
            dataLogItemTimestampTxtView.setText(currentItem.getTimestamp());
            dataLogItemPriorityImgView.setImageResource(currentItem.getPriority().equals("LOW") ? R.drawable.low_priority : R.drawable.high_priority);

            // Return the "filled in" view with
            return itemView;
        }
    }
}