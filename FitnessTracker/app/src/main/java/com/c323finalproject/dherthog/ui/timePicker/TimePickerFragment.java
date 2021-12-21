package com.c323finalproject.dherthog.ui.timePicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Implementation of DialogFragment.
 */
public class TimePickerFragment extends DialogFragment {

    // The listener for when a time is set
    private android.app.TimePickerDialog.OnTimeSetListener timeListener;

    /**
     * @param timeListener The listener for the TimePicker
     */
    public TimePickerFragment(TimePickerDialog.OnTimeSetListener timeListener) {
        this.timeListener = timeListener;
    }

    /**
     * Creates a TimePickerDialog using the current hour and minute.
     * @param savedInstanceState Used to recreate Dialog on config change
     * @return A TimePickerDialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new android.app.TimePickerDialog(getActivity(), timeListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}