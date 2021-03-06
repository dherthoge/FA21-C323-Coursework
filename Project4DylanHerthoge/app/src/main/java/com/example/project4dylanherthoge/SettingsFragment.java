package com.example.project4dylanherthoge;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    // References of Views in SettingsFragment
    private View view;
    private Button buttonInternet;
    private Button buttonBattery;
    private Button buttonStorage;

    // The currently selected setting
    private Setting curSetting;
    // All possible settings
    public enum Setting {
        INTERNET,
        BATTERY,
        STORAGE
    }

    // An interface to communicate with MainActivity
    public interface settingsFragmentInterface {
        public void settingChanged(String curSetting);
    }
    // Reference to MainActivity
    private settingsFragmentInterface activityCommunicator;

    /**
     * Preserves the currently selected setting.
     * @param outState Bundle in which to place your saved state. This value cannot be null.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("SETTING", curSetting.name());
        super.onSaveInstanceState(outState);
    }

    /**
     * If the fragment is being re-created from a previous saved state, initialize the fragment from
     * it's saved state.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!= null) {
            switch (savedInstanceState.getString("SETTING")) {
                case "INTERNET":
                    curSetting = Setting.INTERNET;
                    break;
                case "BATTERY":
                    curSetting = Setting.BATTERY;
                    break;
                case "STORAGE":
                    curSetting = Setting.STORAGE;
                    break;
            }

            notifyActivityCommunicatorSettingChanged();
        }
    }

    /**
     * Creates a SettingsFragment object.
     */
    public SettingsFragment() {
        // Initializes the current setting to INTERNET
        curSetting = Setting.INTERNET;
    }

    /**
     * Gets a reference to MainActivity.
     * @param context The current state of the app
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (settingsFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement SettingsFragmentInterface");
        }
    }

    /**
     * Gets passed arguments from the transaction.
     * @param savedInstanceState The state of a previous instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) curSetting = Setting.valueOf(getArguments().getString("SETTING"));
    }

    /**
     * Generates a View representing the fragment.
     * @param inflater A LayoutInflater object
     * @param container The container for the current fragment.
     * @param savedInstanceState The state of a previous instance
     * @return a View representing a SettingsFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Set references for the Buttons in SettingsFragment
        buttonInternet = view.findViewById(R.id.button_internet);
        buttonBattery = view.findViewById(R.id.button_battery);
        buttonStorage = view.findViewById(R.id.button_storage);
        // Sets onClickListeners for each button
        buttonInternet.setOnClickListener(this::onClick);
        buttonBattery.setOnClickListener(this::onClick);
        buttonStorage.setOnClickListener(this::onClick);
        // Sets the currently active button
        setActiveButton();

        return view;
    }

    /**
     * Changes the active button and notifies the activityCommunicator of the change.
     * @param view The clicked button
     */
    @Override
    public void onClick(View view) {
        // Determines which setting was chosen (i.e. what button was clicked
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

        notifyActivityCommunicatorSettingChanged();
    }

    /**
     * Notifies the activity communicator that the selected setting has changed.
     */
    private void notifyActivityCommunicatorSettingChanged() {
        activityCommunicator.settingChanged(curSetting.name());
    }

    /**
     * Sets the color of the currently active button differently from the inactive buttons.
     */
    private void setActiveButton() {
        switch (curSetting.toString()) {
            case "INTERNET":
                buttonInternet.setTextColor(getResources().getColor(R.color.my_purple));
                buttonBattery.setTextColor(getResources().getColor(R.color.inactive_grey));
                buttonStorage.setTextColor(getResources().getColor(R.color.inactive_grey));
                break;
            case "BATTERY":
                buttonInternet.setTextColor(getResources().getColor(R.color.inactive_grey));
                buttonBattery.setTextColor(getResources().getColor(R.color.my_purple));
                buttonStorage.setTextColor(getResources().getColor(R.color.inactive_grey));
                break;
            case "STORAGE":
                buttonInternet.setTextColor(getResources().getColor(R.color.inactive_grey));
                buttonBattery.setTextColor(getResources().getColor(R.color.inactive_grey));
                buttonStorage.setTextColor(getResources().getColor(R.color.my_purple));
                break;
        }
    }
}