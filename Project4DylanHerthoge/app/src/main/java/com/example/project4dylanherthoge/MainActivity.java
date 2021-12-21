package com.example.project4dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * The Main Activity of the application.
 */
public class MainActivity extends AppCompatActivity implements SettingsFragment.settingsFragmentInterface {

    // Variables for strings defined in strings.xml
    private String internetMsg;
    private String batteryMsg;
    private String storageMsg;

    // The textView in activity_main.xml
    private TextView textSettingInformation;


    /**
     * Initializes the Activity.
     * @param savedInstanceState The state of a previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get variables for information and View references
        internetMsg = getString(R.string.internet_msg);
        batteryMsg = getString(R.string.battery_msg);
        storageMsg = getString(R.string.storage_msg);
        textSettingInformation = findViewById(R.id.text_setting_information);
        textSettingInformation.setText(internetMsg);

        // Attaches fragments to the activity
        if (savedInstanceState == null) {
            /* Activities have their onCreate called twice for some reason, so I need to check if it
             * is being created for the second time, and if it is I don't want to create my fragments
             * again
             */
            attachPersonalInformationFragment();
            attachSettingsFragment();
        }


        // Change colors if night mode is active
        processNightMode();
    }

    /**
     * Attaches a SettingsFragment to MainActivity.
     */
    private void attachSettingsFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.linear_layout_settings_fragment, settingsFragment);
        fragmentTransaction.commit();
    }


    /**
     * Attaches a PersonalInformationFragment to MainActivity.
     */
    private void attachPersonalInformationFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PersonalInformationFragment personalInformationFragment = new PersonalInformationFragment();
        fragmentTransaction.replace(R.id.linear_layout_personal_info_fragment, personalInformationFragment);
        fragmentTransaction.commit();
    }

    /**
     * Replaces the currently attached SettingsFragment with an identical one, but sends the
     * currently selected setting.
     * @param setting The currently active Setting
     */
    private void replaceSettingsFragment(String setting) {
        // Creates a Bundle to pass to the newly created SettingsFragment
        Bundle curSettingBundle = new Bundle();
        curSettingBundle.putString("SETTING", setting);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(curSettingBundle);
        fragmentTransaction.replace(R.id.linear_layout_settings_fragment, settingsFragment);
        fragmentTransaction.commit();
    }

    /**
     * Updates the Activity to display information about the given setting
     * @param curSetting The currently selected setting
     */
    @Override
    public void settingChanged(String curSetting) {
        changeDisplayedInfo(curSetting);
    }

    /**
     * Changes the information being displayed to show information about the current setting.
     * @param curSetting The currently sellected setting
     */
    private void changeDisplayedInfo(String curSetting) {
        // Holds the information for the new setting
        String newSettingInfo = textSettingInformation.getText().toString();
        // Updates the message to be displayed and changes the highlighted setting in SettingsFragment
        switch (curSetting) {
            case "INTERNET":
                newSettingInfo = internetMsg;
                replaceSettingsFragment("INTERNET");
                break;
            case "BATTERY":
                newSettingInfo = batteryMsg;
                replaceSettingsFragment("BATTERY");
                break;
            case "STORAGE":
                newSettingInfo = storageMsg;
                replaceSettingsFragment("STORAGE");
                break;
            case "NEXT":
                // Determines what the next setting information to display is
                if (newSettingInfo == internetMsg) newSettingInfo = batteryMsg;
                else if (newSettingInfo == batteryMsg) newSettingInfo = storageMsg;
                else if (newSettingInfo == storageMsg) newSettingInfo = internetMsg;

                // Changes the highlighted setting in SettingsFragment
                String setting = "INTERNET";
                if (newSettingInfo == batteryMsg) setting = "BATTERY";
                else if (newSettingInfo == storageMsg) setting = "STORAGE";
                replaceSettingsFragment(setting);
                break;
        }
        textSettingInformation.setText(newSettingInfo);
    }

    /**
     * Changes the highlighted setting and information displayed.
     * @param view The clicked View.
     */
    public void onNextClick(View view) {
        settingChanged("NEXT");
    }

    /**
     * Sets the color of the text view to White if night mode is active.
     */
    private void processNightMode() {
        /*
         Most of this code is taken from https://stackoverflow.com/questions/44170028/android-how-to-detect-if-night-mode-is-on-when-using-appcompatdelegate-mode-ni
         but I figured it was okay because it is:
            1) purely for aesthetic purposes
            2) we have been taught nothing about night mode and little about Configuration
         */
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            textSettingInformation.setTextColor(Color.WHITE);
        }
    }
}