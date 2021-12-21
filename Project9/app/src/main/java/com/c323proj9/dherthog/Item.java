package com.c323proj9.dherthog;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A representation of an Expense, holds its information.
 */
@Entity(tableName = "Items")
public class Item implements Comparable<Item> {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String expense, date, category;
    private double cost;

    /**
     * Creates an Item.
     * @param expense The title of the Item
     * @param date The date the Item was purchase
     * @param category The category of the Item
     * @param cost The cost of the item, can be negative
     */
    public Item(String expense, String date, String category, double cost) {
        this.expense = expense;
        this.date = date;
        this.category = category;
        this.cost = cost;
    }

    /**
     * Gets the unique id of the Item
     * @return Any positive integer
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique id of the Item
     * @return Any positive integer
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the Item
     * @return Any String
     */
    public String getExpense() {
        return expense;
    }

    /**
     * Sets the name of the Item
     * @return Any String
     */
    public void setExpense(String expense) {
        this.expense = expense;
    }

    /**
     * Gets the date the Item was purchase
     * @return Any String
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the Item was purchase
     * @return Any String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the category the Item was purchase
     * @return Any String
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category the Item was purchase
     * @return Any String
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the cost of the Item
     * @return Any double
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the Item
     * @return Any double
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Compares Items by dates.
     * @param item The item to compare the receiver to
     * @return -1, 0, or 1 if the receiver is smaller than, equal to, or greater than the given Item
     */
    @Override
    public int compareTo(Item item) {
        String[] date = this.getDate().split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

        String[] otherDate = item.getDate().split("/");
        int otherDay = Integer.parseInt(otherDate[0]);
        int otherMonth = Integer.parseInt(otherDate[1]);
        int otherYear = Integer.parseInt(otherDate[2]);

        if (year > otherYear) return 1;
        else if (year < otherYear) return -1;

        if (month > otherMonth) return 1;
        else if (month > otherMonth) return -1;

        if (day > otherDay) return 1;
        else if (day > otherDay) return -1;

        return 0;
    }
}
