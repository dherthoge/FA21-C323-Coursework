package com.example.project4dylanherthoge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PersonalInformationFragment personalInformationFragment = new PersonalInformationFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.linear_layout_personal_info_fragment, personalInformationFragment);
        fragmentTransaction.replace(R.id.linear_layout_settings_fragment, settingsFragment);
        fragmentTransaction.commit();
    }
}