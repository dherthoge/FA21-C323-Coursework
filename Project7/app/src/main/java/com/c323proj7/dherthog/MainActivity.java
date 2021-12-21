package com.c323proj7.dherthog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private EditText location;
    private TextView weatherTV;
    private TextView descriptionTV;
    private TextView temperatureTV;
    private TextView feelsLikeTV;
    private ImageView weatherIconIV;
    private Location currentLocation;
    private ProgressDialog progressDialog;
    private DownloadWeatherTask downloadWeatherTask;

    /**
     * Stores ids of each View in the Activity.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.et_location);
        weatherTV = findViewById(R.id.tv_weather);
        descriptionTV = findViewById(R.id.tv_weather_description);
        temperatureTV = findViewById(R.id.tv_temperature);
        feelsLikeTV = findViewById(R.id.tv_feels_like);
        weatherIconIV = findViewById(R.id.iv_weather_icon);
    }

    /**
     * Gets the user's current location an stores it as a global variable.
     */
    private void getCurrentLocation() {

        // Create a loop to force the user to give permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                getWeather(null);
                locationManager.removeUpdates(this);
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 1, locationListener);
    }

    /**
     * Determines what permissions the User has allowed the application to access.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 13:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getCurrentLocation();
                break;
            default:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
                Toast.makeText(this, "You must grant location for Map Display to function properly!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Gets the weather for the location of the user by calling openweathermap.
     * @param view The clicked button
     */
    public void getWeather(View view) {
        String restURL = "";

        if (!location.getText().toString().equals("")) { // Determine if user entered a city or coords

            String locationInput = location.getText().toString();

            // Try to get coords from the input
            Pattern pattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$"); // Pattern found at https://stackoverflow.com/questions/3518504/regular-expression-for-matching-latitude-longitude-coordinates
            Matcher matcher = pattern.matcher(locationInput);
            if (matcher.find()) { // If the input is a set of coords, set URL to use lat and lon

                Scanner scanner = new Scanner(locationInput);
                double lat = scanner.nextDouble();
                double lon = scanner.nextDouble();
                restURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat
                        + "&lon=" + lon + "&appid=" + getResources().getString(R.string.open_weather_map_api_key);
            }
            else // Input is a city name
                restURL = "https://api.openweathermap.org/data/2.5/weather?q=" + location.getText().toString() + "&appid=" + getResources().getString(R.string.open_weather_map_api_key);
        }
        else if (currentLocation == null) { // If no city/coords given, get current location
            getCurrentLocation();
            progressDialog = ProgressDialog.show(this, "Weather Data", "Getting your location!", true, true);
            return;
        }
        else // Once location has been fetched, get the weather
            restURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + currentLocation.getLatitude()
                    + "&lon=" + currentLocation.getLongitude()+ "&appid=" + getResources().getString(R.string.open_weather_map_api_key);


        // If a ProgressDialog has been created while fetching location, dismiss it
        if (progressDialog != null)
            progressDialog.dismiss();

        // Start new ProgressDialog
        progressDialog = ProgressDialog.show(this, "Weather Data", "Getting your weather!", true, true);


        // Make a network to get the weather data
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            //Create a new task in a separate thread! (not to get UI thread stuck!)
            downloadWeatherTask = new DownloadWeatherTask();
            downloadWeatherTask.execute(restURL);
        } else {
            Toast.makeText(this, "No network connection available!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parses the weather data and displays it.
     * @param weatherData The data to display (a JSONObject in String format)
     */
    public void displayWeather(String weatherData) {

        // Parse the incoming data
        HashMap<String, String> weatherItems = parseJSONData(weatherData);

        // Load the weather icon from openweathermap using Picasso
        String iconURL = "https://openweathermap.org/img/w/" + weatherItems.get("icon") + ".png";
        Picasso.with(this).load(iconURL).resize(200, 200).into(weatherIconIV);

        // Convert temperates ton
        double tempFahrenheit = (Double.parseDouble(weatherItems.get("temp")) - 273.15) * 9 / 5 + 32;
        double feelsLikeFahrenheit = (Double.parseDouble(weatherItems.get("feels_like")) - 273.15) * 9/5 + 32;

        // Set all of the TextViews
        weatherTV.setText(String.format("Today's Weather: %s", weatherItems.get("main")));
        descriptionTV.setText(String.format("Description: %s", weatherItems.get("description")));
        temperatureTV.setText(String.format("Temperature: %.2f° F", tempFahrenheit));
        feelsLikeTV.setText(String.format("Feels Like: %.2f° F", feelsLikeFahrenheit));

        // Prevent AsyncTask from leaking memory
        downloadWeatherTask.cancel(true);

        progressDialog.dismiss();
    }

    /**
     * Parses weather, description, temperature, and feels like temperature from the given data.
     * @param jsonData A JSONObject in String format
     * @return The parsed data stored in a HashMap
     */
    private HashMap<String, String> parseJSONData(String jsonData) {
        HashMap<String, String> result = new HashMap<>();
        try{
            JSONObject jsonRootObject = new JSONObject(jsonData);

            JSONObject weather = jsonRootObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonRootObject.getJSONObject("main");

            result.put("icon", weather.getString("icon"));
            result.put("main", weather.getString("main"));
            result.put("description", weather.getString("description"));

            result.put("temp", main.getString("temp"));
            result.put("feels_like", main.getString("feels_like"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Starts MovieListActivity.
     * @param view The clicked View
     */
    public void startMovieList(View view) {
        startActivity(new Intent(this, MovieListActivity.class));
    }

    /**
     * Inner class used to download weather data from openweathermap
     */
    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {

        String returnedData;
        boolean callSuccessful = true;

        /**
         * Starts any tasks to run in asynchronously.
         * @param urls A single URL to get weather data from
         * @return An empty string
         */
        @Override
        protected String doInBackground(String... urls) {
            returnedData = downloadWeatherData(urls[0]);
            return "";
        }

        /**
         * Calls openweathermap's api to get weather data.
         * @param url A URL to get weather data from
         * @return The returned JSONObject
         */
        private String downloadWeatherData(String url) {
            InputStream is = null;
            StringBuffer result  = new StringBuffer();
            URL myUrl = null;
            try{
                myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();

                // If the connection was bad, do not read data
                if (responseCode != HttpURLConnection.HTTP_OK){
                    callSuccessful = false;
                    return "";
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

            callSuccessful = true;
            return result.toString();
        }

        /**
         * Triggers UI to display the returned weather data.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            // If data was fetched successfully, display it
            if (callSuccessful) {
                displayWeather(returnedData);
                return;
            }

            // Data was not fetched
            Toast.makeText(MainActivity.this, "Could not find the given city! If using coords, separate with a single space!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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

        return true;
    }
}