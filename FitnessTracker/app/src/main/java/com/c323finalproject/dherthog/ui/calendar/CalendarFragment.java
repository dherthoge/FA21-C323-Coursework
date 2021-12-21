package com.c323finalproject.dherthog.ui.calendar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.c323finalproject.dherthog.DailyWorkout;
import com.c323finalproject.dherthog.Database;
import com.c323finalproject.dherthog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private List<DailyWorkout> dailyWorkouts;
    private List<Calendar> highlightedDates;

    /**
     * Gets a reference for the CalendarView.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                 fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                  attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here
     * @return Return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.cv_daily_workouts);

        return root;
    }

    /**
     * Highlights dates of DailyWorkouts in the database and sets an OnDayClickListener.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Get workouts
        dailyWorkouts = Database.getInstance(requireContext()).getDailyWorkoutDAO().getAllDailyWorkout();
        highlightedDates = getHighlightedDates();

        // Set the current date of the calendar
        try {
            calendarView.setDate(Calendar.getInstance());
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        // Set the highlighted days
        calendarView.setHighlightedDays(highlightedDates);

        // Create listener to display workout information on a clicked day
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                long timeToDisplayInMillis = findTimeToDisplayInMillis(eventDay);

                // Check that the user clicked a highlighted day
                if (timeToDisplayInMillis == -1)
                    return;

                // Create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("");

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.alert_daily_workout, null);
                TextView tv = customLayout.findViewById(R.id.tv_time_in_millis);
                tv.setText(String.format(Locale.US, "Completed all exercises in %d minutes!", timeToDisplayInMillis/60000));
                builder.setView(customLayout);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /**
     * The amount of time to display in milliseconds for the user's workout on the given day.
     * @param eventDay A day the user completed a workout.
     * @return A positive long, or -1 if a date was not found.
     */
    private long findTimeToDisplayInMillis(EventDay eventDay) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        for (int i = 0; i < highlightedDates.size(); i++) {
            DailyWorkout currentDailyWorkout = dailyWorkouts.get(i);

            try {
                // Check if the dates are the same
                long eventDayTimeInMillis = eventDay.getCalendar().getTime().getTime();
                long workout = simpleDateFormat.parse(currentDailyWorkout.getDate()).getTime();
                if (eventDayTimeInMillis == workout) {
                    return currentDailyWorkout.getTimeInMillis(); // finished since we found the correct daily workout
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Makes a list of dates to highlight from dailyWorkouts.
     * @return A list of dates to highlight
     */
    private List<Calendar> getHighlightedDates() {
        // Get dates to highlight
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        highlightedDates = new ArrayList<>();
        for (int i = 0; i < dailyWorkouts.size(); i++) {

            try {
                Date date = simpleDateFormat.parse(dailyWorkouts.get(i).getDate());
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date);
                highlightedDates.add(calendar1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return highlightedDates;
    }
}