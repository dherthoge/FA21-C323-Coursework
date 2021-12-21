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
 * A fragment to add a camp site to the Travel Guide.
 */
public class CampsiteFragment extends Fragment {

    // Request code for image capture
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 10;

    // Necessary variables for making toasts/communicating with activity/creating the item
    private Context context;
    private Updatable activityCommunicator;
    private ImageView campingIv;
    private Bitmap campingBitmap;

    /**
     * Creates a CampingFragment.
     * @param context The context of the application
     * @param activityCommunicator An Object that implements the Updatable interface. Allows the
     *                             fragment to notify it's activity if a City has been added or not
     */
    public CampsiteFragment(Context context, Updatable activityCommunicator) {
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
        // Inflates the fragment_camping layout
        View viewHierarchy = inflater.inflate(R.layout.fragment_camping, container, false);

        // Stores a reference to the camping ImageView
        campingIv = viewHierarchy.findViewById(R.id.iv_camping);

        setClickListeners(viewHierarchy);


        return viewHierarchy;
    }

    /**
     * Sets click listeners for each button in the fragment.
     * @param viewHierarchy The hierarchy of Views containing Views to set click listeners for
     */
    private void setClickListeners(View viewHierarchy) {
        // Sets a click listener for capturing an image
        Button captureImageBtn = viewHierarchy.findViewById(R.id.btn_camping_capture_image);
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

        // Sets a click listener for canceling Campsite creation
        Button cancelBtn = viewHierarchy.findViewById(R.id.btn_camping_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies it's parent activity a Camping item will not be added.
             * @param view The clicked View
             */
            @Override
            public void onClick(View view) {
                activityCommunicator.cancelClicked();
            }
        });

        // Sets a click listener for adding a new Campsite
        Button addBtn = viewHierarchy.findViewById(R.id.btn_camping_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Notifies it's parent activity a Camping item has been added.
             * @param view The clicked View
             */
            @Override
            public void onClick(View view) {

                // Gets references for user input
                EditText nameEt = viewHierarchy.findViewById(R.id.et_camping_name);
                EditText timeToVisitEt = viewHierarchy.findViewById(R.id.et_camping_time_to_visit);
                EditText nearestLocationEt = viewHierarchy.findViewById(R.id.et_camping_nearest_location);
                EditText locationEt = viewHierarchy.findViewById(R.id.et_camping_location);

                // Extracts user input from EditTexts
                String name = nameEt.getText().toString();
                String timeToVisit = timeToVisitEt.getText().toString();
                String nearestMetroLocation = nearestLocationEt.getText().toString();
                String location = locationEt.getText().toString();

                /* Determine if the user entered a valid input in each field. If not, does not save
                 * the event
                 */
                if (campingBitmap == null) {
                    Toast.makeText(context, "You must take a picture for the camp site!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(context, "You must enter the name of the camp site!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (timeToVisit.equals("")) {
                    Toast.makeText(context, "You must enter the best time to visit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nearestMetroLocation.equals("")) {
                    Toast.makeText(context, "You must enter the nearest metropolitan location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (location.equals("")) {
                    Toast.makeText(context, "You must enter the location of the campsite!", Toast.LENGTH_SHORT).show();
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

                // Creates a new Campsite and adds it to the stored list of Campsites
                Campsite newCampsite = new Campsite(name, timeToVisit, nearestMetroLocation, location, longitude, latitude, campingBitmap);
                ItemManager itemManager = ItemManager.getInstance(context);
                itemManager.addCampsite(newCampsite);

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
                    setCampingPic(data);
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
    private void setCampingPic(@Nullable Intent data) {
        // I found some of this code (rotating the bitmap) at https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap campingPic = (Bitmap) data.getExtras().get("data");

        Bitmap scaledCampingPic = Bitmap.createScaledBitmap(campingPic, campingPic.getWidth(), campingPic.getHeight(), true);
        Bitmap rotatedCampingPic = Bitmap.createBitmap(scaledCampingPic, 0, 0, scaledCampingPic.getWidth(), scaledCampingPic.getHeight(), matrix, true);

        campingBitmap = rotatedCampingPic;
        campingIv.setImageBitmap(rotatedCampingPic);
    }
}
