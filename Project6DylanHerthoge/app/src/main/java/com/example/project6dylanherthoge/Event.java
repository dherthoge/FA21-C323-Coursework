package com.example.project6dylanherthoge;

/**
 * A class to store information related to an Event.
 */
public class Event {

    private String eventInformation;
    private String date;
    private double latitude;
    private double longitude;

    /**
     * Creates an Event.
     * @param eventInformation Any information about the Event
     * @param date The date of the Event
     * @param latitude The latitude the Event is occurring at
     * @param longitude The longitude the Event is occurring at
     */
    public Event(String eventInformation, String date, double latitude, double longitude) {
        this.date = date;
        this.eventInformation = eventInformation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the date of the Event.
     * @return The Event's date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the Event.
     * @param date The Event's date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the information for the Event.
     * @return The Event's information
     */
    public String getEventInformation() {
        return eventInformation;
    }

    /**
     * Sets the information for the Event.
     * @param eventInformation  The Event's information
     */
    public void setEventInformation(String eventInformation) {
        this.eventInformation = eventInformation;
    }

    /**
     * Gets the latitude of the Event.
     * @return The Event's latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the Event.
     * @param latitude  The Event's latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the Event.
     * @return The Event's longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the Event.
     * @param longitude  The Event's longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
