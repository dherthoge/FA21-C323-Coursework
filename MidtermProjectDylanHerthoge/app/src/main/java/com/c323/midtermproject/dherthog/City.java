package com.c323.midtermproject.dherthog;

import android.graphics.Bitmap;

/**
 * Holds information for a City item.
 */
public class City extends Item {

    // field unique to City
    private String touristSpots;

    /**
     * Constructs a City object. Should be used when the Ctiy is created for the first time.
     * @param name The name of the city
     * @param bestTimeToVisit The best time to visit the city
     * @param touristSpots Tourist spots located in the city
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the City is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the City is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the City
     */
    public City(String name, String bestTimeToVisit, String touristSpots, String location, double longitude, double latitude, Bitmap bitmap) {
        super(name, bestTimeToVisit, location, longitude, latitude, bitmap);

        this.touristSpots = touristSpots;
    }

    /**
     * Constructs a City object. Should be used when the City is recreated after saving.
     * @param name The name of the city
     * @param bestTimeToVisit The best time to visit the city
     * @param touristSpots Tourist spots located in the city
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the City is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the City is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the City
     * @param bitmapFileName The name of the bitmap file in MediaStore.Images
     * @param favorite If the item is a favorite (boolean)
     */
    public City(String name, String bestTimeToVisit, String touristSpots, String location,
                double longitude, double latitude, Bitmap bitmap, String bitmapFileName, boolean favorite) {
        super(name, bestTimeToVisit, location, longitude, latitude, bitmap, bitmapFileName, favorite);

        this.touristSpots = touristSpots;
    }

    /**
     * @return A list of tourist spots located in the City (can be anything)
     */
    public String getTouristSpots() {
        return touristSpots;
    }

    /**
     * @param touristSpots A new list of tourist spots located in the City (can be anything)
     */
    public void setTouristSpots(String touristSpots) {
        this.touristSpots = touristSpots;
    }
}
