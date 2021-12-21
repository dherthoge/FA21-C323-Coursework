package com.c323proj8.dherthog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Displays all of the user's SMSes.
 */
public class MessagesActivity extends AppCompatActivity {

    private ArrayList<SMS> smsList = new ArrayList<>();

    /**
     * Displays the user's SMSs.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        displayMessages();
    }


    /**
     * Converts stored Message information to a hierarchy of views.
     */
    private class ListViewAdapter extends ArrayAdapter<String> {


        /**
         * Create a new ListViewAdapter.
         */
        public ListViewAdapter() {
            super(MessagesActivity.this, R.layout.item_layout);
        }

        /**
         * Populates the Views at the given position with a Messages' number and body.
         * @param position The position of the current Message
         * @param convertView This value may be null.
         * @param parent This value cannot be null.
         * @return The inflated View.
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
            SMS curSMS = smsList.get(position);

            // Get references for Views to populate
            TextView fromNumberTV = itemView.findViewById(R.id.tv_from_number);
            TextView messageContentsTV = itemView.findViewById(R.id.tv_message_contents);

            fromNumberTV.setText(curSMS.number);
            messageContentsTV.setText(curSMS.body);

            // Return the "filled in" View
            return itemView;
        }

        /**
         * Gets the number of SMSes the user has.
         * @return
         */
        @Override
        public int getCount() {
            return smsList.size();
        }
    }

    /**
     * Displays the user's SMSes in a ListView.
     */
    private void displayMessages() {
        getMessages();

        // Populate the ListView
        ListViewAdapter listViewAdapter = new ListViewAdapter();
        ListView listView = findViewById(R.id.lv_messages);
        listView.setAdapter(listViewAdapter);
    }

    /**
     * A class to contain information related to SMSes.
     */
    private class SMS {
        String number;
        String body;

        /**
         * Creates an SMS.
         * @param number The phone number of the sender. Should be a valid phone number.
         * @param body The body of the SMS. Can be any String.
         */
        public SMS(String number, String body) {
            this.number = number;
            this.body = body;
        }
    }

    /**
     * Gets all of the user's SMSes from their inbox.
     */
    private void getMessages() {

        // Below code used to retrieve a user's sms' was found at https://stackoverflow.com/questions/848728/how-can-i-read-sms-messages-from-the-device-programmatically-in-android
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String curAddress = ""; // The phone number of the current SMS
                for(int i = 0; i < cursor.getColumnCount(); i++) {

                    String columnName = cursor.getColumnName(i);
                    if(columnName.equals("address")) // Check if the current column is the SMSes number
                        curAddress = cursor.getString(i);
                    else if(columnName.equals("body")) // Check if the current column is the SMSes body
                        smsList.add(new SMS(curAddress, cursor.getString(i)));
                }
            } while (cursor.moveToNext());
        }
    }
}