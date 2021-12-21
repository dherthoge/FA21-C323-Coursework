package com.c323finalproject.dherthog;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * A Dao for the Exercise class.
 */
@Dao
public interface ExerciseDao {

    /**
     * Inserts the given Exercises into the database.
     * @param exercises The Exercises to insert
     */
    @Insert
    void insert(Exercise... exercises);

    /**
     * Updates the given Exercises in the database.
     * @param exercises The Exercises to update
     */
    @Update
    void update(Exercise... exercises);

    /**
     * Deletes the given Exercises from the database.
     * @param exercises The Exercises to delete
     */
    @Delete
    void delete(Exercise... exercises);

    /**
     * Returns an ArrayList of all Exercises.
     */
    @Query("SELECT * FROM exercise")
    List<Exercise> getAllExercise();
}
