package com.c323finalproject.dherthog.ui.dailyTraining;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.c323finalproject.dherthog.DailyWorkout;
import com.c323finalproject.dherthog.DailyWorkoutDao;
import com.c323finalproject.dherthog.Database;
import com.c323finalproject.dherthog.Exercise;
import com.c323finalproject.dherthog.Mode;
import com.c323finalproject.dherthog.R;
import com.c323finalproject.dherthog.ui.dailyTraining.childFragments.ActivityFragment;
import com.c323finalproject.dherthog.ui.dailyTraining.childFragments.GetReadyFragment;
import com.c323finalproject.dherthog.ui.dailyTraining.childFragments.RestFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyTrainingFragment extends Fragment implements ChildFragmentListener {

    private ProgressBar trainingStatusPb;
    private Button startBtn;
    private TextView endMsgTv;
    private List<Exercise> exercises;
    private Mode mode;
    private DailyWorkoutDao dailyWorkoutDao;
    private int exerciseCounter = 0;
    private static final int MAX_PROGRESS = 100;
    private int progress = 0;
    private Calendar startTime;

    // The possible child Fragments of this Fragment.
    public enum Children {
        GET_READY,
        ACTIVITY,
        REST
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
        View root = inflater.inflate(R.layout.fragment_daily_training, container, false);

        trainingStatusPb = root.findViewById(R.id.pb_daily_training);
        startBtn = root.findViewById(R.id.btn_daily_training_start);
        endMsgTv = root.findViewById(R.id.tv_end_msg);

        return root;
    }

    /**
     * Gets all of the exercises to perform and sets a listener for the start Button. Makes the
     * Button invisible and disabled.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Database database = Database.getInstance(requireContext());

        dailyWorkoutDao = database.getDailyWorkoutDAO();
        exercises = database.getExerciseDAO().getAllExercise();
        mode = database.getModeDAO().getAllMode().get(0);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(false);
                startBtn.setVisibility(View.INVISIBLE);
                startGetReady();
            }
        };
        startBtn.setOnClickListener(listener);
    }

    /**
     * Creates a GetReadyFragment and attaches it to the container. Also begins tracking how long
     * it takes the user to complete the exercise.
     */
    public void startGetReady() {
        startTime = Calendar.getInstance();

        Fragment childFragment = new GetReadyFragment(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.const_lay_child_fragment_container, childFragment).commit();
    }

    /**
     * Creates a ActivityFragment and attaches it to the container.
     */
    public void startActivity() {
        Exercise nextExercise = getNextExercise();

        Fragment childFragment = new ActivityFragment(this, nextExercise, mode.getTime(), progress);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.const_lay_child_fragment_container, childFragment).commit();
    }

    /**
     * Creates a GetReadyFragment and attaches it to the container.
     */
    public void startRest() {
        Fragment childFragment = new RestFragment(this, progress);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.const_lay_child_fragment_container, childFragment).commit();
    }

    /**
     * Displays an end message. Also inserts a new DailyWorkout entry
     * in the database.
     */
    public void startEnd() {
        ConstraintLayout childFragmentContainer = requireView().findViewById(R.id.const_lay_child_fragment_container);
        childFragmentContainer.setEnabled(false);
        childFragmentContainer.setVisibility(View.INVISIBLE);

        trainingStatusPb.setVisibility(View.VISIBLE);
        trainingStatusPb.setProgress(MAX_PROGRESS);
        endMsgTv.setVisibility(View.VISIBLE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdf.format(new Date());
        long totalTimeInMillis = Calendar.getInstance().getTime().getTime() - startTime.getTime().getTime();

        dailyWorkoutDao.insert(new DailyWorkout(date, totalTimeInMillis));
    }

    /**
     * Gets the next Exercise to perform and increments exerciseCounter and progress.
     * @return The next Exercise to perform, or null if there are no more.
     */
    @Nullable
    private Exercise getNextExercise() {
        Exercise nextExercise = exerciseCounter >= exercises.size() ? null : exercises.get(exerciseCounter++);
        progress = exerciseCounter * MAX_PROGRESS / exercises.size();
        return nextExercise;
    }

    /**
     * Called when the user is done with a particular child and navigates to the next fragment.
     * @param child The identifier of the calling child fragment.
     */
    @Override
    public void messageFromChildFragment(Children child) {
        switch (child) {
            case GET_READY:
            case REST:
                startActivity();
                break;
            case ACTIVITY:
                Log.i("progress", ""+progress);
                if (progress == MAX_PROGRESS)
                    startEnd();
                else
                    startRest();
                break;
        }
    }
}
   