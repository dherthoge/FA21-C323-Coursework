package com.c323.midtermproject.dherthog;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An menu Activity for the Travel Guide app.
 */
public class MainActivity extends AppCompatActivity {

    // Request code for image capture
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 10;

    // References to necessary Views
    private EditText edtTxtName;
    private ImageButton imgBtnProfilePic;
    private String profilePicURI;

    /**
     * Gets ids of necessary Views and loads the user's information.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getViewIDs();
        loadUserData();

    }

    /**
     * Looks up the IDs of the views in activity_main.xml.
     */
    private void getViewIDs() {
        edtTxtName = findViewById(R.id.et_name);
        imgBtnProfilePic = findViewById(R.id.img_btn_profile_pic);
    }

    /**
     * Loads the user's name and profile picture from Shared Preferences and MediaStore.
     */
    private void loadUserData() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.c323.midtermproject.dherthog", Context.MODE_PRIVATE);

        String name = prefs.getString("NAME", null);
        if (name != null)
            edtTxtName.setText(name);

        profilePicURI = prefs.getString("PROFILE_PIC", null);
        try {
            Uri uri = Uri.parse(profilePicURI);
            if (profilePicURI != null) {
                    Bitmap profilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgBtnProfilePic.setImageBitmap(profilePic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the user's name to Shared Preferences.
     * @param view The clicked View
     */
    public void saveInfo(View view) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.c323.midtermproject.dherthog", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean nameSaved = false;
        boolean picSaved = false;

        String name = edtTxtName.getText().toString();
        if (!name.equals("")) {
            editor.putString("NAME", name);
            nameSaved = editor.commit();
        }

        if (profilePicURI != null) {
            editor.putString("PROFILE_PIC", profilePicURI);
            picSaved = editor.commit();
        }

        Log.i("saveInfo", "Name saved: " + nameSaved);
        Log.i("saveInfo", "Picture saved: " + picSaved);
        if (nameSaved && picSaved)
            Toast.makeText(this, "Name and profile picture saved!", Toast.LENGTH_SHORT).show();
        else if (nameSaved)
            Toast.makeText(this, "Name saved!", Toast.LENGTH_SHORT).show();
        else if (picSaved)
            Toast.makeText(this, "Profile picture saved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Brings up an activity to allow the user to choose their profile picture from a photo storage
     * app.
     * @param view The clicked View
     */
    public void changeProfilePicture(View view) {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            try {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_CODE_CAPTURE_IMAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else view.setEnabled(false);
    }

    /**
     * Determines if the result is okay, then determines what to do with the received data.
     * @param requestCode An integer representing the completed request
     * @param resultCode An integer signifying if the request was completed successfully
     * @param data Data from the request, or null if not completed successfully
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE:
                    saveNewProfilePic(data);
                    break;
                default:
                    Toast.makeText(this, "Unknown request code!", Toast.LENGTH_SHORT).show();
                    Log.i("onActivityResult", "Unknown request code!");
                    break;
            }
        }
    }

    /**
     * Sets the user's profile picture.
     * @param data An Intent containing a Bitmap
     */
    private void saveNewProfilePic(@Nullable Intent data) {
        // I found some of this code (rotating the bitmap) at https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap newProfilePic = (Bitmap) data.getExtras().get("data");
        Bitmap scaledNewProfilePic = Bitmap.createScaledBitmap(newProfilePic, newProfilePic.getWidth(), newProfilePic.getHeight(), true);

        Bitmap rotatedNewProfilePic = Bitmap.createBitmap(scaledNewProfilePic, 0, 0, scaledNewProfilePic.getWidth(), scaledNewProfilePic.getHeight(), matrix, true);
        imgBtnProfilePic.setImageBitmap(rotatedNewProfilePic);

        profilePicURI = MediaStore.Images.Media.insertImage(getContentResolver(), rotatedNewProfilePic, "PROFILE_PICTURE", "The user's profile picture");
    }

    /**
     * Starts new activity to add a city item.
     * @param view The clicked View
     */
    public void addCity(View view) {
        Intent intent = new Intent(this, ItemAddActivity.class);
        intent.putExtra("CATEGORY", "CITY");
        startActivity(intent);
    }

    /**
     * Starts new activity to add a historical monument item.
     * @param view The clicked View
     */
    public void addMonument(View view) {
        Intent intent = new Intent(this, ItemAddActivity.class);
        intent.putExtra("CATEGORY", "MONUMENT");
        startActivity(intent);
    }

    /**
     * Starts new activity to add a camping/trekking item.
     * @param view The clicked View
     */
    public void addCamping(View view) {
        Intent intent = new Intent(this, ItemAddActivity.class);
        intent.putExtra("CATEGORY", "CAMPSITE");
        startActivity(intent);
    }


    /**
     * Starts new activity to display all cities.
     * @param view The clicked layout
     */
    public void citiesLayoutClicked(View view) {
        Intent intent = new Intent(this, ItemDisplayActivity.class);
        intent.putExtra("CATEGORY", "CITY");
        startActivity(intent);
    }

    /**
     * Starts new activity to display all monuments.
     * @param view The clicked layout
     */
    public void monumentsLayoutClicked(View view) {
        Intent intent = new Intent(this, ItemDisplayActivity.class);
        intent.putExtra("CATEGORY", "MONUMENT");
        startActivity(intent);
    }

    /**
     * Starts new activity to display all campsites.
     * @param view The clicked layout
     */
    public void campingLayoutClicked(View view) {
        Intent intent = new Intent(this, ItemDisplayActivity.class);
        intent.putExtra("CATEGORY", "CAMPSITE");
        startActivity(intent);
    }

    /**
     * Launches an Activity to display the user's favorite Items.
     * @param view
     */
    public void displayFavorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }
}