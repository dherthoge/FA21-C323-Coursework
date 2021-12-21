package com.c323finalproject.dherthog.ui.modeOfTraining;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.c323finalproject.dherthog.Database;
import com.c323finalproject.dherthog.Mode;
import com.c323finalproject.dherthog.ModeDao;
import com.c323finalproject.dherthog.R;
import com.c323finalproject.dherthog.SignInActivity;
import com.c323finalproject.dherthog.databinding.FragmentModeOfTrainingBinding;
import com.c323finalproject.dherthog.ui.timePicker.TimePickerFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 */
public class ModeOfTrainingFragment extends Fragment {

    private boolean switchJustChecked = false;

    private ModeDao modeDAO;
    private Mode mode;
    private RadioGroup radioGroup;
    private Button saveModeBtn;
    private SwitchMaterial alarmSw;
    private TextView alarmTimeTv;
    private AlarmManager alarmManager;

    /**
     * Obtains references for necessary Views.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                 fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                  attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mode_of_training, container, false);

        radioGroup = root.findViewById(R.id.radio_group);
        saveModeBtn = root.findViewById(R.id.btn_save_mode);
        alarmSw = root.findViewById(R.id.sw_alarm_state);
        alarmTimeTv = root.findViewById(R.id.tv_alarm_time);

        return root;
    }

    /**
     * Initializes the radio group and sets click listeners for radio group, save button, and alarm
     * switch.
     * @param view The root View
     * @param savedInstanceState Used for configuration changes
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Get the current mode if the user has changed it from Easy
        modeDAO = Database.getInstance(requireActivity().getApplicationContext()).getModeDAO();
        List<Mode> modes = modeDAO.getAllMode();

        // Get the current mode, there will only ever be one
        mode = modes.get(0);

        // Determine which button should be checked
        switch (mode.getMode()) {
            case 0:
                radioGroup.check(R.id.rb_easy);
                break;
            case 1:
                radioGroup.check(R.id.rb_medium);
                break;
            case 2:
                radioGroup.check(R.id.rb_hard);
                break;
            case 3:
                radioGroup.check(R.id.rb_custom);
                break;
        }

        // Set an listener to detect check changes
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_easy:
                        mode.setMode(0);
                        break;
                    case R.id.rb_medium:
                        mode.setMode(1);
                        break;
                    case R.id.rb_hard:
                        mode.setMode(2);
                        break;
                    case R.id.rb_custom:
                        mode.setMode(3);
                        break;
                }

                Log.i("updateMode", "Current Mode: " + modeDAO.getAllMode().get(0).getMode());
            }
        });

        // On click listener to save selected mode to db and notify user
        saveModeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modeDAO.update(mode);

                        String modeName = "Easy";
                        switch (mode.getMode()) {
                            case 1: modeName = "Medium"; break;
                            case 2: modeName = "Hard"; break;
                            case 3: modeName = "Custom"; break;
                        }

                        Snackbar.make(view, "Mode saved: " + modeName, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );


        // Set listener for alarmTgl
        alarmSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                // TimePicker is already showing
                if (switchJustChecked) {
                    switchJustChecked = false;
                    return;
                }

                // Open time picker and set alarm for that time
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (!alarmManager.canScheduleExactAlarms()) {
                            startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM), null);
                            return;
                        }
                    }

                    DialogFragment dialogFragment = new TimePickerFragment(ModeOfTrainingFragment.this.onTimeSetListener);
                    dialogFragment.show(requireActivity().getSupportFragmentManager(), "Set Time");
                }

                alarmSw.setText(getString(R.string.alarm_off));
                alarmSw.setChecked(false);
            }
        });
    }

    /**
     * Sets up an alarm to notify the user when to work out.
     */
    private final OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
        /**
         * Creates an alarm for the given time.
         * @param timePicker The instance of the TimePicker
         * @param hourOfDay The hour the user picked (24hr)
         * @param minute The minute the user picked
         */
        @SuppressLint("DefaultLocale")
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            // Calculate when the alarm should go off
            Date date = new Date();
            Calendar cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            cal_now.setTime(date);
            cal_alarm.setTime(date);
            cal_alarm.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cal_alarm.set(Calendar.MINUTE,minute);
            cal_alarm.set(Calendar.SECOND,0);

            // Can't set a time in the past, notify user and uncheck the switch
            if(cal_alarm.before(cal_now)) {
                Snackbar.make(requireView(), getString(R.string.invalid_time_msg), Snackbar.LENGTH_SHORT).show();
                alarmSw.setChecked(false);
                return;
            }


//            Intent myIntent = new Intent(requireContext(), receiver);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
//
            long alarmMillis = cal_alarm.getTimeInMillis() - cal_now.getTimeInMillis();
//
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 5000, pendingIntent);


            // Update the UI
            alarmSw.setText(getString(R.string.alarm_on));
            alarmSw.setChecked(true);
            switchJustChecked = true;
            alarmTimeTv.setText(String.format("%d:%02d %s", hourOfDay % 12, minute, (minute <= 12 ? "AM" : "PM")));
            Log.i("millisToAlarm", "" + alarmMillis);
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Log.i("alarm", "Alarm received!");

            Intent openApp = new Intent(requireContext(), SignInActivity.class);
            openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, openApp, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), getString(R.string.CHANNEL_ID))
                    .setSmallIcon(R.drawable.ic_menu_exercise)
                    .setContentTitle("Fitness Tracker")
                    .setContentText("IT'S TIME TO WORK OUT!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }
    };
}