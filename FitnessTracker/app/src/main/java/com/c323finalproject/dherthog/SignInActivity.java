package com.c323finalproject.dherthog;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    private ImageView profilePictureIv;
    private EditText nameEdt, emailEdt;
    private Button signInBtn;
    private final int REQUEST_IMAGE_CAPTURE = 42;
    private final int REQUEST_SELECT_IMAGE = 43;

    /**
     * Initializes the SignInActivity and creates a copy of pre-loaded exercise images on the user's
     * device if they haven't already been stored.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // Get references for Views
        profilePictureIv = findViewById(R.id.iv_sign_in_profile_picture);
        nameEdt = findViewById(R.id.edt_sign_in_name);
        emailEdt = findViewById(R.id.edt_sign_in_email);
        signInBtn = findViewById(R.id.btn_sign_in);

        // Save images from assets for preloaded exercises if they're not already there
        File appDirectory = this.getFilesDir();
        if (!new File(appDirectory.getPath()+"/exerciseImages").isDirectory())
            ImageManager.preLoadImages(getApplicationContext());


        // Check if user is currently logged in and direct them to the application if they are
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(getString(R.string.is_logged_in_key), false);
        if (isLoggedIn)
            launchTracker();


        // If directed to the sign-in page, check for saved profile picture and load it
        if (new File(appDirectory.getPath()+"/profilePicture").isDirectory()) {
            String imagePath = getApplicationContext().getFilesDir() + "/profilePicture/profile_picture.jpeg";
            profilePictureIv.setImageBitmap(ImageManager.readImage(imagePath));
        }

        // Creates a NotificationChannel for possible notifications.
        createNotificationChannel();
    }

    /**
     * Sets click listeners for sign-in button and the profile picture.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Create dialog to choose between gallery or camera
        profilePictureIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle("");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_pick_profile_picture, null);
                builder.setView(customLayout);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


                // Set click listeners for gallery and camera
                Button galleryBtn = dialog.findViewById(R.id.btn_gallery);
                Button cameraBtn = dialog.findViewById(R.id.btn_camera);

                // Set click listener to launch the camera, dismiss the dialog,
                // and call a startActivityForResult for the gallery
                galleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_IMAGE);
                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }
                    }
                });

                // Set click listener to launch the camera, dismiss the dialog,
                // and call a startActivityForResult for the camera
                cameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        try {
                            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }
                    }
                });
            }
        });

        // Create/verify sign-in information and save profile picture
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the profile picture's directory has not been created do so
                String profilePictureDir = getApplicationContext().getFilesDir() + "/profilePicture";
                String profilePicturePath = profilePictureDir + "/profile_picture.jpeg";
                File dir = new File(profilePictureDir);
                if (!dir.isDirectory()) {
                    try {
                        dir.mkdir();
                    } catch (Exception e) {
                    }
                }

                // Save the user's profile picture whether or not they changed it
                Bitmap profilePicture = ((BitmapDrawable) profilePictureIv.getDrawable()).getBitmap();
                ImageManager.writeImage(profilePicturePath, profilePicture);


                String name, email;
                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();

                // Make sure the user entered info for name and email
                if (name.equals("") || !isValidEmailAddress(email)) {
                    Snackbar.make(signInBtn, "Enter a name and valid email!", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                // Access shared prefs
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                // If shared prefs have not been set up, do so and launch the tracker
                if (!sharedPref.contains(getString(R.string.saved_name_key))) {
                    editor.putString(getString(R.string.saved_name_key), name);
                    editor.putString(getString(R.string.saved_email_key), email);

                    // Put the path in shared prefs and save the image
                    editor.putString(getString(R.string.saved_profile_picture_path_key), profilePicturePath);

                    // Mark the user as logged in
                    editor.putBoolean(getString(R.string.is_logged_in_key), true);

                    // Apply pref changes
                    editor.apply();

                    // Navigate user to the main part of the application
                    launchTracker();
                }
                else {
                    // If user entered incorrect credentials, let them know
                    if (!name.equals(sharedPref.getString(getString(R.string.saved_name_key), ""))
                            || !email.equals(sharedPref.getString(getString(R.string.saved_email_key), ""))) {
                        Snackbar.make(view, "Please enter the correct name and email!", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    // Mark the user as logged in
                    editor.putBoolean(getString(R.string.is_logged_in_key), true);
                    // Apply pref changes
                    editor.apply();

                    // Navigate user to the main part of the application
                    launchTracker();
                }
            }
        });
    }

    /**
     * Checks if the given String matches the EMAIL_ADDRESS pattern.
     * @param string The String to check
     * @return If the String is a valid email address
     */
    private static boolean isValidEmailAddress(String string) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    private void launchTracker() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Checks if the result came from the camera or gallery and updates exerciseIv accordingly (or
     * not at all if the request is not from REQUEST_IMAGE_CAPTURE or REQUEST_SELECT_IMAGE).
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
                    profilePictureIv.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    break;
                case REQUEST_SELECT_IMAGE:
                    Uri imageUri = data.getData();
                    try {
                        profilePictureIv.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    /**
     * Used to create a NotificationChannel for alarm notifications.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}