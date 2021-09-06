package com.example.project3dylanherthoge;

import android.content.SharedPreferences;

import java.util.Calendar;

public class ListItem {

    private String title, date, priority;

    public ListItem(String title, String date, String priority) {
        Calendar calendar = Calendar.getInstance();

        this.title = title;
        // dateAndTime is a string of the pattern "DD/MM/YYYY HH:MM _M"
        this.date = date;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String isPriority() {
        return priority;
    }
}
