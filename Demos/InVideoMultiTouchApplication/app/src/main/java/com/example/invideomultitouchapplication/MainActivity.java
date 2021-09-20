package com.example.invideomultitouchapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private TextView textViewTitle, textViewDouble, textViewScale;
    private int startTouchX, startTouchY;
    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDouble = findViewById(R.id.textViewDouble);
        textViewScale = findViewById(R.id.textViewScale);

        //Generate gesture Detectors and resgister for doubleTap event
        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);
        scaleGestureDetector = new ScaleGestureDetector(this, new MyOwnScaleGestureDetector());

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getActionMasked(); //or getAction()
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startTouchX = (int) motionEvent.getX();
                        startTouchY = (int) motionEvent.getY();
                        Log.v("TOUCH_APP","Action is DOWN");
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.v("TOUCH_APP","Action is POINTER_DOWN");
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        Log.v("TOUCH_APP","Action is POINTER_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        textViewTitle.setX(motionEvent.getX() - startTouchX);
                        textViewTitle.setY(motionEvent.getY() - startTouchY);
                        Log.v("TOUCH_APP","Action is MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v("TOUCH_APP","Action is UP");
                        break;
                    default:
                        Log.v("TOUCH_APP","Action is OTHER");
                        break;
                }
                return false; //true if consumed. false to pass on to others
            }
        });
    }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            scaleGestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onSingleTapConfirmed");
            textViewDouble.setText("onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onDoubleTap");
            textViewDouble.setText("onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onDoubleTapEvent");
            textViewDouble.setText("onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onDown");
            textViewDouble.setText("onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onShowPress");
            textViewDouble.setText("onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onSingleTapUp");
            textViewDouble.setText("onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.v("TOUCH_APP","Action is onScroll");
            textViewDouble.setText("onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.v("TOUCH_APP","Action is onLongPress");
            textViewDouble.setText("onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.v("TOUCH_APP","Action is onFling");
            textViewDouble.setText("onFling");
            return false;
        }

    private class MyOwnScaleGestureDetector implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            if (scaleFactor>1) {
                Log.v("TOUCH_APP","Action is ZOOM OUT");
                textViewScale.setText("Zoom Out");
            } else{
                Log.v("TOUCH_APP","Action is ZOOM IN");
                textViewScale.setText("Zoom In");
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    }
}