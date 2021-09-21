package com.example.project5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
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
   // int imageBallHeight, imageBallWidth;
    Drawable image;
    PointF translatePoint;
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
//        imageBallHeight = imageBall.getWidth();
//        imageBallHeight = imageBall.getHeight();


        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
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
                return true; //true if consumed. false to pass on to others
            }


        });
        return view;
    }
}