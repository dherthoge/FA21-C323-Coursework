package com.c323proj10.dherthog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ActivityCommunicator {

//    TODO: Make sure I don't get files w/out permissions

    private List<Song> songs;
    private List<Video> videos;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView tvItemTitle;
    private ProgressBar progressBar;
    private static final MediaPlayer mediaPlayer = new MediaPlayer();

    /**
     * Checks for external storage read permissions, gathers music and video files, then display
     * SongFragment.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvItemTitle = findViewById(R.id.item_title);
        progressBar = findViewById(R.id.progressBar);

        initializeNavigationDrawer();
        initializeBottomSheetDialog();

        checkPermissions();
        songs = MediaQuery.querySongs(this);
        videos = MediaQuery.queryVideos(this);

        setVideoFragment();
    }

    /**
     * Checks if the application has read permission for external storage and requests it if not.
     */
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 13);
    }

    /**
     * Determines if the user has granted read permission for external storage and continues app
     * flow if they have.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 13:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    songs = MediaQuery.querySongs(this);
                    videos = MediaQuery.queryVideos(this);
                }
                break;
            default:
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
                Toast.makeText(this, "You must grant read permission to get files!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Displays a SongFragment.
     */
    private void setSongFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SongFragment songFragment = new SongFragment(songs, this);
        fragmentTransaction.replace(R.id.constLayout, songFragment);
        fragmentTransaction.commit();
    }

    /**
     * Displays a VideoFragment.
     */
    private void setVideoFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        VideoFragment videoFragment = new VideoFragment(videos, this);
        fragmentTransaction.replace(R.id.constLayout, videoFragment);
    }

    /**
     * Sets up the Navigation Drawer.
     */
    private void initializeNavigationDrawer() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set click listener for menu items
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Listener for Navigation Drawer open/close.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Determines what item was selected and moves to its corresponding activity.
     * @param item The clicked MenuItem
     * @return True if the method consumes the event, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {

            case R.id.nav_playlist:
                break;
            case R.id.nav_songs:
                setSongFragment();
                break;
            case R.id.nav_videos:
                setVideoFragment();
                break;
        }

        return true;
    }

    /**
     * Gets references for the bottom sheet dialog and sets listeners for play/pause buttons.
     */
    private void initializeBottomSheetDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        // Set click listeners for play/pause buttons
        Button btnPlay = findViewById(R.id.btn_play);
        Button btnPause = findViewById(R.id.btn_pause);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
            }
        });
    }

    private Song findSong(List<Song> songs, long id) {

        Song result = null;
        for (Song song : songs)
            if (song.getId() == id) {
                result = song;
                break;
            }

        return result;
    }

    private Video findVideo(List<Video> videos, long id) {

        Video result = null;
        for (Video video : videos)
            if (video.getId() == id) {
                result = video;
                break;
            }

        return result;
    }

    Thread progressThread;
    private void playMedia(long id) {
        // First check if it's a song
        Item mediaToPlay = findSong(songs, id);

        // If no song was found, look for a video
        if (mediaToPlay == null)
            mediaToPlay = findVideo(videos, id);

        // If no video was found, do nothing
        if (mediaToPlay == null)
            return;

        tvItemTitle.setText(mediaToPlay.getTitle());

        progressThread = createThread();
        progressThread.start();

        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mediaToPlay.getPath()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private Thread createThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = mediaPlayer.getCurrentPosition();
                progressBar.setProgress(progress);
            }
        });
    }

    @Override
    public void onItemSelected(long id, Category category) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            progressThread.interrupt();
        }

        playMedia(id);
    }

    @Override
    public void onItemFavorited(long id, Category category) {

    }
}