package com.example.project6dylanherthoge;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * DatePicker class to get a date from the user. This class uses the Factory pattern to allow the
 * same DatePicker to work across multiple Activities.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // The activity listening for a date
    private Observer currentObserver;
    private volatile static DatePicker datePicker = null;

    /**
     * Gets the current instance of DatePicker or creates on if one has not been instantiated
     * already.
     * @param currentObserver The Observer requesting the DatePicker
     * @return The current DatePicker instance
     */
    public static DatePicker getInstance(Observer currentObserver) {

        if(datePicker == null)
            synchronized (DatePicker.class) {
                    datePicker = new DatePicker();
            }

        datePicker.currentObserver = currentObserver;
        return datePicker;
    }

    /**
     * An interface to give the picked date to the requesting Activity.
     */
    public interface Observer {
        public void newDate(int year, int month, int day);
    }

    /**
     * Creates a DatePickerDialog to display to the user.
     * @param savedInstanceState last saved instance state of the Fragment, or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Gives the picked date to the current Observer.
     * @param datePicker the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with Calendar#MONTH)
     * @param day the selected day of the month (1-31, depending on month)
     */
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {

        // Need to add one because months start at 0
        currentObserver.newDate(year, month+1, day);
        Log.i("onDateSet", "Chosen date: " + day + "/" + month + "/" + year);
    }
}