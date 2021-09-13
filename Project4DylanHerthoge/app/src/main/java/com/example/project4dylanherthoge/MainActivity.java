package com.example.project4dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SettingsFragment.settingsFragmentInterface, PersonalInformationFragment.personalInformationFragmentInterface {

    String internetMsg;
    String batteryMsg;
    String storageMsg;
    TextView textSettingMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internetMsg = getString(R.string.internet_msg);
        batteryMsg = getString(R.string.battery_msg);
        storageMsg = getString(R.string.storage_msg);
        textSettingMsg = findViewById(R.id.text_setting_information);
        textSettingMsg.setText(internetMsg);

        attachPersonalInformationFragment();
        attachSettingsFragment();
    }

    private void attachSettingsFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.linear_layout_settings_fragment, settingsFragment);
        fragmentTransaction.commit();
    }

    private void attachPersonalInformationFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PersonalInformationFragment personalInformationFragment = new PersonalInformationFragment();
        fragmentTransaction.replace(R.id.linear_layout_personal_info_fragment, personalInformationFragment);
        fragmentTransaction.commit();
    }

    private void replaceSettingsFragment(String setting) {
        Bundle curSettingBundle = new Bundle();
        curSettingBundle.putString("SETTING", setting);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(curSettingBundle);
        fragmentTransaction.replace(R.id.linear_layout_settings_fragment, settingsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void changeDisplayedSetting(String settingMsgStr) {
        String settingMsg = textSettingMsg.getText().toString();
        switch (settingMsgStr) {
            case "INTERNET":
                settingMsg = internetMsg;
                replaceSettingsFragment("INTERNET");
                break;
            case "BATTERY":
                settingMsg = batteryMsg;
                replaceSettingsFragment("BATTERY");
                break;
            case "STORAGE":
                settingMsg = storageMsg;
                replaceSettingsFragment("STORAGE");
                break;
            case "NEXT":
                if (settingMsg == internetMsg) settingMsg = batteryMsg;
                else if (settingMsg == batteryMsg) settingMsg = storageMsg;
                else if (settingMsg == storageMsg) settingMsg = internetMsg;

                String setting = "INTERNET";
                if (settingMsg == batteryMsg) setting = "BATTERY";
                else if (settingMsg == storageMsg) setting = "STORAGE";
                replaceSettingsFragment(setting);
                break;
        }
        textSettingMsg.setText(settingMsg);
    }

    public void onNextClick(View view) {
        changeDisplayedSetting("NEXT");
    }
}