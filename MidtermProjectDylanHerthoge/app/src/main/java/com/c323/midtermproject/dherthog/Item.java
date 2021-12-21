package com.c323.midtermproject.dherthog;

import android.graphics.Bitmap;

/**
 * Object for City, Monument, and Campsite to subclass. Allows single arraylists to be used when
 * displaying each item.
 */
abstract class Item {

    private boolean favorite;

    private Bitmap bitmap;
    private String bitmapFileName;
    private String name;
    private String bestTimeToVisit;
    private String location;
    private double longitude;
    private double latitude;

    /**
     * Creates an new Item. Should be used when the Item is created for the first time.
     * @param name The name of the Item
     * @param bestTimeToVisit The best time to visit the Item
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the Item is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the Item is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the Item
     */
    public Item(String name, String bestTimeToVisit, String location, double longitude, double latitude, Bitmap bitmap) {
        this.favorite = false;

        this.bitmap = bitmap;
        this.name = name;
        this.bestTimeToVisit = bestTimeToVisit;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;

        this.bitmapFileName = FileNameGenerator.generateUniqueName()+".jpg";
    }

    /**
     * Creates an new Item. Should be used when the Item is recreated after saving.
     * @param name The name of the Item
     * @param bestTimeToVisit The best time to visit the Item
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the Item is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the Item is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the Item
     */
    public Item(String name, String bestTimeToVisit, String location, double longitude, double latitude, Bitmap bitmap, String bitmapFileName, boolean favorite) {
        this.favorite = favorite;

        this.bitmap = bitmap;
        this.name = name;
        this.bestTimeToVisit = bestTimeToVisit;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;

        this.bitmapFileName = bitmapFileName;
    }

    /**
     * @return If the item is a favorite (boolean)
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * @param favorite If the item is a favorite (boolean)
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * @return The name of the Item (can be anything)
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The new name of the Item (can be anything)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The best time to visit the Monument (can be anything)
     */
    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    /**
     * @param bestTimeToVisit The new best time to visit of the Monument (can be anything)
     */
    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    /**
     * @return The image of the Item
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * @param bitmap The image to set for the Item (must be a Bitmap)
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * @return The file name of the image of the Item
     */
    public String getBitmapFileName() {
        return bitmapFileName;
    }

    /**
     * @param bitmap The file name of the image to set for the Item (must be a Bitmap)
     */
    public void getBitmapFileName(Bitmap bitmap) {
        this.bitmapFileName = bitmapFileName;
    }

    /**
     * @param location A description of the Monument's coordinates
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return A description of the Monument's coordinates
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return The longitude the Monument is located along. Must be a double between -180 and 180
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude the Monument is located along. Must be a double between -180 and
     *                  180
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The latitude the Monument is located along. Must be a double between -90 and 90
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude the Monument is located along. Must be a double between -90 and
     *                 90
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
