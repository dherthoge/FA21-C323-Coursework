package com.c323proj7.dherthog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieListActivity extends AppCompatActivity {

    private PrepareMoviesTask prepareMoviesTask;
    private ProgressDialog progressDialog;

    /**
     * Starts downloading movie data from TMDB.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        progressDialog = ProgressDialog.show(this, "Movie Data", "Getting your movies!", true, true);

        // Make a network to get the movie data
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            //Create a new task in a separate thread! (not to get UI thread stuck!)
            prepareMoviesTask = new PrepareMoviesTask();
            prepareMoviesTask.execute();
        } else {
            Toast.makeText(this, "No network connection available!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the given list of Movies in a ListView.
     * @param movies The list of Movies to display.
     */
    public void displayMovies(ArrayList<Movie> movies) {

        prepareMoviesTask.cancel(true);

        // Populate the ListView
        ListViewAdapter listViewAdapter = new ListViewAdapter(movies);
        ListView listView = findViewById(R.id.lv_movies);
        listView.setAdapter(listViewAdapter);

        // Set click listeners for each Movie in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
                Movie movieToDetail = movies.get(position);
                intent.putExtra("TITLE", movieToDetail.getTitle());
                intent.putExtra("LANGUAGE", movieToDetail.getLanguage());
                intent.putExtra("GENRE", movieToDetail.getGenre());
                intent.putExtra("RELEASE_DATE", movieToDetail.getReleaseDate());
                intent.putExtra("DESCRIPTION", movieToDetail.getDescription());
                intent.putExtra("IMAGE_PATH", movieToDetail.getImagePath());
                MovieListActivity.this.startActivity(intent);
            }
        });

        // Dismiss the download dialog
        progressDialog.dismiss();
    }

    /**
     * Inner class used to download weather data from openweathermap
     */
    private class PrepareMoviesTask extends AsyncTask<String, Void, String> {

        HashMap<Integer, String> genreMap;
        ArrayList<Movie> movies;

        /**
         * Starts any tasks to run in asynchronously.
         * @param urls A single URL to get weather data from
         * @return An empty string
         */
        @Override
        protected String doInBackground(String... urls) {
            String genreData = downloadGenreData();
            genreMap = parseGenreData(genreData);

            String movieData = downloadMovieData();
            movies = parseMovieData(movieData);

            return "";
        }

        /**
         * Calls TMDB api to get id-genre mapping.
         * @return The returned JSONObject
         */
        private String downloadGenreData() {
            InputStream is = null;
            StringBuffer result  = new StringBuffer();
            URL myUrl = null;
            try{
                myUrl = new URL("https://api.themoviedb.org/3/genre/movie/list?api_key="+ getResources().getString(R.string.the_movie_db_api_key) + "&language=en-US");
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();

                // If the connection was bad, do not read data
                if (responseCode != HttpURLConnection.HTTP_OK){
                    throw new IOException("Connection not successful!");
                }
                is = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        /**
         * Calls TMDB api to get a list of the current most popular movies.
         * @return The returned JSONObject
         */
        private String downloadMovieData() {
            InputStream is = null;
            StringBuffer result  = new StringBuffer();
            URL myUrl = null;
            try{
                myUrl = new URL("https://api.themoviedb.org/3/discover/movie?api_key="+ getResources().getString(R.string.the_movie_db_api_key) +"&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1");
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();

                // If the connection was bad, do not read data
                if (responseCode != HttpURLConnection.HTTP_OK){
                    throw new IOException("Connection not successful!");
                }
                is = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        /**
         * Creates a HashMap of Integer-String pairs for genre mapping.
         * @param genreData The JSONObject to parse
         * @return A HashMap of Integer-String pairs for genre mappings
         */
        private HashMap<Integer, String> parseGenreData(String genreData) {
            HashMap<Integer, String> result = new HashMap<>();
            try{
                JSONArray jsonGenresArray = new JSONObject(genreData).getJSONArray("genres");

                for (int i = 0; i < jsonGenresArray.length(); i++) {
                    JSONObject curObj = jsonGenresArray.getJSONObject(i);
                    result.put(curObj.getInt("id"), curObj.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        /**
         * Creates an ArrayList<Movie> containing information about each movie.
         * @param genreData The JSONObject to parse
         * @return An ArrayList<Movie> of the returned movies
         */
        private ArrayList<Movie> parseMovieData(String genreData) {

            ArrayList<Movie> result = new ArrayList<>();
            try{
                JSONArray jsonMoviesArray = new JSONObject(genreData).getJSONArray("results");

                for (int i = 0; i < jsonMoviesArray.length(); i++) {
                    JSONObject curJSONObj = jsonMoviesArray.getJSONObject(i);
                    String title = curJSONObj.getString("title");
                    String language = curJSONObj.getString("original_language");
                    String releaseDate = curJSONObj.getString("release_date");
                    String description = curJSONObj.getString("overview");
                    String imagePath = curJSONObj.getString("poster_path");

                    // There is always at least one genre in the list of possible genre ids
                    JSONArray genreIDsJSONArray = curJSONObj.getJSONArray("genre_ids");
                    int genre = Integer.parseInt(genreIDsJSONArray.get(0).toString());

                    result.add(new Movie(title, language, genreMap.get(genre), releaseDate, description, imagePath));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        /**
         * Triggers UI to display the returned weather data.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            displayMovies(movies);
        }
    }

    /**
     * Converts stored Movie information to a hierarchy of views.
     */
    private class ListViewAdapter extends ArrayAdapter<Movie> {

        ArrayList<Movie> movies;

        /**
         * Create a new ListViewAdapter.
         * @param movies The list of Movies to display
         */
        public ListViewAdapter(ArrayList<Movie> movies) {
            super(MovieListActivity.this, R.layout.item_layout, movies);
            this.movies = movies;
        }

        /**
         * Populates the Views at the given position with a Movie's image and it's title.
         * @param position The position of the current Movie
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Get the view to fill in
            View itemView = convertView;
            // If the view has not been inflated yet, do so
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_layout, parent, false);

            // Get the data to "fill in" the view
            Movie curMovie = movies.get(position);

            // Get references for Views to populate
            ImageView iv = itemView.findViewById(R.id.iv_list_item_image);
            TextView tv = itemView.findViewById(R.id.tv_list_item_title);

            // Populate the image with Picasso
            String imageURL = "https://image.tmdb.org/t/p/original" + curMovie.getImagePath();
            Picasso.with(MovieListActivity.this).load(imageURL).resize(200, 300).into(iv);

            // Set the Movie's title
            tv.setText(curMovie.getTitle());

            // Return the "filled in" view with
            return itemView;
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
        if (id == R.id.action_weather)
            startActivity(new Intent(this, MainActivity.class));

        return true;
    }
}