package com.c323.midtermproject.dherthog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages all items (City, Monument, Campsite) and their persistence.
 */
public class ItemManager {

    private ArrayList<City> cities;
    private ArrayList<Monument> monuments;
    private ArrayList<Campsite> campsites;
    private Context applicationContext;
    private volatile static ItemManager itemManager = null;

    /**
     * Gets the current instance of ItemManager or creates on if one has not been instantiated
     * already.
     * @param applicationContext The context of the application
     * @return The current ItemManager instance
     */
    public static ItemManager getInstance(Context applicationContext) {
        if (itemManager == null) {
            synchronized (ItemManager.class) {
                try {
                    itemManager = new ItemManager(applicationContext);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(applicationContext, "Could not create a new ItemManager!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return itemManager;
    }

    /**
     * Creates an EventManager and reads Cities, Monuments, and Campsites from internal storage.
     * @param applicationContext The context of the application
     * @throws IOException If any file was not found
     * @throws JSONException If the data is corrupt
     */
    public ItemManager(Context applicationContext) {
        this.applicationContext = applicationContext;
        cities = new ArrayList<>();
        monuments = new ArrayList<>();
        campsites = new ArrayList<>();

        try {
            readCities();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not read cities!", Toast.LENGTH_SHORT).show();
        }
        try {
            readMonuments();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not read monuments!", Toast.LENGTH_SHORT).show();
        }
        try {
            readCampsites();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not read campsites!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves Cities to internal storage.
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public void writeCities() throws JSONException, IOException {
        JSONArray data = new JSONArray();
        JSONObject city;


        // Converts every City stored in city to a JSONObject and puts it in a JSONArray
        for (City c: cities) {

            city = new JSONObject();
            city.put("name", c.getName());
            city.put("bestTimeToVisit", c.getBestTimeToVisit());
            city.put("touristSpots", c.getTouristSpots());
            city.put("location", c.getLocation());
            city.put("longitude", c.getLongitude());
            city.put("latitude", c.getLatitude());
            String bitmapFileName = c.getBitmapFileName();
            city.put("bitmapFileName", bitmapFileName);
            city.put("favorite", c.isFavorite());
            data.put(city);

            writeBitmap(c.getBitmap(), bitmapFileName);
        }

        // Writes out the cities in JSON format
        String text = data.toString();
        FileOutputStream fos = applicationContext.openFileOutput("CITIES", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
    }

    /**
     * Saves Monuments to internal storage.
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public void writeMonuments() throws JSONException, IOException {
        JSONArray data = new JSONArray();
        JSONObject monument;

        // Converts every Monument stored in monumentS to a JSONObject and puts it in a JSONArray
        for (Monument m: monuments) {

            monument = new JSONObject();
            monument.put("name", m.getName());
            monument.put("history", m.getHistory());
            monument.put("bestTimeToVisit", m.getBestTimeToVisit());
            monument.put("ticketPrice", m.getTicketPrice());
            monument.put("location", m.getLocation());
            monument.put("longitude", m.getLongitude());
            monument.put("latitude", m.getLatitude());
            String bitmapFileName = m.getBitmapFileName();
            monument.put("bitmapFileName", bitmapFileName);
            monument.put("favorite", m.isFavorite());
            data.put(monument);

            writeBitmap(m.getBitmap(), bitmapFileName);
        }

        // Writes out the cities in JSON format
        String text = data.toString();
        FileOutputStream fos = applicationContext.openFileOutput("MONUMENTS", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
    }

    /**
     * Saves Campsite to internal storage.
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public void writeCampsite() throws JSONException, IOException {
        JSONArray data = new JSONArray();
        JSONObject campsite;

        // Converts every Campsite stored in campsite to a JSONObject and puts it in a JSONArray
        for (Campsite c: campsites) {

            campsite = new JSONObject();
            campsite.put("name", c.getName());
            campsite.put("bestTimeToVisit", c.getBestTimeToVisit());
            campsite.put("nearestMetroLocation", c.getNearestMetroLocation());
            campsite.put("location", c.getLocation());
            campsite.put("longitude", c.getLongitude());
            campsite.put("latitude", c.getLatitude());
            String bitmapFileName = c.getBitmapFileName();
            campsite.put("bitmapFileName", bitmapFileName);
            campsite.put("favorite", c.isFavorite());
            data.put(campsite);

            writeBitmap(c.getBitmap(), bitmapFileName);
        }

        // Writes out the campsites in JSON format
        String text = data.toString();
        FileOutputStream fos = applicationContext.openFileOutput("CAMPSITES", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
    }

    /**
     * Writes the given Bitmap to the given file name (stored app's contextual directory)
     * @param bitmap The Bitmap to write
     * @param bitmapFileName The file name to write to
     */
    private void writeBitmap(Bitmap bitmap, String bitmapFileName) {
        try {
            FileOutputStream out = applicationContext.openFileOutput(bitmapFileName, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads Cities from internal storage.
     * @throws JSONException If the data is corrupt
     */
    public void readCities() throws JSONException {
        try {

            // Reads in saved JSONArray of Cities
            FileInputStream fis = applicationContext.openFileInput("CITIES");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while(bis.available() != 0) {
                b.append((char)bis.read());
            }
            bis.close();
            fis.close();

            // Converts Cities as JSONObjects to City literals
            JSONArray data = new JSONArray(b.toString());
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonCity = data.getJSONObject(i);
                String name = jsonCity.getString("name");
                String bestTimeToVisit = jsonCity.getString("bestTimeToVisit");
                String touristSpots = jsonCity.getString("touristSpots");
                String location = jsonCity.getString("location");
                double longitude = Double.parseDouble(jsonCity.getString("longitude"));
                double latitude = Double.parseDouble(jsonCity.getString("latitude"));
                String bitmapFileName = jsonCity.getString("bitmapFileName");
                boolean favorite = jsonCity.getBoolean("favorite");

                // Uses a default image in case the image for the City cannot be found
                Bitmap bitmap = BitmapFactory.decodeResource(applicationContext.getResources(),
                        R.drawable.image_not_found);
                try {
                    FileInputStream imageFis = applicationContext.openFileInput(bitmapFileName);
                    bitmap = BitmapFactory.decodeStream(imageFis);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                City city = new City(name, bestTimeToVisit, touristSpots, location, longitude, latitude, bitmap, bitmapFileName, favorite);
                cities.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads Monuments from internal storage.
     * @throws JSONException If the data is corrupt
     */
    public void readMonuments() throws JSONException {
        try {

            // Reads in saved JSONArray of Monuments
            FileInputStream fis = applicationContext.openFileInput("MONUMENTS");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while(bis.available() != 0) {
                b.append((char)bis.read());
            }
            bis.close();
            fis.close();

            // Converts Monuments as JSONObjects to Monument literals
            JSONArray data = new JSONArray(b.toString());
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonMonument = data.getJSONObject(i);
                String name = jsonMonument.getString("name");
                String history = jsonMonument.getString("history");
                String bestTimeToVisit = jsonMonument.getString("bestTimeToVisit");
                double ticketPrice = jsonMonument.getDouble("ticketPrice");
                String location = jsonMonument.getString("location");
                double longitude = Double.parseDouble(jsonMonument.getString("longitude"));
                double latitude = Double.parseDouble(jsonMonument.getString("latitude"));
                String bitmapFileName = jsonMonument.getString("bitmapFileName");
                boolean favorite = jsonMonument.getBoolean("favorite");

                // Uses a default image in case the image for the City cannot be found
                Bitmap bitmap = BitmapFactory.decodeResource(applicationContext.getResources(),
                        R.drawable.image_not_found);
                try {
                    FileInputStream imageFis = applicationContext.openFileInput(bitmapFileName);
                    bitmap = BitmapFactory.decodeStream(imageFis);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Monument monument = new Monument(name, history, bestTimeToVisit, ticketPrice, location, longitude, latitude, bitmap, bitmapFileName, favorite);
                monuments.add(monument);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads Campsites from internal storage.
     * @throws JSONException If the data is corrupt
     */
    public void readCampsites() throws JSONException {
        try {

            // Reads in saved JSONArray of Campsites
            FileInputStream fis = applicationContext.openFileInput("CAMPSITES");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while(bis.available() != 0) {
                b.append((char)bis.read());
            }
            bis.close();
            fis.close();

            // Converts Campsites as JSONObjects to Campsite literals
            JSONArray data = new JSONArray(b.toString());
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonCampsite = data.getJSONObject(i);
                String name = jsonCampsite.getString("name");
                String bestTimeToVisit = jsonCampsite.getString("bestTimeToVisit");
                String nearestMetroLocation = jsonCampsite.getString("nearestMetroLocation");
                String location = jsonCampsite.getString("location");
                double longitude = Double.parseDouble(jsonCampsite.getString("longitude"));
                double latitude = Double.parseDouble(jsonCampsite.getString("latitude"));
                String bitmapFileName = jsonCampsite.getString("bitmapFileName");
                boolean favorite = jsonCampsite.getBoolean("favorite");

                // Uses a default image in case the image for the City cannot be found
                Bitmap bitmap = BitmapFactory.decodeResource(applicationContext.getResources(),
                        R.drawable.image_not_found);
                try {
                    FileInputStream imageFis = applicationContext.openFileInput(bitmapFileName);
                    bitmap = BitmapFactory.decodeStream(imageFis);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Campsite campsite = new Campsite(name, bestTimeToVisit, nearestMetroLocation, location, longitude, latitude, bitmap, bitmapFileName, favorite);
                campsites.add(campsite);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a City to the list of Cities and saves the Cities to internal storage.
     * @param city The City to add
     * @return The current list of Cities
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public ArrayList<City> addCity(City city) {
        cities.add(0, city);
        try {
            writeCities();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not save cities!", Toast.LENGTH_SHORT).show();
        }
        return getCitiesCopy();
    }

    /**
     * Adds a Monument to the list of Monuments and saves the Monuments to internal storage.
     * @param monument The Monument to add
     * @return The current list of Monuments
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public ArrayList<Monument> addMonument(Monument monument) {
        monuments.add(0, monument);
        try {
            writeMonuments();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not save monuments!", Toast.LENGTH_SHORT).show();
        }
        return getMonumentsCopy();
    }

    /**
     * Adds a Campsite to the list of Campsites and saves the Campsites to internal storage.
     * @param campsite The Campsite to add
     * @return The current list of Campsites
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public ArrayList<Campsite> addCampsite(Campsite campsite) {
        campsites.add(0, campsite);
        try {
            writeCampsite();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Could not save campsites!", Toast.LENGTH_SHORT).show();
        }
        return getCampsitesCopy();
    }

    /**
     * Removes a City from the list of Cities and saves the Cities to internal storage.
     * @param position The position of the City to delete
     * @return If a City was properly deleted
     */
    public boolean removeCity(int position) {
        try {
            applicationContext.deleteFile(cities.get(position).getBitmapFileName());
            cities.remove(position);
            writeCities();
            return true;
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "City deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "City deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a Monument from the list of Monuments and saves the Monuments to internal storage.
     * @param position The position of the Monument to delete
     * @return If a Monument was properly deleted
     */
    public boolean removeMonument(int position) {
        try {
            applicationContext.deleteFile(monuments.get(position).getBitmapFileName());
            monuments.remove(position);
            writeCities();
            return true;
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "Monument deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "Monument deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a Campsite from the list of Campsites and saves the Campsites to internal storage.
     * @param position The position of the Campsite to delete
     * @return If a Campsite was properly deleted
     */
    public boolean removeCampsite(int position) {
        try {
            applicationContext.deleteFile(campsites.get(position).getBitmapFileName());
            campsites.remove(position);
            writeCities();
            return true;
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "Campsite deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "Campsite deleted but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
        return false;
    }

    /**
     * Favorites the City at the given position.
     * @param position The position of the City to favorite
     */
    public void cityFavorited(int position) {

        try {
            cities.get(position).setFavorite(true);
            writeCities();
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
    }

    /**
     * Favorites the Monument at the given position.
     * @param position The position of the Monument to favorite
     */
    public void monumentFavorited(int position) {

        try {
            monuments.get(position).setFavorite(true);
            writeMonuments();
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
    }

    /**
     * Favorites the Campsite at the given position.
     * @param position The position of the Campsite to favorite
     */
    public void campsiteFavorited(int position) {

        try {
            campsites.get(position).setFavorite(true);
            writeCampsite();
        } catch (JSONException jsonException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            Toast.makeText(applicationContext, "City favorited but could not save deletion!", Toast.LENGTH_SHORT).show();
            ioException.printStackTrace();
        }
    }

    /**
     * Returns a copy of the stored Cities.
     * @return An ArrayList<City>
     */
    public ArrayList<City> getCitiesCopy() {
        ArrayList<City> citiesCopy = new ArrayList<>();
        for (City c: cities)
            citiesCopy.add(c);
        return citiesCopy;
    }

    /**
     * Returns a copy of the stored Monuments.
     * @return An ArrayList<City>
     */
    public ArrayList<Monument> getMonumentsCopy() {
        ArrayList<Monument> monumentsCopy = new ArrayList<>();
        for (Monument m: monuments)
            monumentsCopy.add(m);
        return monumentsCopy;
    }

    /**
     * Returns a copy of the stored Campsites.
     * @return An ArrayList<City>
     */
    public ArrayList<Campsite> getCampsitesCopy() {
        ArrayList<Campsite> campsitesCopy = new ArrayList<>();
        for (Campsite c: campsites)
            campsitesCopy.add(c);
        return campsitesCopy;
    }

    /**
     * Returns a copy of favorited Times
     * @return An ArrayList of favorited Items
     */
    public ArrayList<Item> getFavoriteItemsCopy() {

        ArrayList<Item> favoriteItemsCopy = new ArrayList<>();
        for (City c: cities)
            if (c.isFavorite())
                favoriteItemsCopy.add(c);

        for (Monument m: monuments)
            if (m.isFavorite())
                favoriteItemsCopy.add(m);

        for (Campsite c: campsites)
            if (c.isFavorite())
                favoriteItemsCopy.add(c);

        return favoriteItemsCopy;
    }

    /**
     * Un-favorites the given Item.
     * @param item The Item to un-favorite
     */
    public void removeFavorite(Item item) {
        item.setFavorite(false);
    }
}
