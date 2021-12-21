package com.c323finalproject.dherthog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.c323finalproject.dherthog.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

/**
 * A NavigationDrawerActivity to navigate between various Fragments.
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    /**
     * Initializes the NavigationDrawerActivity, gets references for necessary Views, and sets click
     * listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayoutMain;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_mode_of_training, R.id.nav_exercises, R.id.nav_daily_training, R.id.nav_calendar)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // Set user's name, email, and profile picture
        TextView nameTv = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_activity_name);
        TextView emailTv = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_activity_email);
        ImageView profilePictureIv = navigationView.getHeaderView(0).findViewById(R.id.iv_nav_activity_profile_picture);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
        nameTv.setText(sharedPref.getString(getString(R.string.saved_name_key), ""));
        emailTv.setText(sharedPref.getString(getString(R.string.saved_email_key), ""));

        profilePictureIv.setImageBitmap(ImageManager.readImage(sharedPref.getString(getString(R.string.saved_profile_picture_path_key), "")));

        setSignOutListener();
    }

    /**
     * Sets an OnClickListener for the sign out Button.
     */
    private void setSignOutListener() {
        // Set click listener for sign out
        findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.is_logged_in_key), false);
                editor.commit();

                finish();
            }
        });
    }

    /**
     * Necessary override for Navigation Drawer.
     * @return true if Up navigation completed successfully and this Activity was finished, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Prevents the user from pressing back to SignInActivity.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}