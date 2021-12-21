package com.c323finalproject.dherthog.ui.dailyTraining.childFragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.c323finalproject.dherthog.Exercise;
import com.c323finalproject.dherthog.ImageManager;
import com.c323finalproject.dherthog.R;
import com.c323finalproject.dherthog.ui.dailyTraining.ChildFragmentListener;
import com.c323finalproject.dherthog.ui.dailyTraining.DailyTrainingFragment;

public class ActivityFragment extends Fragment {

    private final ChildFragmentListener mListener; // Listener for fragment completion
    private final Exercise exercise;
    private final int exerciseTimeInSeconds, progress;
    private int timeInMilliseconds;
    private ProgressBar trainingStatusPb;
    private TextView nameTv, timerTv;
    private ImageView exerciseIv;
    private Button startDoneBtn;
    private CountDownTimer countDownTimer;

    /**
     * @param mListener The listener for fragment completion.
     * @param exercise The exercise to display.
     * @param exerciseTimeInSeconds The amount of time the exercise should last in seconds
     * @param progress The percentage of exercises the user has completed (0% - 100%)
     */
    public ActivityFragment(ChildFragmentListener mListener, Exercise exercise, int exerciseTimeInSeconds, int progress) {
        this.mListener = mListener;
        this.exercise = exercise;
        this.exerciseTimeInSeconds = exerciseTimeInSeconds;
        this.progress = progress;
        Log.i("progress", ""+progress);
    }

    /**
     * Gets a reference for the necessary Views.
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
        View root = inflater.inflate(R.layout.fragment_activity, container, false);

        // Get references for necessary TextViews, ImageView, and Button
        trainingStatusPb = root.findViewById(R.id.pb_daily_training);
        nameTv = root.findViewById(R.id.tv_activity_name);
        timerTv = root.findViewById(R.id.tv_activity_timer);
        exerciseIv = root.findViewById(R.id.iv_activity_image);
        startDoneBtn = root.findViewById(R.id.btn_rest_skip);

        return root;
    }

    /**
     * Sets values for views and sets an OnClickListener for startDoneBtn.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Set values for progress bar
        trainingStatusPb.setProgress(progress);

        // Set values for other Views
        nameTv.setText(exercise.getName());
        timerTv.setText(Integer.toString(exerciseTimeInSeconds));
        timeInMilliseconds = exerciseTimeInSeconds * 1000;
        exerciseIv.setImageBitmap(ImageManager.readImage(exercise.getImageFilePath()));

        // Check if the timer should be started or stopped, and notifies parent of completion
        startDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if the timer has started already
                if(startDoneBtn.getText().toString().equals(getResources().getString(R.string.start))) {
                    // Start a countdown timer for the length of the mode
                    countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {

                        public void onTick(long millisUntilFinished) {
                            timerTv.setText(getString(R.string.time_remaining, millisUntilFinished / 1000));
                        }

                        public void onFinish() {
                            // End this Fragment
                            mListener.messageFromChildFragment(DailyTrainingFragment.Children.ACTIVITY);
                        }
                    }.start();
                }
                else {
                    // Kill the CountDownTimer
                    countDownTimer.cancel();

                    // End this Fragment
                    mListener.messageFromChildFragment(DailyTrainingFragment.Children.ACTIVITY);
                }

                // Change the button to show done
                startDoneBtn.setText(R.string.done);
            }
        });
    }

    /**
     * Cancels timer before de-referencing dependant Views.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}