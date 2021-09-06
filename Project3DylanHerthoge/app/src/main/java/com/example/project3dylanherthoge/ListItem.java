package com.example.project3dylanherthoge;

import android.content.SharedPreferences;

import java.util.Calendar;

public class ListItem {

    private String title, date, priority, timestamp;

    public ListItem(String title, String date, String priority) {
        this.title = title;
        this.date = date;
        this.priority = priority;
    }

    public ListItem(String title, String date, String priority, String timestamp) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
}
