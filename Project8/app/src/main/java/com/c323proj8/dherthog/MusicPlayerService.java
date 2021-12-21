package com.c323proj8.dherthog;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * A simple background service for playing a song.
 */
public class MusicPlayerService extends Service {

    private MediaPlayer mp;

    /**
     * Uses a separate Thread to play the song.
     */
    final class PlayerThread implements Runnable {

        /**
         * Creates a MediaPlayer instance and starts the mp3.
         */
        @Override
        public void run() {
            mp = MediaPlayer.create(MusicPlayerService.this, R.raw.so_we_wont_forget);
            mp.start();
        }
    }

    /**
     * Creates a new Thread for playback and registers home button listeners.
     * @param intent The Intent supplied to Context.startService(Intent), as given. This may be null
     *               if the service is being restarted after its process has gone away, and it had
     *               previously returned anything except START_STICKY_COMPATIBILITY.
     * @param flags Additional data about this start request. Value is either 0 or a combination of
     *              START_FLAG_REDELIVERY, and START_FLAG_RETRY
     * @param startId A unique integer representing this specific request to start. Use with
     *                stopSelfResult(int).
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread playerThread = new Thread(new PlayerThread());
        playerThread.run();

        registerReceiver(homePressedReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        return START_STICKY;
    }

    /**
     * Unused.
     * @param intent The Intent that was used to bind to this service, as given to
     *               Context.bindService. Note that any extras that were included with the Intent at
     *               that point will not be seen here.
     * @return null
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Stops and releases the media player as well as unregisters the BroadcastReceiver listening
     * for home clicks.
     */
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        unregisterReceiver(homePressedReceiver);
        super.onDestroy();
    }

    /**
     * A simple BroadcastReceiver to stop the service.
     */
    private BroadcastReceiver homePressedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };
}
