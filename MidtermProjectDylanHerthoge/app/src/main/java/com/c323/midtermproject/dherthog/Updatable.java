package com.c323.midtermproject.dherthog;

/**
 * Interface for activity communicators during item creation. Allows a fragment to notify it's
 * parent activity what the user has clicked.
 */
public interface Updatable {

    /**
     * Notifies the parent activity item creation was canceled.
     */
    public void cancelClicked();

    /**
     * Notifies the parent activity an item was created.
     */
    public void addClicked();
}
