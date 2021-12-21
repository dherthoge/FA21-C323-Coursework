package com.c323.midtermproject.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Activity do display the user's favorite Items.
 */
public class FavoritesActivity extends AppCompatActivity implements ActionObserver {

    private ArrayList<Item> items;
    private RecyclerView recyclerView;
    private RecyclerAdapterFavorites recyclerAdapterFavorites;
    private ItemManager itemManager;

    /**
     * Gets ids of necessary Views and loads the user's information.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        itemManager = ItemManager.getInstance(getApplicationContext());
        items = itemManager.getFavoriteItemsCopy();

        recyclerView = findViewById(R.id.recycler_view_favorites);
        setRecyclerAdapter();
    }

    /**
     * Creates a RecyclerAdapter to manage list items.
     */
    private void setRecyclerAdapter() {
        recyclerAdapterFavorites = new RecyclerAdapterFavorites(getApplicationContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapterFavorites);
    }

    /**
     * Launches a MapActivity to display the selected Item and the user's location.
     * @param position The position of the Item to display
     */
    @Override
    public void displayItemOnMap(int position) {

        Item itemToDisplay = items.get(position);

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("NAME", itemToDisplay.getName());
        intent.putExtra("LOCATION", itemToDisplay.getLocation());
        intent.putExtra("LONGITUDE", (float) itemToDisplay.getLongitude());
        intent.putExtra("LATITUDE", (float) itemToDisplay.getLatitude());
        startActivity(intent);
    }


    // Unused imports
    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void favoriteItem(int position) {

    }
}