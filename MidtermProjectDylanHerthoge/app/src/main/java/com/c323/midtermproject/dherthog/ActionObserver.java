package com.c323.midtermproject.dherthog;

/**
 * An interface to give the deleted event to the parent Activity
 */
public interface ActionObserver {
    public void deleteItem(int position);
    public void favoriteItem(int position);
    public void displayItemOnMap(int position);
}