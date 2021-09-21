package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

public class GestureActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    LogFragment logFragment;
    String log = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        logFragment = new LogFragment();
        fragmentTransaction.replace(R.id.linear_layout_log_fragment, logFragment);
        fragmentTransaction.commit();


        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);
//        scaleGestureDetector = new ScaleGestureDetector(this, new MyOwnScaleGestureDetector());
    }

    /**
     * Replaces the currently attached SettingsFragment with an identical one, but sends the
     * currently selected setting.
     * @param setting The currently active Setting
     */
    private void replaceLogFragment() {
        // Creates a Bundle to pass to the newly created SettingsFragment
        Bundle curSettingBundle = new Bundle();
        curSettingBundle.putString("LOG", log);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        logFragment = new LogFragment();
        logFragment.setArguments(curSettingBundle);
        fragmentTransaction.replace(R.id.linear_layout_log_fragment, logFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
//        scaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        log = "onSingleTapConfirmed\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        log = "onDoubleTap\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        log = "onDoubleTapEvent\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        log = "onDown\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        log = "onShowPress\n" + log;
        replaceLogFragment();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        log = "onSingleTapUp\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        log = "onScroll\n" + log;
        replaceLogFragment();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        log = "onLongPress\n" + log;
        replaceLogFragment();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        log = "onFling\n" + log;
        replaceLogFragment();
        return false;
    }
}