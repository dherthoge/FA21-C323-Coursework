package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class GestureActivity extends AppCompatActivity implements BallFragment.Updatable {

    LogFragment logFragment;
    BallFragment ballFragment;
    String log = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);


        setLogFragment();
        setBallFragment();
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
    private void replaceLogFragment() {
        // Creates a Bundle to pass to the newly created SettingsFragment
        Bundle curSettingBundle = new Bundle();
        curSettingBundle.putString("LOG", log);

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
        replaceLogFragment();
    }

    @Override
    public void updateGesture(float dx, float dy) {

    }

}