package com.example.project4dylanherthoge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    View view;
    Button buttonInternet;
    Button buttonBattery;
    Button buttonStorage;
    Button buttonNext;
    
    Setting curSetting;
    private enum Setting {
        INTERNET,
        BATTERY,
        STORAGE
    }

    public interface settingsFragmentInterface {
        public void settingChanged(String settingMsg);
    }
    settingsFragmentInterface activityCommunicator;

    public SettingsFragment() {
        // Required empty public constructor
        curSetting = Setting.INTERNET;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_internet:
                curSetting = Setting.INTERNET;
                break;
            case R.id.button_battery:
                curSetting = Setting.BATTERY;
                break;
            case R.id.button_storage:
                curSetting = Setting.STORAGE;
                break;
            case R.id.button_next:
                if (Setting.INTERNET == curSetting) curSetting = Setting.BATTERY;
                else if (Setting.BATTERY == curSetting) curSetting = Setting.STORAGE;
                else if (Setting.STORAGE == curSetting) curSetting = Setting.INTERNET;
                break;
        }

        activityCommunicator.settingChanged(curSetting.name());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonInternet = view.findViewById(R.id.button_internet);
        buttonBattery = view.findViewById(R.id.button_battery);
        buttonStorage = view.findViewById(R.id.button_storage);
        buttonNext = view.findViewById(R.id.button_next);
        buttonInternet.setOnClickListener(this::onClick);

        return view;
    }
}