package com.c323.midtermproject.dherthog;

import android.graphics.Bitmap;

/**
 * Holds information for a Monument item.
 */
public class Monument extends Item {

    // fields unique to Monument
    private String history;
    private Double ticketPrice;

    /**
     * Constructs a Monument object. Should be used when the Monument is created for the first time.
     * @param name The name of the monument
     * @param history The history of the monument
     * @param bestTimeToVisit The best time to visit the monument
     * @param ticketPrice The ticket price of the monument (must be a double greater than 0)
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the City is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the Monument is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the City
     */
    public Monument(String name, String history, String bestTimeToVisit, Double ticketPrice, String location, double longitude, double latitude, Bitmap bitmap) {

        super(name, bestTimeToVisit, location, longitude, latitude, bitmap);

        this.history = history;
        this.ticketPrice = ticketPrice;
    }

    /**
     * Constructs a Monument object. Should be used when the Monument is recreated after saving.
     * @param name The name of the monument
     * @param history The history of the monument
     * @param bestTimeToVisit The best time to visit the monument
     * @param ticketPrice The ticket price of the monument (must be a double greater than 0)
     * @param location A description of the given longitude and latitude
     * @param longitude The longitude the City is located along. Must be a double between -180 and
     *                  180
     * @param latitude The latitude the Monument is located along. Must be a double between -90 and
     *                 90
     * @param bitmap The image of the City
     * @param bitmapFileName The name of the bitmap file in MediaStore.Images
     * @param favorite If the item is a favorite (boolean)
     */
    public Monument(String name, String history, String bestTimeToVisit, Double ticketPrice,
                    String location, double longitude, double latitude, Bitmap bitmap, String bitmapFileName, boolean favorite) {

        super(name, bestTimeToVisit, location, longitude, latitude, bitmap, bitmapFileName, favorite);

        this.history = history;
        this.ticketPrice = ticketPrice;
    }

    /**
     * @return The history of the Monument (can be anything)
     */
    public String getHistory() {
        return history;
    }

    /**
     * @param history The new history of the Monument (can be anything)
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * @return The ticket price of the monument (must be a double greater than 0)
     */
    public Double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * @param ticketPrice The ticket price of the monument (must be a double greater than 0)
     */
    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
