package com.c323proj9.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * An activity to display a list of the user's created Items.
 */
public class ExpenseListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecyclerAdapter.EventObserver {

    private ExpenseDatabase database;
    private ItemDao itemDao;
    private Spinner spinner;
    private EditText searchCriteria;
    private TextView statusMsgTv;
    private Category curCategory;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    /**
     * Obtains references for necessary Views and sets the spinner options. Also obtains a
     * reference to the database and ItemDao.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        database = Room.databaseBuilder(this, ExpenseDatabase.class, "expenseDB")
                .allowMainThreadQueries()
                .build();
        itemDao = database.getItemDAO();

        /*
         * have to initialize here to get items from the database. In MainActivity, category's
         * initialization is performed in the normal flow of the program
         */
        curCategory = Category.FOOD;

        searchCriteria = findViewById(R.id.edt_search_criteria);
        statusMsgTv = findViewById(R.id.tv_status_msg);
        spinner = findViewById(R.id.sp_expense_categories);
        recyclerView = findViewById(R.id.recycler_view_items);

        initializeSpinner();
        search(null);
    }

    /**
     * Creates a RecyclerAdapter to manage list items.
     */
    private void setAdapter(ArrayList<Item> items) {
        recyclerAdapter = new RecyclerAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Populates the categories Spinner with predefined values.
     */
    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Queries the database for Items matching curCategory.
     */
    private void displayItems(ArrayList<Item> items) {
        Collections.sort(items);
        setAdapter(items);

        if (items.size() == 0)
            statusMsgTv.setText("No expenses found!");
        else
            statusMsgTv.setText("");
    }

    /**
     * Sets the user's selected Category.
     * @param adapterView The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param i The position of the view in the adapter
     * @param l The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                curCategory = Category.FOOD;
                break;
            case 1:
                curCategory = Category.TRANSPORTATION;
                break;
            case 2:
                curCategory = Category.HOUSING;
                break;
            case 3:
                curCategory = Category.ENTERTAINMENT;
                break;
        }
    }

    /**
     * If the selection disappears from the spinner, sets category to null.
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        curCategory = null;
    }

    /**
     * Queries the DB for items matching the selected category and search criteria.
     * @param view The clicked view
     */
    public void search(View view) {

        String criteria = searchCriteria.getText().toString().trim();
        criteria.replace("$", "");

        QueryDatabase queryDatabase = new QueryDatabase();
        if (!criteria.equals(""))
            queryDatabase.execute(criteria);
        else
            queryDatabase.execute();
    }

    /**
     * Reloads the search results after user's possible edit.
     */
    @Override
    protected void onResume() {
        super.onResume();
        search(null);
    }

    /**
     * Launches a new Activity for the user to edit the selected Item.
     * @param item The item to edit.
     */
    @Override
    public void editClicked(Item item) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("ITEM_ID", item.getId());
        startActivityForResult(intent, 42);
    }

    /**
     * Delets the given item from expenseDB and repopulates the RecyclerAdapter
     * @param item The Item to delete.
     */
    @Override
    public void deleteClicked(Item item) {
        itemDao.delete(item);
        search(null);
    }


    /**
     * Inner class used to query the database
     */
    private class QueryDatabase extends AsyncTask<String, Void, String> {

        ArrayList<Item> items;

        /**
         * Determines whether to search by category and criteria of category alone.
         * @return An empty string
         */
        @Override
        protected String doInBackground(String... criteria) {
            if (criteria.length == 0)
                getItems();
            else
                getItems(criteria[0]);

            return "";
        }

        /**
         * Queries the Database for Items by category only.
         * @return The result of the query
         */
        private ArrayList<Item> getItems() {
            items = (ArrayList<Item>) itemDao.getItemsByCategory(curCategory.toString());
            return items;
        }

        /**
         * Queries the Database for Items by category and criteria.
         * @return The result of the query
         */
        private ArrayList<Item> getItems(String criteria) {
            items = (ArrayList<Item>) itemDao.getItemsByCriteria(curCategory.toString(), criteria);
            return items;
        }

        /**
         * Triggers UI to display the returned Items
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            displayItems(items);
        }
    }
}