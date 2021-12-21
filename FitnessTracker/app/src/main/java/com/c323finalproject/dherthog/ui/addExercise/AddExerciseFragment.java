package com.c323finalproject.dherthog.ui.addExercise;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.c323finalproject.dherthog.DailyWorkout;
import com.c323finalproject.dherthog.Database;
import com.c323finalproject.dherthog.Exercise;
import com.c323finalproject.dherthog.ExerciseDao;
import com.c323finalproject.dherthog.ImageManager;
import com.c323finalproject.dherthog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A Fragment to add new exercises to the database.
 */
public class AddExerciseFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private EditText nameEdt;
    private ImageView exerciseIv;
    private Button captureImageButton;
    private Button saveImageButton;
    private ExerciseDao exerciseDao;
    private final int REQUEST_IMAGE_CAPTURE = 42;

    /**
     * Gets a reference for necessary Views.
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
        View root = inflater.inflate(R.layout.fragment_add_exercise, container, false);


        nameEdt = root.findViewById(R.id.edt_add_exercise_name);
        exerciseIv = root.findViewById(R.id.iv_add_exercise_image);
        exerciseIv.setImageDrawable(null);

        captureImageButton = root.findViewById(R.id.btn_capture_image);
        saveImageButton = root.findViewById(R.id.btn_save_exercise);

        return root;
    }

    /**
     * Gets an instance of ExerciseDao and sets listeners for captureImageButton and
     * saveImageButton.
     * @param view The View returned by onCreateView(android.view.LayoutInflater,
     *             android.view.ViewGroup, android.os.Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here. This value may be null.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        exerciseDao = Database.getInstance(getContext()).getExerciseDAO();

        // Set click listener to launch the camera
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        // Checks fields to see if user entered name and image, then inserts new Exercise into the
        // db
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user entered a name and image
                if (nameEdt.getText().equals("") || exerciseIv.getDrawable() == null)
                    return;


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
                String filename = requireContext().getFilesDir() + "/exerciseImages/image_" + simpleDateFormat.format(new Date()) + ".jpeg";

                Bitmap bitmap = ((BitmapDrawable) exerciseIv.getDrawable()).getBitmap();
                ImageManager.writeImage(filename, bitmap);

                Exercise exercise = new Exercise(nameEdt.getText().toString(), filename);
                exerciseDao.insert(exercise);

                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    /**
     * Checks if the result came from the camera and updates exerciseIv if it did.
     * @param requestCode The integer request code originally supplied to startActivityForResult()
     * @param resultCode -1 if the result is okay
     * @param data An intent with data from the exiting Activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    exerciseIv.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    break;
                default:
                    break;
            }
        }
    }
}