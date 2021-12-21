package com.c323finalproject.dherthog;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * A Dao for the Mode class.
 */
@Dao
public interface ModeDao {

    /**
     * Updates the given Recipe into the database. This method should only take one Mode with id of
     * 0.
     * @param mode The Mode to insert
     */
    @Update
    void update(Mode... mode);

    /**
     * Returns an ArrayList of a single Mode with id 0.
     */
    @Query("SELECT * FROM mode")
    List<Mode> getAllMode();
}
