package com.c323finalproject.dherthog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * An Object to store information for a Recipe.
 */
@Entity
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    @NotNull
    private final String name; // The name of the Exercise

    /**
     * The filepath to the image of the Exercise, of the form
     * /data/user/0/com.c323finalproject.dherthog/files/profilePicture/*image_name*.jpeg
     */
    @ColumnInfo
    @NotNull
    private final String imageFilePath;

    public Exercise (@NonNull String name, @NonNull String imageFilePath) {
        this.name = name;
        this.imageFilePath = imageFilePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getImageFilePath() {
        return imageFilePath;
    }
}
