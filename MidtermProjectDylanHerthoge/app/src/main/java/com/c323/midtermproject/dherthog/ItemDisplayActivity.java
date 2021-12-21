package com.c323.midtermproject.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Displays Items based on the user's selected category.
 */
public class ItemDisplayActivity extends AppCompatActivity implements ViewOptionFragment.DisplayOptionListener, ActionObserver, RecyclerAdapter.PopupObserver {

    private Category category;
    private ArrayList<Item> items;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerAdapterCardView recyclerAdapterCardView;
    private ItemManager itemManager;
    private DisplayOption displayOption;

    enum DisplayOption {
        RECYCLERVIEW,
        CARDVIEW
    }

    /**
     * Gets the Items to display and create a fragment to display them.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        displayOption = DisplayOption.RECYCLERVIEW;

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            category = Category.valueOf(extras.getString("CATEGORY", "CITY"));

        itemManager = ItemManager.getInstance(getApplicationContext());

        populateItems();

        recyclerView = findViewById(R.id.recycler_view_items);
        setViewOptionsFragment();
        setRecyclerAdapter();
    }

    /**
     * Populates the items ArrayList with items from the current category.
     */
    private void populateItems() {
        items = new ArrayList<>();
        switch (category) {
            case CITY:
                ArrayList<City> cities = itemManager.getCitiesCopy();
                for (City c: cities)
                    items.add(c);
                break;
            case MONUMENT:
                ArrayList<Monument> monuments = itemManager.getMonumentsCopy();
                for (Monument m: monuments)
                    items.add(m);
                break;
            case CAMPSITE:
                ArrayList<Campsite> campsites = itemManager.getCampsitesCopy();
                for (Campsite c: campsites)
                    items.add(c);
                break;
        }
    }

    /**
     * Creates a ViewOptionFragment and makes it adds it to the Activity.
     */
    private void setViewOptionsFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ViewOptionFragment viewOptionFragment = new ViewOptionFragment(this);
        fragmentTransaction.replace(R.id.con_lay_view_options_fragment_holder, viewOptionFragment);
        fragmentTransaction.commit();
    }

    /**
     * Creates a RecyclerAdapter to manage list items.
     */
    private void setRecyclerAdapter() {
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), displayOption, category, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Creates a RecyclerAdapterCardView to manage list items.
     */
    private void setRecyclerAdapterCardView() {
        recyclerAdapterCardView = new RecyclerAdapterCardView(getApplicationContext(), category, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapterCardView);
    }

    /**
     * Switches between displayed layout (RecyclerView or Cardview)
     * @param option
     */
    @Override
    public void optionChanged(ItemDisplayActivity.DisplayOption option) {
        this.displayOption = option;

        if (option == DisplayOption.RECYCLERVIEW)
            setRecyclerAdapter();
        else
            setRecyclerAdapterCardView();
    }

    /**
     * Notifies ItemManager that an event needs to be deleted
     * @param position The position of the item that needs to be deleted
     */
    @Override
    public void deleteItem(int position) {

        switch (category) {
            case CITY:
                itemManager.removeCity(position);
                break;
            case MONUMENT:
                itemManager.removeMonument(position);
                break;
            case CAMPSITE:
                itemManager.removeCampsite(position);
                break;
        }
        Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();

        switch (displayOption) {
            case RECYCLERVIEW:
                setRecyclerAdapter();
                break;
            case CARDVIEW:
                setRecyclerAdapterCardView();
                break;
        }
    }

    /**
     * Notifies ItemManager that an event needs to be favorited
     * @param position The position of the item that needs to be deleted
     */
    @Override
    public void favoriteItem(int position) {
        switch (category) {
            case CITY:
                itemManager.cityFavorited(position);
                break;
            case MONUMENT:
                itemManager.monumentFavorited(position);
                break;
            case CAMPSITE:
                itemManager.campsiteFavorited(position);
                break;
        }
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

    private boolean popupActive = false;
    /**
     * Displays a popup window for the selected item
     * @param position The position of the item that needs to be deleted
     */
    @Override
    public void displayPopup(int position) {

        if (popupActive) return;

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (category) {
            case CITY:
                {
                    View popupView = inflater.inflate(R.layout.popup_city, null);

                    // Setup popup Text/Image Views
                    City city = (City) items.get(position);
                    ((TextView) popupView.findViewById(R.id.popup_tv_city_name)).setText(city.getName());
                    ((TextView) popupView.findViewById(R.id.popup_tv_city_time_to_visit)).setText(city.getBestTimeToVisit());
                    ((TextView) popupView.findViewById(R.id.popup_tv_city_tourist_spots)).setText(city.getTouristSpots());
                    ((TextView) popupView.findViewById(R.id.popup_tv_city_location)).setText(city.getLocation());
                    ((ImageView) popupView.findViewById(R.id.popup_iv_city_image)).setImageBitmap(city.getBitmap());

                    // Set click listener to dismiss after it's clicked
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            popupActive = false;
                        }
                    });

                    // Add click listeners for favorite and map btns respectively
                    Button favoriteBtn = popupView.findViewById(R.id.popup_btn_city_favorite);
                    favoriteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemManager.cityFavorited(position);
                        }
                    });

                    Button mapDisplayBtn = popupView.findViewById(R.id.popup_btn_city_map_display);
                    mapDisplayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayItemOnMap(position);
                        }
                    });

                    // Display the popup window
                    popupWindow.showAtLocation(findViewById(R.id.lin_lay_item_popup_container), Gravity.CENTER, 0, 0);
                    popupActive = true;
                    break;
                }
            case MONUMENT:
                {
                    View popupView = inflater.inflate(R.layout.popup_monument, null);

                    // Setup popup Text/Image Views
                    Monument monument = (Monument) items.get(position);
                    ((TextView) popupView.findViewById(R.id.popup_tv_monu_name)).setText(monument.getName());
                    ((TextView) popupView.findViewById(R.id.popup_tv_monu_history)).setText(monument.getHistory());
                    ((TextView) popupView.findViewById(R.id.popup_tv_monu_time_to_visit)).setText(monument.getBestTimeToVisit());
                    ((TextView) popupView.findViewById(R.id.popup_tv_monu_ticket_price)).setText(monument.getTicketPrice().toString());
                    ((TextView) popupView.findViewById(R.id.popup_tv_monu_location)).setText(monument.getLocation());
                    ((ImageView) popupView.findViewById(R.id.popup_iv_monu_image)).setImageBitmap(monument.getBitmap());

                    // Set click listener to dismiss after it's clicked
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            popupActive = false;
                        }
                    });

                    // Add click listeners for favorite and map btns respectively
                    Button favoriteBtn = popupView.findViewById(R.id.popup_btn_monu_favorite);
                    favoriteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemManager.monumentFavorited(position);
                        }
                    });

                    Button mapDisplayBtn = popupView.findViewById(R.id.popup_btn_monu_map_display);
                    mapDisplayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayItemOnMap(position);
                        }
                    });

                    // Display the popup window
                    popupWindow.showAtLocation(findViewById(R.id.lin_lay_item_popup_container), Gravity.CENTER, 0, 0);
                    popupActive = true;
                    break;
                }
            case CAMPSITE:
                {
                    View popupView = inflater.inflate(R.layout.popup_camping, null);

                    // Setup popup Text/Image Views
                    Campsite campsite = (Campsite) items.get(position);
                    ((TextView) popupView.findViewById(R.id.popup_tv_campsite_name)).setText(campsite.getName());
                    ((TextView) popupView.findViewById(R.id.popup_tv_campsite_time_to_visit)).setText(campsite.getBestTimeToVisit());
                    ((TextView) popupView.findViewById(R.id.popup_tv_campsite_nearest_location)).setText(campsite.getNearestMetroLocation().toString());
                    ((TextView) popupView.findViewById(R.id.popup_tv_campsite_location)).setText(campsite.getLocation());
                    ((ImageView) popupView.findViewById(R.id.popup_iv_campsite_image)).setImageBitmap(campsite.getBitmap());

                    // Set click listener to dismiss after it's clicked
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            popupActive = false;
                        }
                    });

                    // Add click listeners for favorite and map btns respectively
                    Button favoriteBtn = popupView.findViewById(R.id.popup_btn_campsite_favorite);
                    favoriteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemManager.campsiteFavorited(position);
                        }
                    });

                    // Display the popup window
                    Button mapDisplayBtn = popupView.findViewById(R.id.popup_btn_campsite_map_display);
                    mapDisplayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayItemOnMap(position);
                        }
                    });

                    popupWindow.showAtLocation(findViewById(R.id.lin_lay_item_popup_container), Gravity.CENTER, 0, 0);
                    popupActive = true;
                    break;
                }
        }
    }
}