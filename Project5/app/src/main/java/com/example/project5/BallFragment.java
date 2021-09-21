package com.example.project5;
// TODO: 9/21/2021
// - ball translation
// - gestures
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BallFragment extends Fragment {

    public interface Updatable {
        public void update(String logMsg);
    }

    private Updatable activityCommunicator;
    private View view;
    private ImageView imageBall;
    private GestureDetector gestureDetector;
    private GestureListener gestureListener;
    private View.OnTouchListener onTouchListener;
    private ScaleGestureDetector scaleGestureDetector;
    private PointF translatePoint;

    public class GestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
//            Log.i("onSingleTapConfirmed: ", "here");
            activityCommunicator.update("onSingleTapConfirmed\n");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
//            Log.i("onDoubleTap: ", "here");
            activityCommunicator.update("onDoubleTap\n");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
//            Log.i("onDoubleTapEvent: ", "here");
            activityCommunicator.update("onDoubleTapEvent\n");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
//            Log.i("onDown: ", "here");
            activityCommunicator.update("onDown\n");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
//            Log.i("onShowPress: ", "here");
            activityCommunicator.update("onShowPress\n");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
//            Log.i("onSingleTapUp: ", "here");
            activityCommunicator.update("onSingleTapUp\n");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.i("onScroll: ", "here");
            activityCommunicator.update("onScroll\n");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
//            Log.i("onLongPress: ", "here");
            activityCommunicator.update("onLongPress\n");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.i("onFling: ", "here");
            activityCommunicator.update("onFling\n");
            return false;
        }
    }

    public class OnTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getActionMasked(); //or getAction()
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    translatePoint = new PointF(motionEvent.getX(), motionEvent.getY());
                    Log.v("TOUCH_APP","Action is DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    translatePoint.x = motionEvent.getX();
                    translatePoint.y = motionEvent.getY();
                    Log.v("TOUCH_APP","Action is MOVE");
                    break;
            }


            // The following code was partially found at https://www.dev2qa.com/android-imageview-matrix-rotate-scale-skew-translate-example/
            BitmapDrawable originalBitmapDrawable = (BitmapDrawable) imageBall.getDrawable();
            final Bitmap originalBitmap = originalBitmapDrawable.getBitmap();
            final int imageBallWidth = originalBitmap.getWidth();
            final int imageBallHeight = originalBitmap.getHeight();
            final Bitmap.Config originalImageConfig = originalBitmap.getConfig();

            Log.i("onTouch", "imageBallWidth: " + imageBallWidth + "  imageBallHeight: " + imageBallHeight + "  translatePoint.x: " + translatePoint.x + "  translatePoint.y: " + translatePoint.y);
            Log.i("onTouch", "imageX: " + imageBall.getX() + "imageY: " + imageBall.getY());
            // According to the skew ratio of the picture, calculate the size of the image after the transformation.
            Bitmap translateBitmap = Bitmap.createBitmap(imageBallWidth, imageBallHeight, originalImageConfig);

            Canvas translateCanvas = new Canvas(translateBitmap);

            Matrix translateMatrix = new Matrix();

            // Set x y translate value..
            translateMatrix.setTranslate(Math.abs(translatePoint.x-imageBall.getX()), Math.abs(translatePoint.y-imageBall.getY()));

            translateCanvas.drawBitmap(originalBitmap, translateMatrix, null);
            imageBall.setImageBitmap(translateBitmap);
            return false; //true if consumed. false to pass on to others
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Gets a reference to MainActivity.
     * @param confxt The current state of the app
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ball, container, false);
        imageBall = view.findViewById(R.id.imageBall);


        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(getActivity(), gestureListener);
        gestureDetector.setOnDoubleTapListener(gestureListener);
//        scaleGestureDetector = new ScaleGestureDetector(this, new MyOwnScaleGestureDetector());

        onTouchListener = new OnTouchListener();
        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        frameLayout.setOnTouchListener(onTouchListener);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                onTouchListener.onTouch(view, motionEvent);
                return true;
            }
        });
        return view;
    }



}