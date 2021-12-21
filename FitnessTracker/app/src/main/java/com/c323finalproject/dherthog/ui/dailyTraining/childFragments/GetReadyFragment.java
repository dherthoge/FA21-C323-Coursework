package com.c323finalproject.dherthog.ui.dailyTraining.childFragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.c323finalproject.dherthog.R;
import com.c323finalproject.dherthog.ui.dailyTraining.ChildFragmentListener;
import com.c323finalproject.dherthog.ui.dailyTraining.DailyTrainingFragment;

public class GetReadyFragment extends Fragment {

    private ChildFragmentListener mListener; // Listener for fragment completion
    private TextView timerTv; // TextView for the countdown timer
    private CountDownTimer countDownTimer;
    private final int GET_READY_TIME_IN_MILLIS = 5000; // Timer duration

    /**
     * @param mListener The listener for fragment completion.
     */
    public GetReadyFragment(ChildFragmentListener mListener) {
        this.mListener = mListener;
    }

    /**
     * Gets a reference for the timer's TextView.
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
        View root = inflater.inflate(R.layout.fragment_get_ready, container, false);

        timerTv = root.findViewById(R.id.tv_get_ready_timer);

        return root;
    }

    /**
     * Starts a countdown timer for the user to get ready.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Start a countdown timer for the length of the mode
        countDownTimer = new CountDownTimer(GET_READY_TIME_IN_MILLIS, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTv.setText(getString(R.string.time_remaining, millisUntilFinished / 1000));
            }

            public void onFinish() {
                mListener.messageFromChildFragment(DailyTrainingFragment.Children.GET_READY);
            }
        }.start();
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