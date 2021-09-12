package com.example.project4dylanherthoge;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

    int purple500 = Color.rgb(98,0,238);
    int inactiveGrey = Color.rgb(163, 163, 163);
    
    Setting curSetting;
    public enum Setting {
        INTERNET,
        BATTERY,
        STORAGE
    }
    settingsFragmentInterface activityCommunicator;

    public interface settingsFragmentInterface {
        public void changeDisplayedSetting(String settingMsg);
    }

    public SettingsFragment() {
        // Required empty public constructor
        curSetting = Setting.INTERNET;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (settingsFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement SettingsFragmentInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) curSetting = Setting.valueOf(getArguments().getString("SETTING"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonInternet = view.findViewById(R.id.button_internet);
        buttonBattery = view.findViewById(R.id.button_battery);
        buttonStorage = view.findViewById(R.id.button_storage);
        buttonInternet.setOnClickListener(this::onClick);
        buttonBattery.setOnClickListener(this::onClick);
        buttonStorage.setOnClickListener(this::onClick);
        setActiveButton();

        return view;
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
        }
        setActiveButton();

        activityCommunicator.changeDisplayedSetting(curSetting.name());
    }

    public void setActiveButton() {
        switch (curSetting.toString()) {
            case "INTERNET":
                buttonInternet.setTextColor(purple500);
                buttonBattery.setTextColor(inactiveGrey);
                buttonStorage.setTextColor(inactiveGrey);
                break;
            case "BATTERY":
                buttonInternet.setTextColor(inactiveGrey);
                buttonBattery.setTextColor(purple500);
                buttonStorage.setTextColor(inactiveGrey);
                break;
            case "STORAGE":
                buttonInternet.setTextColor(inactiveGrey);
                buttonBattery.setTextColor(inactiveGrey);
                buttonStorage.setTextColor(purple500);
                break;
        }
    }
}