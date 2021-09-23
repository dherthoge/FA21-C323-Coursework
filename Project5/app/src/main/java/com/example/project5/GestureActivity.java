package com.example.project5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Creates fragments to move a ball and log it's movements. Also handles communications between the
 * two fragments and manages the log.
 */
public class GestureActivity extends AppCompatActivity implements BallFragment.Updatable {

    // Global variables for storing various objects
    LogFragment logFragment;
    BallFragment ballFragment;
    String log = "";


    /**
     * Creates and attaches both fragments if they are not already created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                          may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

       /* Since rotating the device creates the fragments twice, need to check if the fragments
        * have been created the first time. If they created a second time, this causes issues with
        * saving the states of the fragments
        */
        if (savedInstanceState == null) {
            setLogFragment();
            setBallFragment();
        }
    }

    /**
     * Creates and attaches a LogFragment to it's linear layout.
     */
    private void setLogFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        logFragment = new LogFragment();
        fragmentTransaction.replace(R.id.linear_layout_log_fragment, logFragment);
        fragmentTransaction.commit();
    }

    /**
     * Creates and attaches a BallFragment to it's linear layout.
     */
    private void setBallFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ballFragment = new BallFragment();
        fragmentTransaction.replace(R.id.linear_layout_ball_fragment, ballFragment);
        fragmentTransaction.commit();
    }

    /**
     * Recreates and attaches a BallFragment to it's linear layout with the new log message.
     * @param newLogMsg The message for the log to display
     */
    private void replaceLogFragment(String newLogMsg) {
        // Creates a Bundle to pass to the newly created SettingsFragment
        Bundle curSettingBundle = new Bundle();
        curSettingBundle.putString("LOG", newLogMsg);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        logFragment = new LogFragment();
        logFragment.setArguments(curSettingBundle);
        fragmentTransaction.replace(R.id.linear_layout_log_fragment, logFragment);
        fragmentTransaction.commit();
    }

    /**
     * Determines the direction the ball moved and logs it.
     * @param dx The movement of the ball on the x axis
     * @param dy The movement of the ball on the x axis
     */
    @Override
    public void updateBallMovement(float dx, float dy) {

        // Determines direction starting at up and rotates clock wise
        String logMsg = "";
        if (dx == 0 && dy < 0) logMsg = "You moved up\n";
        else if (dx > 0 && dy < 0) logMsg = "You moved up-right\n";
        else if (dx > 0 && dy == 0) logMsg = "You moved right\n";
        else if (dx > 0 && dy > 0) logMsg = "You moved down-right\n";
        else if (dx == 0 && dy > 0) logMsg = "You moved down\n";
        else if (dx < 0 && dy > 0) logMsg = "You moved down-left\n";
        else if (dx < 0 && dy == 0) logMsg = "You moved left\n";
        else if (dx < 0 && dy < 0) logMsg = "You moved up-left\n";
        log = logMsg + log;
        replaceLogFragment(log);
    }

    /**
     * Logs the given message. All messages given will be related to gestures, not ball movement.
     * @param logMsg The message to log
     */
    @Override
    public void updateGesture(String logMsg) {
        log = logMsg + log;
        replaceLogFragment(log);
    }

    /**
     * Gets the log from the previous instance and stores it.
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle) or null. This value may be null.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) log = savedInstanceState.getString("LOG");
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * Saves the data currently in the log.
     * @param outState Bundle: Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("LOG", log);
        super.onSaveInstanceState(outState);
    }

}