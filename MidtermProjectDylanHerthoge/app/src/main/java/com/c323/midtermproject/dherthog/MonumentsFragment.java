package com.c323.midtermproject.dherthog;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment to add a monument to the Travel Guide.
 */
public class MonumentsFragment extends Fragment {

    // Request code for image capture
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 10;

    // Necessary variables for making toasts/communicating with activity/creating the item
    private Context context;
    private Updatable activityCommunicator;
    private ImageView monuIv;
    private Bitmap monuBitmap;


    /**
     * Creates a MonumentsFragment.
     * @param context The context of the application
     * @param activityCommunicator An Object that implements the Updatable interface. Allows the
     *                             fragment to notify it's activity if a City has been added or not
     */
    public MonumentsFragment(Context context, Updatable activityCommunicator) {
        this.context = context;
        this.activityCommunicator = activityCommunicator;
    }

    /**
     * Creates a hierarchy of Views for the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                 attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Return the View for the fragment's UI, or null
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates the fragment_monuments layout
        View viewHierarchy = inflater.inflate(R.layout.fragment_monuments, container, false);

        // Stores a reference to the monuments ImageView
        monuIv = viewHierarchy.findViewById(R.id.iv_monu);

        setClickListeners(viewHierarchy);


        return viewHierarchy;
    }

    /**
     * Sets click listeners for each button in the fragment.
     * @param viewHierarchy The hierarchy of Views containing Views to set click listeners for
     */
    private void setClickListeners(View viewHierarchy) {
        // Sets a click listener for capturing an image
        Button captureImageBtn = viewHierarchy.findViewById(R.id.btn_monu_capture_image);
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts an Intent to capture an image.
             * @param view The clicked View
             */
            @Override
            public void onClick(View view) {
                if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    try {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, REQUEST_CODE_CAPTURE_IMAGE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else view.setEnabled(false);
            }
        });

        // Sets a click listener for canceling Monument creation
        Button cancelBtn = viewHierarchy.findViewById(R.id.btn_monu_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies it's parent activity a City item will not be added.
             * @param view The clicked View
             */
            @Override
            public void onClick(View view) {
                activityCommunicator.cancelClicked();
            }
        });

        // Sets a click listener for adding a new Monument
        Button addBtn = viewHierarchy.findViewById(R.id.btn_monu_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies it's parent activity a City item has been added.
             * @param view The clicked View
             */
            @Override
            public void onClick(View view) {

                // Gets references for user input
                EditText nameEt = viewHierarchy.findViewById(R.id.et_monu_name);
                EditText historyEt = viewHierarchy.findViewById(R.id.et_monu_history);
                EditText timeToVisitEt = viewHierarchy.findViewById(R.id.et_monu_time_to_visit);
                EditText ticketPriceEt = viewHierarchy.findViewById(R.id.et_monu_ticket_price);
                EditText locationEt = viewHierarchy.findViewById(R.id.et_monu_location);

                // Extracts user input from EditTexts
                String name = nameEt.getText().toString();
                String history = historyEt.getText().toString();
                String timeToVisit = timeToVisitEt.getText().toString();
                String ticketPriceString = ticketPriceEt.getText().toString();
                String location = locationEt.getText().toString();

                // Parses a double from the user. If not formatted correctly, notifies the user
                Double ticketPrice = -1.0;
                try {
                    ticketPrice = Double.parseDouble(ticketPriceString);
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();

                    Toast.makeText(context, "Ticket price must be a double!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /* Determine if the user entered a valid input in each field. If not, does not save
                 * the event
                 */
                if (monuBitmap == null) {
                    Toast.makeText(context, "You must take a picture for the monument!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(context, "You must enter the name of the monument!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (history.equals("")) {
                    Toast.makeText(context, "You must enter the history of the monument!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (timeToVisit.equals("")) {
                    Toast.makeText(context, "You must enter the best time to visit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ticketPrice < 0) {
                    Toast.makeText(context, "Ticket price must be double that is at least 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (location.equals("")) {
                    Toast.makeText(context, "You must enter the location of the monument!", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Creates a new Geocoder and attempts to resolve it to an Address
                Geocoder geocoder = new Geocoder(context);
                ArrayList<Address> addresses = null;
                try {
                    addresses = ((ArrayList<Address>) geocoder.getFromLocationName(location, 1));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Geocoder unavailable, default address used!", Toast.LENGTH_SHORT).show();
                }

                // If Geocoder could not resolve the given location, makes the user enter a new location
                if (addresses.size() < 1) {
                    Toast.makeText(context, "Could not resolve the given location! Try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Gets the lon and lat of the given location
                double longitude = addresses.get(0).getLongitude();
                double latitude = addresses.get(0).getLatitude();

                // Creates a new Monument and adds it to the stored list of Monuments
                Monument newMonument = new Monument(name, history, timeToVisit, ticketPrice, location, longitude, latitude, monuBitmap);
                ItemManager itemManager = ItemManager.getInstance(context);
                itemManager.addMonument(newMonument);

                activityCommunicator.addClicked();
            }
        });
    }

    /**
     * Determines if the result is okay, then determines what to do with the received data.
     * @param requestCode An integer representing the completed request
     * @param resultCode An integer signifying if the request was completed successfully
     * @param data Data from the request, or null if not completed successfully
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE:
                    setMonumentPic(data);
                    break;
                default:
                    Toast.makeText(context, "Unknown request code!", Toast.LENGTH_SHORT).show();
                    Log.i("onActivityResult", "Unknown request code!");
                    break;
            }
        }
    }

    /**
     * Displays the captured Image and stores it.
     * @param data An Intent containing a Bitmap
     */
    private void setMonumentPic(@Nullable Intent data) {
        // I found some of this code (rotating the bitmap) at https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap monumentPic = (Bitmap) data.getExtras().get("data");

        Bitmap scaledMonumentPic = Bitmap.createScaledBitmap(monumentPic, monumentPic.getWidth(), monumentPic.getHeight(), true);
        Bitmap rotatedMonumentPic = Bitmap.createBitmap(scaledMonumentPic, 0, 0, scaledMonumentPic.getWidth(), scaledMonumentPic.getHeight(), matrix, true);

        monuBitmap = rotatedMonumentPic;
        monuIv.setImageBitmap(rotatedMonumentPic);
    }
}
