package com.c323finalproject.dherthog;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * A Dao for the DailyWorkout class.
 */
@Dao
public interface DailyWorkoutDao {

    /**
     * Inserts the given DailyWorkouts into the database.
     * @param dailyWorkouts The DailyWorkouts to insert
     */
    @Insert
    void insert(DailyWorkout... dailyWorkouts);

    /**
     * Returns an ArrayList of all DailyWorkouts.
     */
    @Query("SELECT * FROM dailyWorkout")
    List<DailyWorkout> getAllDailyWorkout();
}
