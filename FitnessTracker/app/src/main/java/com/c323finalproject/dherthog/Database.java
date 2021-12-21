package com.c323finalproject.dherthog;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * A representation of a database to keep track of user's Exercises.
 */
@androidx.room.Database(entities = {Mode.class, Exercise.class, DailyWorkout.class}, version = 1)
public abstract class Database extends RoomDatabase {

    /**
     * Get's the DAO for Mode.
     * @return A ModeDao instance
     */
    public abstract ModeDao getModeDAO();

    /**
     * Get's the DAO for Exercise.
     * @return A ExerciseDao instance
     */
    public abstract ExerciseDao getExerciseDAO();

    /**
     * Get's the DAO for DailyWorkout.
     * @return A DailyWorkoutDao instance
     */
    public abstract DailyWorkoutDao getDailyWorkoutDAO();

    /**
     * The current instance of the Database.
     */
    private static volatile Database INSTANCE;

    /**
     * Returns the current instance of the database, or creates a new one from
     * assets/database/database.db
     * @param context The Context of the application
     * @return An instance of the database
     */
    public static Database getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "database")
                            .allowMainThreadQueries()
                            .createFromAsset("database/database.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
