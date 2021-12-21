package com.c323.midtermproject.dherthog;

import android.graphics.Bitmap;

/**
 * Holds information for a Campsite item.
 */
public class Campsite extends Item {

    // field unique to Campsite
    private String nearestMetroLocation;

    /**
     * Constructs a Campsite object. Should be used when the Monument is created for the first time.
     * @param name The name of the campsite
     * @param bestTimeToVisit The best time to visit the campsite
     * @param nearestMetroLocation The nearest metropolitan location to the campsite (any String)
     * @param longitude The longitude the City is located along. Must be a double between -180 and 
     *                  180
     * @param latitude The latitude the Campsite is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the Campsite
     */
    public Campsite(String name, String bestTimeToVisit, String nearestMetroLocation, String location, double longitude, double latitude, Bitmap bitmap) {
        super(name, bestTimeToVisit, location, longitude, latitude, bitmap);

        this.nearestMetroLocation = nearestMetroLocation;
    }

    /**
     * Constructs a Campsite object. Should be used when the Monument is created for the first time.
     * @param name The name of the campsite
     * @param bestTimeToVisit The best time to visit the campsite
     * @param nearestMetroLocation The nearest metropolitan location to the campsite (any String)
     * @param longitude The longitude the City is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the Campsite is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the Campsite
     * @param bitmapFileName The name of the bitmap file in MediaStore.Images
     * @param favorite If the item is a favorite (boolean)
     */
    public Campsite(String name, String bestTimeToVisit, String nearestMetroLocation,
                    String location, double longitude, double latitude, Bitmap bitmap, String bitmapFileName, boolean favorite) {

        super(name, bestTimeToVisit, location, longitude, latitude, bitmap, bitmapFileName, favorite);

        this.nearestMetroLocation = nearestMetroLocation;
    }

    /**
     * @return The nearest metropolitan location to the campsite (any String)
     */
    public String getNearestMetroLocation() {
        return nearestMetroLocation;
    }

    /**
     * @param nearestMetroLocation The nearest metropolitan location to the campsite (any String)
     */
    public void setNearestMetroLocation(String nearestMetroLocation) {
        this.nearestMetroLocation = nearestMetroLocation;
    }
}
