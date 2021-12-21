package com.example.project6dylanherthoge;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages all of the user's Events and stores them in internal storage.
 */
public class EventManager {

    private ArrayList<Event> events;
    private Context context;
    private volatile static EventManager eventManager = null;

    /**
     * Gets the current instance of EventManager or creates on if one has not been instantiated
     * already.
     * @param applicationContext The context of the application
     * @return The current EventManager instance
     */
    public static EventManager getInstance(Context applicationContext) {
        if(eventManager == null) {
            synchronized (EventManager.class) {
                try {
                    eventManager = new EventManager(applicationContext);
                } catch (IOException e) {
                    Log.i("EventManager", "No Events found");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.i("EventManager", "No Events found");
                    e.printStackTrace();
                }
            }
        }
        return eventManager;
    }

    /**
     * Creates an EventManager and reads Events from internal storage.
     * @param applicationContext The context of the application
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    private EventManager(Context applicationContext) throws IOException, JSONException {
        context = applicationContext;
        events = new ArrayList<>();
        readJSONFile();
    }

    /**
     * Adds an Event to the list of Events and saves the Events to internal storage.
     * @param e The event to add
     * @return The current list of Events
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public ArrayList<Event> addEvent(Event e) throws IOException, JSONException {
        events.add(0, e);
        writeJSONFile();
        return events;
    }

    /**
     * Removes an Event from the list of Events and saves the Events to internal storage.
     * @param date The date of the Event to remove
     * @param eventInformation The information of the Event to remove
     * @param latitude The latitude of the Event to remove
     * @param longitude The longitude of the Event to remove
     * @return If an Event matching the given information was deleted
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public boolean removeEvent(String date, String eventInformation, double latitude, double longitude) {
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            if (e.getDate() == date && e.getEventInformation() == eventInformation && e.getLatitude() == latitude && e.getLongitude() == longitude) {
                events.remove(i);
                try {
                    writeJSONFile();
                } catch (JSONException jsonException) {
                    Toast.makeText(context, "Event not deleted! Could not save deletion!", Toast.LENGTH_SHORT).show();
                    jsonException.printStackTrace();
                } catch (IOException ioException) {
                    Toast.makeText(context, "Event not deleted! Could not save deletion!", Toast.LENGTH_SHORT).show();
                    ioException.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Filters the list of events by the given date.
     * @param date The filter criteria
     * @return The results of applying the filter
     */
    public ArrayList<Event> filter(String date) {
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for (Event e: events)
            if (date.equals(e.getDate()))
                matchingEvents.add(e);

        return matchingEvents;
    }

    /**
     * Saves Events to internal storage.
     * @throws IOException If the file is not found
     * @throws JSONException If the data is corrupt
     */
    public void writeJSONFile() throws JSONException, IOException {
        JSONArray data = new JSONArray();
        JSONObject event;

        for (Event e: events) {

            event = new JSONObject();
            event.put("information", e.getEventInformation());
            event.put("date", e.getDate());
            event.put("latitude", e.getLatitude());
            event.put("longitude", e.getLongitude());
            data.put(event);
        }
        String text = data.toString();
        FileOutputStream fos = context.openFileOutput("EVENTS", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
    }

    /**
     * Reads Events from internal storage.
     * @throws JSONException If the data is corrupt
     */
    public void readJSONFile() throws JSONException {

        try {
            FileInputStream fis = context.openFileInput("EVENTS");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while(bis.available() != 0) {
                b.append((char)bis.read());
            }
            bis.close();
            fis.close();

            JSONArray data = new JSONArray(b.toString());
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonEvent = data.getJSONObject(i);
                String eventInformation = jsonEvent.getString("information");
                String eventDate = jsonEvent.getString("date");
                double eventLatitude = Double.parseDouble(jsonEvent.getString("latitude"));
                double eventLongitude = Double.parseDouble(jsonEvent.getString("longitude"));
                Event event = new Event(eventInformation, eventDate, eventLatitude, eventLongitude);
                events.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Returns the current list of Events.
     * @return The current list of Events
     */
    public ArrayList<Event> getEvents() {
        ArrayList<Event> eventsCopy = new ArrayList<>();
        for (Event e: events)
            eventsCopy.add(e);
        return eventsCopy;
    }
}
