package com.c323proj9.dherthog;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * A DAO for an Item.
 */
@Dao
 interface ItemDao {

    /**
     * Inserts the given Items into the database.
     * @param items The Items to insert
     */
    @Insert
    void insert(Item... items);

    /**
     * Updates the given Items in the database.
     * @param items The Items to update
     */
    @Update
    void update(Item... items);

    /**
     * Deletes the given Items from the database.
     * @param items The Items to delete
     */
    @Delete
    void delete(Item... items);

    /**
     * Get an Item from the database by the given id
     * @param id The id of the Item to get
     */
    @Query("SELECT * FROM items WHERE id = :id")
    List<Item> getItemsById(String id);

    /**
     * Gets a list of Items from the database by the given category
     * @param category The category of Items to get
     */
    @Query("SELECT * FROM items WHERE category = :category")
    List<Item> getItemsByCategory(String category);

    /**
     * Gets a list of Items from the database by the given category and filters those by the given
     * criteria
     * @param category The category of Items to get
     * @param criteria The criteria to filter the Items by (any String)
     */
    @Query("SELECT * FROM items WHERE category = :category AND (expense = :criteria OR cost = :criteria OR date = :criteria)")
    List<Item> getItemsByCriteria(String category, String criteria);
}
