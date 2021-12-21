package com.c323proj8.dherthog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to start music playback and receive an OTP code.
 */
public class MainActivity extends AppCompatActivity {

    private TextView otpCodeTV;

    /**
     * Requests permissions for SMS reading.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        otpCodeTV = findViewById(R.id.tv_otp_code);

        getPermissions();
    }

    /**
     * Asks the users for Manifest.permission.READ_SMS and Manifest.permission.RECEIVE_SMS
     * permissions.
     */
    private void getPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 13);
            return;
        }
    }

    /**
     * Determines what permissions the User has allowed the application to access.
     * @param requestCode The request code passed in ActivityCompat.requestPermissions(android.app.Activity, String[], int)
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either
     *                     PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED.
     *                     Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 13:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getPermissions();
                break;
            default:
                requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 13);
                Toast.makeText(this, "You must grant location for Map Display to function properly!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Starts music playback if it is not already running.
     * @param view The clicked View.
     */
    public void playMusic(View view) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if("com.c323proj8.dherthog.MusicPlayerService".equals(service.service.getClassName()))
                return;

        Intent intent = new Intent(this, MusicPlayerService.class);
        startService(intent);
    }

    /**
     * Determines if the device has received a OTP and starts MessagesActivity if it has.
     * @param view
     */
    public void verifyOTP(View view) {

        if (otpCodeTV.getText().equals(""))
            return;

        startActivity(new Intent(this, MessagesActivity.class));
    }

    /**
     * Determines if the received message body contains a OTP.
     * @param body The body of the received message.
     */
    private void messageReceived(String body) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) { // If the input is a set of coords, set URL to use lat and lon

            otpCodeTV.setText(body);
        }
    }

    /**
     * A BroadcastReceiver that listens for incoming SMSes.
     */
    BroadcastReceiver otpReceiver = new BroadcastReceiver() {

        /**
         * Receives incoming SMSes and extracts it's body.
         * @param context The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();

            // The code to receive the message was found at https://stackoverflow.com/questions/7089313/android-listen-for-incoming-sms-messages
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        // Since this is a "toy" app, I don't need to check anything but the body
                        // of the message
                        messageReceived(currentMessage.getDisplayMessageBody());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Unregisters the otpReceiver.
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(otpReceiver);
    }

    /**
     * Registers the otpReceiver.
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(otpReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
}