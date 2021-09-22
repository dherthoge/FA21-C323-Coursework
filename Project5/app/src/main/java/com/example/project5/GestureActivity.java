package com.example.project5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;

public class GestureActivity extends AppCompatActivity implements BallFragment.Updatable {

    LogFragment logFragment;
    BallFragment ballFragment;
    String log = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        if (savedInstanceState == null) {
            setLogFragment();
            setBallFragment();
        }
    }

    /**
     * Replaces the currently attached SettingsFragment with an identical one, but sends the
     * currently selected setting.
     * @param setting The currently active Setting
     */
    private void setLogFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        logFragment = new LogFragment();
        fragmentTransaction.replace(R.id.linear_layout_log_fragment, logFragment);
        fragmentTransaction.commit();
    }

    /**
     * Replaces the currently attached SettingsFragment with an identical one, but sends the
     * currently selected setting.
     * @param setting The currently active Setting
     */
    private void setBallFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ballFragment = new BallFragment();
        fragmentTransaction.replace(R.id.linear_layout_ball_fragment, ballFragment);
        fragmentTransaction.commit();
    }

    /**
     * Replaces the currently attached SettingsFragment with an identical one, but sends the
     * currently selected setting.
     * @param setting The currently active Setting
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

    @Override
    public void updateGesture(String logMsg) {
        log = logMsg + log;
        replaceLogFragment(log);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) log = savedInstanceState.getString("LOG");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("LOG", log);
        super.onSaveInstanceState(outState);
    }

}