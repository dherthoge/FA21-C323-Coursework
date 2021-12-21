package com.example.project5;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

/**
 * Displays a ball that the user can fling around the screen.
 */
public class BallFragment extends Fragment {

    // Global variables for storing various objects
    private Updatable activityCommunicator;
    private View view;
    private ImageView imageBall;
    private GestureDetector gestureDetector;
    private GestureListener gestureListener;
    private View.OnTouchListener onTouchListener;
    private ScaleGestureDetector scaleGestureDetector;
    private PointF originalPoint;
    private float currentDx, currentDy;


    /**
     * Interface to notify listeners of user flings/gestures.
     */
    public interface Updatable {
        public void updateBallMovement(float dx, float dy);
        public void updateGesture(String logMsg);
    }

    /**
     * Class to listen for user gestures.
     */
    public class GestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        /* Most gesture logs are commented out so they don't overwhelm the log, but if you want to
        * log every gesture just uncomment the lines you want */

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event of the single-tap.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
//            activityCommunicator.updateGesture("You single tap confirmed\n");
            return false;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event of the first tap of the double-tap.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            activityCommunicator.updateGesture("You double tapped\n");
            return false;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The motion event that occurred during the double-tap gesture.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
//            activityCommunicator.updateGesture("You double tap evented\n");
            return false;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
//            activityCommunicator.updateGesture("You downed\n");
            return true;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The up motion event that completed the first tap
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
//            activityCommunicator.updateGesture("You single tap upped\n");
            return false;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event
         */
        @Override
        public void onShowPress(MotionEvent motionEvent) {
            activityCommunicator.updateGesture("You show pressed\n");
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event of the first tap of the double-tap.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            activityCommunicator.updateGesture("You scrolled\n");
            return false;
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The initial on down motion event that started the longpress.
         */
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            activityCommunicator.updateGesture("You long pressed\n");
        }

        /**
         * Notifies listeners of the MotionEvent.
         * @param motionEvent The down motion event of the first tap of the double-tap.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            activityCommunicator.updateGesture("You flung\n");
            return false;
        }
    }

    public class OnTouchListener implements View.OnTouchListener {

        /**
         * Calculates the direction the user wants to move the ball and translates it.
         * @param view The View the event originated from
         * @param motionEvent The user's MotionEvent
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getActionMasked();
            switch (action) {

                // Stores the original touch coords
                case MotionEvent.ACTION_DOWN:
                    originalPoint = new PointF(motionEvent.getX(), motionEvent.getY());
                    break;

                // Calculates the delta of the start and stop coords
                case MotionEvent.ACTION_UP:
                    float newX = motionEvent.getX();
                    float newY = motionEvent.getY();
                    currentDx = newX - originalPoint.x;
                    currentDy = newY - originalPoint.y;
                    translateImage();
                    break;
            }
            return false;
        }
    }

    /**
     * Creates the Fragment.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Stores a reference of the Activity listening for updates.
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (Updatable) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement SettingsFragmentInterface");
        }
    }

    /**
     * Creates a heirarchy of views representing the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                 fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                  attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ball, container, false);
        imageBall = view.findViewById(R.id.imageBall);

        // Sets up gestureListener
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(getActivity(), gestureListener);
        gestureDetector.setOnDoubleTapListener(gestureListener);

        // Sets up the listener for the ball
        onTouchListener = new OnTouchListener();
        imageBall.setOnTouchListener(onTouchListener);

        // Sets up touch listener for the fragment
        view.setOnTouchListener(new View.OnTouchListener() {

            /**
             * Passes the MotionEvent to gestureDetector and onTouchListener.
             * @param view The View the event originated from
             * @param motionEvent The user's MotionEvent
             * @return true if the event is consumed, else false
             */
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                onTouchListener.onTouch(view, motionEvent);
                return true;
            }
        });

        return view;
    }

    /**
     * Translates the ball using the currentDx and currentDy.
     */
    private void translateImage() {
        ViewPropertyAnimator vpa = imageBall.animate();
        vpa.translationXBy(currentDx);
        vpa.translationYBy(currentDy);

        activityCommunicator.updateBallMovement(currentDx, currentDy);
    }

    /**
     * Gets the the coordinates of the ball in the previous instance and moves the current ball
     * (the actual ImageView) to that location.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            currentDx = savedInstanceState.getFloat("DX");
            currentDy = savedInstanceState.getFloat("DY");
            translateImage();
        }
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * Saves the location of the current ball
     * @param outState Bundle: Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putFloat("DX", currentDx);
        outState.putFloat("DY", currentDy);
        super.onSaveInstanceState(outState);
    }
}