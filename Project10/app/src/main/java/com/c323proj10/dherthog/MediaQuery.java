package com.c323proj10.dherthog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MediaQuery {

    /**
     * Queries all songs in external storage.
     * @param context The application context
     * @return A list of songs in external storage
     */
    @SuppressLint("Range")
    public static ArrayList<Song> querySongs(Context context) {
        ArrayList<Song> songs = new ArrayList<>();

        // Query the content resolver for audio files
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null)
            return null;

        // Iterate through the returned cursor to get song information
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

                // If the item at the cursor is not music continue
                if (isMusic == 0)
                    continue;

                Song song = new Song();
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setDuration(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION) + "");

                songs.add(song);
            } while (cursor.moveToNext());
        }

        return songs;
    }

    /**
     * Queries all videos in external storage.
     * @param context The application context
     * @return A list of videos in external storage
     */
    @SuppressLint("Range")
    public static ArrayList<Video> queryVideos(Context context) {
        ArrayList<Video> videos = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                "LOWER (" + MediaStore.Video.Media.DATE_TAKEN + ") DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Video video = new Video();
                video.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                video.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                videos.add(video);
            } while (cursor.moveToNext());
        }

        return videos;

        /*
        ArrayList<Song> songs = new ArrayList<>();

        // projections for song query
        String[] songProjection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };
        // Query the content resolver for audio files
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songProjection, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null)
            return null;

        // Iterate through the returned cursor to get song information
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

            if (isMusic != 0) {
                Song song = new Song();

                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

                if (!new File(song.getPath()).exists())
                    continue;

                song.setSongId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                song.setSongTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setDuration(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                songs.add(song);
            }
        }

        return songs;
        */
    }
}
