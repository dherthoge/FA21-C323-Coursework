package com.c323proj7.dherthog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A very simple class to display movie details passed in the Intent's extras.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    /**
     * Gets extras in the Intent and populates it's Views.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get all of the relevant Views
        TextView titleTV = findViewById(R.id.tv_movie_title);
        TextView languageTV = findViewById(R.id.tv_movie_language);
        TextView genreTV = findViewById(R.id.tv_movie_genre);
        TextView releaseDateTV = findViewById(R.id.tv_movie_release_date);
        TextView descriptionTV = findViewById(R.id.tv_movie_description);
        ImageView imageIV = findViewById(R.id.iv_movie_image);

        // Make the description scrollable since descriptions are long
        descriptionTV.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Set the text of the TextViews
            titleTV.setText(extras.getString("TITLE"));
            languageTV.setText("Language: " + extras.getString("LANGUAGE"));
            genreTV.setText("Genre: " + extras.getString("GENRE"));
            releaseDateTV.setText("Release Date: " + extras.getString("RELEASE_DATE"));
            descriptionTV.setText(extras.getString("DESCRIPTION"));

            // Set the image
            String imagePath = "https://image.tmdb.org/t/p/original" + extras.getString("IMAGE_PATH");
            Picasso.with(this).load(imagePath).resize(600, 900).into(imageIV);
        }
    }

    /**
     * Inflates the activity menu.
     * @param menu This value cannot be null.
     * @return True if the panes is to be displayed, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    /**
     * Navigates to a new Activity depending on the selected item.
     * @param item The menu item that was selected. This value cannot be null.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_movies)
            startActivity(new Intent(this, MovieListActivity.class));
        else if (id == R.id.action_weather)
            startActivity(new Intent(this, MainActivity.class));

        return true;
    }
}