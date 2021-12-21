package com.c323finalproject.dherthog.ui.performExercise;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.c323finalproject.dherthog.ImageManager;
import com.c323finalproject.dherthog.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformExerciseFragment extends Fragment {

    private TextView nameTv, timerTv;
    private ImageView exerciseIv;
    private Button startDoneBtn;
    private int timeInMilliseconds;
    private CountDownTimer countDownTimer;

    /**
     * Obtains references for necessary Views.
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
        View root = inflater.inflate(R.layout.fragment_perform_exercise, container, false);

        // Get references for necessary TextViews, ImageView, and Button
        nameTv = root.findViewById(R.id.tv_perform_exercise_name);
        timerTv = root.findViewById(R.id.tv_perform_exercise_timer);
        exerciseIv = root.findViewById(R.id.iv_perform_exercise_image);
        startDoneBtn = root.findViewById(R.id.btn_perform_exercise_start_done);

        return root;
    }

    /**
     * Populates information about the Exercise and Mode and sets a listener for the start/done
     * button.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null)
            return;

        // Set values for Views
        nameTv.setText(getArguments().getString("exerciseName"));
        timerTv.setText(getArguments().getString("time"));
        timeInMilliseconds = Integer.parseInt(getArguments().getString("time")) * 1000;
        exerciseIv.setImageBitmap(ImageManager.readImage(getArguments().getString("filePath")));

        // Set listener for startDoneBtn
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
                            Navigation.findNavController(view).popBackStack();
                        }
                    }.start();
                }
                else {
                    // Toast the user for finishing the exercise
                    Snackbar.make(view, "Great job!", Snackbar.LENGTH_LONG).show();

                    // Kill the CountDownTimer
                    countDownTimer.cancel();

                    // Navigate back to ExercisesFrgment
                    Navigation.findNavController(view).popBackStack();
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