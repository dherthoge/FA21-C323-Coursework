package com.c323proj9.dherthog;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * A representation of a database to keep track of user's expenses.
 */
@Database(entities = {Item.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {

    /**
     * Get's the DAO for an Item.
     * @return
     */
    public abstract ItemDao getItemDAO();
}
