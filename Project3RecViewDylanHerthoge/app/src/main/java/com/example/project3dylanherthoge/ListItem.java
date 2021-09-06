package com.example.project3dylanherthoge;

import android.content.SharedPreferences;

import java.util.Calendar;

public class ListItem {

    private String title, date, priority;

    public ListItem(String title, String date, String priority) {
        this.title = title;
        this.date = date;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }
}
