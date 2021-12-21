package com.c323finalproject.dherthog;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * An Object to store information for the user's selected mode.
 */
@Entity
public class Mode {

    @PrimaryKey
    private final int id;

    @ColumnInfo
    private int mode;

    public Mode(int id, int mode) {
        this.id = id;
        this.mode = mode;
    }

    /**
     * @return The amount of time (in seconds) the mode represents.
     */
    public int getTime() {
        // Determine the time of the given Mode
        int time = 20;
        switch (mode) {
            case 1:
                time = 30;
                break;
            case 2:
                time = 50;
                break;
            case 3:
                time = 120;
                break;
        }

        return time;
    }

    public int getId() {
        return id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
