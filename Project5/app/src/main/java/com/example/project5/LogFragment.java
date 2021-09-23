package com.example.project5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Displays the log of user gestures on the ball.
 */
public class LogFragment extends Fragment {

    // Global variables for storing various objects
    View view;
    TextView textLog;
    String log = "";

    /**
     * Gets the arguments passed during Fragment creation.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) log = getArguments().getString("LOG");
        super.onCreate(savedInstanceState);
    }

    /**
     * Initializes all View variables and sets the log's text.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                 fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                  attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null
     * @param savedInstanceState Bundle: If non-null, this fragment is being re-constructed from a
     *                           previous saved state as given here.
     * @return The inflated View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log, container, false);
        textLog = view.findViewById(R.id.textLog);

        textLog.setText(log);
        return view;
    }

    /**
     * Gets the log from the previous instance and displays it.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) textLog.setText(savedInstanceState.getString("LOG"));
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * Saves the data currently in the log.
     * @param outState Bundle: Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("LOG", log);
        super.onSaveInstanceState(outState);
    }
}