package com.c323finalproject.dherthog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * A class to manage reading and writing Images.
 */
public class ImageManager {

    /**
     * Writes the given bitmap to the given filePath.
     * @param filePath The absolute path to write to.
     * @param bitmap The Bitmap of the image to write.
     * @return True if the image was successfully written, false otherwise.
     */
    public static boolean writeImage(String filePath, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Reads a Bitmap from the given filePath.
     * @param filePath The absolute path to read
     * @return The Bitmap at the given filePath, or null if there was any error
     */
    @Nullable
    public static Bitmap readImage(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes anything at the given filePath.
     * @param filePath The filePath to delete
     * @return True if the file was successfully deleted, false otherwise.
     */
    public static boolean deleteImage(String filePath) {
        return new File(filePath).delete();
    }

    /**
     * Writes images from the assets folder to the app's files folder.
     * @param context The application's Context
     * @return True if the images were successfully written, false if not
     */
    public static boolean preLoadImages(Context context) {
        // Make the directory to store the exercise images
        File dir = new File(context.getFilesDir() + "/exerciseImages");
        try {
            dir.mkdir();
        }catch(Exception e){
            e.printStackTrace();
        }

        String[] exerciseNames = context.getResources().getStringArray(R.array.exercises);

        for (int i = 0; i < exerciseNames.length; i++) {

            try {
                InputStream is = context.getAssets().open("images/" + exerciseNames[i] + ".jpeg");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                FileOutputStream fos = new FileOutputStream(context.getFilesDir() + "/exerciseImages/" + exerciseNames[i] + ".jpeg");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch (Exception e) {
                return false;
            }
        }

        return true;
    }
}
