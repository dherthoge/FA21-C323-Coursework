package com.example.project3dylanherthoge;

/**
 * Packages information for each item in the list to be used by ListViewAdapter
 * @author Dylan Herthoge
 */
public class Item {

    private String title, date, priority, timestamp;

    /**
     * Constructs a ListItem object.
     * @param title The title of the item
     * @param date The date of the item
     * @param priority The priority level of the item
     */
    public Item(String title, String date, String priority) {
        this.title = title;
        this.date = date;
        this.priority = priority;
    }

    /**
     * Constructs a ListItem object.
     * @param title The title of the item
     * @param date The date of the item
     * @param priority The priority level of the item
     * @param timestamp The time the item was created
     */
    public Item(String title, String date, String priority, String timestamp) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    /**
     * Returns the title of the item.
     * @return The title of the item
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the date of the item.
     * @return The date of the item
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Returns the priority of the item.
     * @return The priority of the item
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * Returns the timestamp of the item.
     * @return The timestamp of the item
     */
    public String getTimestamp() {
        return this.timestamp;
    }
}
