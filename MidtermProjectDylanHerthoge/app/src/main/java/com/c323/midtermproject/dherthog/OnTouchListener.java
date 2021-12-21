package com.c323.midtermproject.dherthog;

import android.content.Context;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class can be attached to any View. On the first swipe to the left notifies user it will
 * delete an item. On the second swipe (within 5 seconds of the first) it will delete the item.
 */
public class OnTouchListener implements View.OnTouchListener {

    private int position;
    private float originalX, currentDx;
    private boolean swipedLeftOnce;
    private ActionObserver actionObserver;
    private Context applicationContext;

    /**
     * Create an OnTouchListener to listen for Item deletion.
     * @param position The position of the Item being listened to
     * @param applicationContext The context of the application
     * @param actionObserver The Object to notify an item needs to be deleted
     */
    public OnTouchListener(int position, Context applicationContext, ActionObserver actionObserver) {
        this.position = position;
        this.applicationContext = applicationContext;
        this.actionObserver = actionObserver;
    }

    /**
     * Determines if the user wants to delete an item (by swiping left). On the second swipe (within
     * 5 seconds of the first), deletes the item.
     * @param view        The View the event originated from
     * @param motionEvent The user's MotionEvent
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int action = motionEvent.getActionMasked();
        switch (action) {

            // Stores the original touch coords
            case MotionEvent.ACTION_DOWN:
                originalX = motionEvent.getX();
                break;

            // Calculates the delta of the start and stop coords
            case MotionEvent.ACTION_UP:
                currentDx = motionEvent.getX() - originalX;

                // If this is the user's first time swiping left, ask for deletion
                if (currentDx < -10 && !swipedLeftOnce) {
                    Toast.makeText(applicationContext, "Do you want to delete the item?", Toast.LENGTH_SHORT).show();
                    swipedLeftOnce = true;

                    // This runs regardless of if the item was deleted or not. It really doesn't matter
                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    swipedLeftOnce = false;
                                }
                            }, 5000
                    );
                    return true;
                }
                // If user already swiped left less than 5 seconds ago, delete the item
                else if (currentDx < -10 && swipedLeftOnce) {
                    actionObserver.deleteItem(position);
                    swipedLeftOnce = false;
                    return true;
                }
                break;
        }

        return false;
    }
}