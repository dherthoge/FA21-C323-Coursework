package com.example.project3dylanherthoge;

import java.util.ArrayList;

public class ItemManager {

    private ArrayList<ListItem> notesAndReminders;

    public ItemManager() {
        this.notesAndReminders = new ArrayList<>();
    }

    public void addItem(ListItem li) {
        this.notesAndReminders.add(li);
    }

    public void removeItem(int index) {
        this.notesAndReminders.remove(index);
    }

    public ArrayList<ListItem> getNotesAndReminders() {
        return notesAndReminders;
    }
}
