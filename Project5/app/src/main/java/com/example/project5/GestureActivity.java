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
    public void update(String logMsg) {
        log = logMsg + log;
        replaceLogFragment();
    }

}