package com.example.project5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    View view;
    ImageView imageBall;
    Drawable image;
    PointF point;
    Matrix matrix = new Matrix();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ball, container, false);
        imageBall = view.findViewById(R.id.imageBall);

        matrix.reset();

        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getActionMasked(); //or getAction()
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        point = new PointF(motionEvent.getX(), motionEvent.getY());
                        Log.v("TOUCH_APP","Action is DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        point.x -= motionEvent.getX();
                        point.y -= motionEvent.getX();
                        matrix.postTranslate(point.x, point.y);
                        Log.v("TOUCH_APP","Action is MOVE");
                        break;
                }


                imageBall.setImageMatrix(matrix);
                imageBall.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imageBall.getDrawingCache());
                Canvas canvas = new Canvas(bitmap);
                imageBall.draw(canvas);
                return true; //true if consumed. false to pass on to others
            }


        });
        return view;
    }
}