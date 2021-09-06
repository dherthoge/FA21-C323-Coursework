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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList(),
            dates = new ArrayList(),
            priorities = new ArrayList(),
            timestamps = new ArrayList(),
            logged_titles = new ArrayList(),
            logged_dates = new ArrayList(),
            logged_priorities = new ArrayList();
    private ArrayList<ListItem> listItems = new ArrayList();
    private ArrayAdapter<ListItem> listViewAdapter;
    private boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListItems();
        populateListView();
        registerListViewClicks();
    }

    private void populateListItems() {
        loadListItems();

//        Collections.addAll(titles, getResources().getStringArray(R.array.titles));
//        Collections.addAll(dates, getResources().getStringArray(R.array.dates));
//        Collections.addAll(priorities, getResources().getStringArray(R.array.priorities));
//        Collections.addAll(timestamps, getResources().getStringArray(R.array.timestamps));
//        Collections.addAll(logged_titles, getResources().getStringArray(R.array.titles));
//        Collections.addAll(logged_dates, getResources().getStringArray(R.array.dates));
//        Collections.addAll(logged_priorities, getResources().getStringArray(R.array.priorities));
        for (int i = 0; i < titles.size(); i++)
                listItems.add(new ListItem(titles.get(i), dates.get(i), priorities.get(i)));


        saveListItems();
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

        saveListItems();
    }

    private void changeListItemVisibility(int position) {

        if (deleteMode) {
            deleteMode = !deleteMode;
            minusBackgroundTintChange();
            titles.remove(position);
            dates.remove(position);
            priorities.remove(position);
            repopulateListItems();
        }
    }

    public void minusClickListener(View view) {
        deleteMode = !deleteMode;
        minusBackgroundTintChange();
    }

    public void minusBackgroundTintChange() {
        Button minusBtn = findViewById(R.id.minusBtn);
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

    private void saveListItems() {
        String titlesAggregate = "";
        if (titles.size() > 0) titlesAggregate = titles.get(0);
        for (int i = 1; i < titles.size(); i++) {
            titlesAggregate += " ? " + titles.get(i);
        }
        titlesAggregate += " ?";

        String datesAggregate = "";
        if (dates.size() > 0) datesAggregate = dates.get(0);
        for (int i = 1; i < dates.size(); i++) {
            datesAggregate += " ? " + dates.get(i);
        }
        datesAggregate += " ?";

        String prioritiesAggregate = "";
        if (priorities.size() > 0) prioritiesAggregate = priorities.get(0);
        for (int i = 1; i < priorities.size(); i++) {
            prioritiesAggregate += " ? " + priorities.get(i);
        }
        prioritiesAggregate += " ?";

        String timestampsAggregate = "";
        if (timestamps.size() > 0) timestampsAggregate = timestamps.get(0);
        for (int i = 1; i < timestamps.size(); i++) {
            timestampsAggregate += " ? " + timestamps.get(i);
        }
        timestampsAggregate += " ?";

        String loggedTitlesAggregate = "";
        if (logged_titles.size() > 0) loggedTitlesAggregate = logged_titles.get(0);
        for (int i = 1; i < logged_titles.size(); i++) {
            loggedTitlesAggregate += " ? " + logged_titles.get(i);
        }
        loggedTitlesAggregate += " ?";

        String loggedDatesAggregate = "";
        if (logged_dates.size() > 0) loggedDatesAggregate = logged_dates.get(0);
        for (int i = 1; i < logged_dates.size(); i++) {
            loggedDatesAggregate += " ? " + logged_dates.get(i);
        }
        loggedDatesAggregate += " ?";

        String loggedPrioritiesAggregate = "";
        if (logged_priorities.size() > 0) loggedPrioritiesAggregate = logged_priorities.get(0);
        for (int i = 1; i < logged_priorities.size(); i++) {
            loggedPrioritiesAggregate += " ? " + logged_priorities.get(i);
        }
        loggedPrioritiesAggregate += " ?";

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("TITLES", titlesAggregate);
        editor.putString("DATES", datesAggregate);
        editor.putString("PRIORITIES", prioritiesAggregate);
        editor.putString("TIMESTAMPS", timestampsAggregate);
        editor.putString("LOGGEDTITLES", loggedTitlesAggregate);
        editor.putString("LOGGEDDATES", loggedDatesAggregate);
        editor.putString("LOGGEDPRIORITIES", loggedPrioritiesAggregate);
        editor.commit();
    }

    private void loadListItems() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String titlesAggregate = sp.getString("TITLES", "");
        String datesAggregate = sp.getString("DATES", "");
        String prioritiesAggregate = sp.getString("PRIORITIES", "");
        String timestampsAggregate = sp.getString("TIMESTAMPS", "");
        String loggedTitlesAggregate = sp.getString("LOGGEDTITLES", "");
        String loggedDatesAggregate = sp.getString("LOGGEDDATES", "");
        String loggedPrioritiesAggregate = sp.getString("LOGGEDPRIORITIES", "");

        Scanner sc = new Scanner(titlesAggregate);
        String currentTitle = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                titles.add(currentTitle.substring(1));
                currentTitle = "";
            }
            else currentTitle += " " + currentWord;
        }
        sc.close();

        sc = new Scanner(datesAggregate);
        String currentDate = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                dates.add(currentDate.substring(1));
                currentDate = "";
            }
            else currentDate += " " + currentWord;
        }
        sc.close();

        sc = new Scanner(prioritiesAggregate);
        String currentPriority = "";
        while (sc.hasNext()) {
            String currentWord = sc.next();
            if (currentWord.equals("?")) {
                priorities.add(currentPriority.substring(1));
                currentPriority = "";
            }
            else currentPriority += " " + currentWord;
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

        sc = new Scanner(loggedTitlesAggregate);
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
            TextView itemTitleTxtView = itemView.findViewById(R.id.dataLogItemTitleTxtView);
            TextView itemDateTxtView = itemView.findViewById(R.id.dataLogItemDateTxtView);
            ImageView itemPriorityImgView = itemView.findViewById(R.id.dataLogItemPriorityImgView);
            itemTitleTxtView.setText(currentItem.getTitle());
            itemDateTxtView.setText(currentItem.getDate());
            itemPriorityImgView.setImageResource(currentItem.getPriority().equals("LOW") ? R.drawable.low_priority : R.drawable.high_priority);

            // Return the "filled in" view with
            return itemView;
        }
    }
}