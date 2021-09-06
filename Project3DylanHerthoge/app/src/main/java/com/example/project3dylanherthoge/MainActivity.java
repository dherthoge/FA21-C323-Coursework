package com.example.project3dylanherthoge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<String>(),
            dates = new ArrayList<String>(),
            priorities = new ArrayList<String>(),
            timestamps = new ArrayList<String>(),
            logged_titles = new ArrayList<String>(),
            logged_dates = new ArrayList<String>(),
            logged_priorities = new ArrayList<String>();
    private ArrayList<ListItem> listItems = new ArrayList();
    private boolean deleteMode = false;
    private ArrayAdapter<ListItem> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListItems();
        populateListView();
        registerListViewClicks();
    }

    private void populateListItems() {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor

        Collections.addAll(titles, getResources().getStringArray(R.array.titles));
        Collections.addAll(dates, getResources().getStringArray(R.array.dates));
        Collections.addAll(priorities, getResources().getStringArray(R.array.priorities));
        Collections.addAll(timestamps, getResources().getStringArray(R.array.timestamps));
        for (int i = 0; i < titles.size(); i++)
                listItems.add(new ListItem(titles.get(i), dates.get(i), priorities.get(i)));

        Collections.addAll(logged_titles, getResources().getStringArray(R.array.titles));
        Collections.addAll(logged_dates, getResources().getStringArray(R.array.dates));
        Collections.addAll(logged_priorities, getResources().getStringArray(R.array.priorities));
    }

    private void populateListView() {
        listViewAdapter = new ListViewAdapter();
        ListView listView = findViewById(R.id.itemLstView);
        listView.setAdapter(listViewAdapter);
    }

    private void repopulateListItems() {

        listItems.clear();
        for (int i = 0; i < titles.size(); i++)
                listItems.add(new ListItem(titles.get(i), dates.get(i), priorities.get(i)));
        listViewAdapter.notifyDataSetChanged();
    }

    private void changeListItemVisibility(int position) {

        if (deleteMode) {
            titles.remove(position);
            dates.remove(position);
            priorities.remove(position);
            repopulateListItems();
        }
    }

    public void minusClickListener(View view) {
        Button minusBtn = (Button) view;
        deleteMode = !deleteMode;
        if (deleteMode) minusBtn.setBackgroundTintList(this.getResources().getColorStateList(R.color.red_button));
        else minusBtn.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_button));
    }

    private void registerListViewClicks() {
        ListView listView = findViewById(R.id.itemLstView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                changeListItemVisibility(position);
            }
        });
    }

    public void plusBtnClicked(View view) {
        startActivityForResult(new Intent(this, AddItemActivity.class), 42);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Error: Result Cancelled", Toast.LENGTH_SHORT);
            return;
        }
        switch (requestCode){
            case 42:
                titles.add(data.getStringExtra("TITLE"));
                dates.add(data.getStringExtra("DATE"));
                priorities.add(data.getStringExtra("PRIORITY"));
                repopulateListItems();

                logged_titles.add(data.getStringExtra("TITLE"));
                logged_dates.add(data.getStringExtra("DATE"));
                logged_priorities.add(data.getStringExtra("PRIORITY"));
                Calendar calendar = Calendar.getInstance();
                timestamps.add(String.format("%d/%d/%d %d:%d %s",
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE), calendar.get(Calendar.AM_PM) == calendar.get(Calendar.AM) ? "AM" : "PM"));


                break;
        }
    }

    private class ListViewAdapter extends ArrayAdapter<ListItem> {

        public ListViewAdapter() {
            super(MainActivity.this, R.layout.item_layout, listItems);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Get the view to fill in
            View itemView = convertView;
            // If the view has not been inflated yet, do so
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_layout, parent, false);

            // Get the data to "fill in" the view
            ListItem currentItem = listItems.get(position);

            // Set the views to have the data for the current position
            TextView itemTitleTxtView = itemView.findViewById(R.id.itemTitleTxtView);
            TextView itemDateTxtView = itemView.findViewById(R.id.itemDateTxtView);
            ImageView itemPriorityImgView = itemView.findViewById(R.id.itemPriorityImgView);
            itemTitleTxtView.setText(currentItem.getTitle());
            itemDateTxtView.setText(currentItem.getDate());
            itemPriorityImgView.setImageResource(currentItem.getPriority() == "LOW" ? R.drawable.low_priority : R.drawable.high_priority);

            // Return the "filled in" view with
            return itemView;
        }
    }
}