package com.c323.midtermproject.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Creates an Item of the given category.
 */
public class ItemAddActivity extends AppCompatActivity implements Updatable {


    /* Stores an identifier to indicate the current fragment displayed in Activity 2.
     * If the user cancels adding an item, we can just reference this to determine which category to
     * display in Activity 3.
     */
    private Category category;

    /**
     * Sets the fragment to display.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                          down then this Bundle contains the data it most recently supplied in
     *                          onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (savedInstanceState == null) {
            determineFragment();
        }
    }

    /**
     * Determines which fragment to display based on the identifier in the Intent's Bundle.
     */
    private void determineFragment() {
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            category = Category.valueOf(extras.getString("CATEGORY", "CITY"));

        switch (category) {
            case CITY:
                setCityFragment();
                break;
            case MONUMENT:
                setMonumentsFragment();
                break;
            case CAMPSITE:
                setCampingFragment();
                break;
        }
    }

    /**
     * Sets the displayed fragment as an CityFragment.
     */
    private void setCityFragment() {
        category = Category.CITY;
        CitiesFragment citiesFragment =
                new CitiesFragment(this.getApplicationContext(), this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lin_layout_fragment_holder, citiesFragment);
        fragmentTransaction.commit();
    }

    /**
     * Sets the displayed fragment as an Monuments Fragment.
     */
    private void setMonumentsFragment() {
        category = Category.MONUMENT;
        MonumentsFragment citiesFragment =
                new MonumentsFragment(this.getApplicationContext(), this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lin_layout_fragment_holder, citiesFragment);
        fragmentTransaction.commit();
    }

    /**
     * Sets the displayed fragment as an CampingFragment.
     */
    private void setCampingFragment() {
        category = Category.CAMPSITE;
        CampsiteFragment campsiteFragment =
                new CampsiteFragment(this.getApplicationContext(), this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lin_layout_fragment_holder, campsiteFragment);
        fragmentTransaction.commit();
    }


    /**
     * Notifies the parent activity item creation was canceled.
     */
    @Override
    public void cancelClicked() {
        Intent intent = new Intent(this, ItemDisplayActivity.class);
        intent.putExtra("CATEGORY", category.toString());
        startActivity(intent);
        finish();
    }

    /**
     * Notifies the parent activity an item was created.
     */
    @Override
    public void addClicked() {

        Toast.makeText(this, "Data has been added!", Toast.LENGTH_SHORT).show();
        finish();
    }


}