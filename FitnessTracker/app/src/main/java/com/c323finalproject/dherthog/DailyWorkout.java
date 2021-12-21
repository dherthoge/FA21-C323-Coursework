package com.c323finalproject.dherthog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * An Object to store information for a DailyWorkout
 */
@Entity
public class DailyWorkout {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The date the user completed the exercise, of the form yyyy-MM-dd.
     */
    @ColumnInfo
    @NotNull
    private final String date;

    /**
     * How long the user took to complete the workout in milliseconds.
     */
    @ColumnInfo
    private final long timeInMillis;

    public DailyWorkout(@NonNull String date, long timeInMillis) {
        this.date = date;
        this.timeInMillis = timeInMillis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getDate() {
        return date;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }
}
