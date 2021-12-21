package com.c323proj9.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A class to allow the user to edit an Item's information
 */
public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ExpenseDatabase database;
    private ItemDao itemDao;
    private Spinner spinner;
    private Category category;
    private EditText expenseEdt, costEdt;
    private TextView dateLiteralTv;
    private Item item;

    /**
     * Obtains references for necessary Views and sets the spinner options. Also obtains a
     * reference to the database and ItemDao and queries the database for the item to edit.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        database = Room.databaseBuilder(this, ExpenseDatabase.class, "expenseDB")
                .allowMainThreadQueries()
                .build();

        itemDao = database.getItemDAO();

        expenseEdt = findViewById(R.id.edt_edit_expense);
        costEdt = findViewById(R.id.edt_edit_cost);
        dateLiteralTv = findViewById(R.id.tv_edit_date_literal);

        spinner = findViewById(R.id.sp_edit_category);
        initializeSpinner();

        Bundle extras = getIntent().getExtras();

        QueryDatabase queryDatabase = new QueryDatabase();
        queryDatabase.execute(extras.getInt("ITEM_ID") + "");
    }

    /**
     * Populated fields with the selected Item's information.
     */
    private void initializeDisplay() {
        expenseEdt.setText(item.getExpense());
        costEdt.setText(item.getCost() + "");
        dateLiteralTv.setText(item.getDate());

        switch (item.getCategory()) {
            case "FOOD":
                spinner.setSelection(0);
                break;
            case "TRANSPORTATION":
                spinner.setSelection(1);
                break;
            case "HOUSING":
                spinner.setSelection(2);
                break;
            case "ENTERTAINMENT":
                spinner.setSelection(3);
                break;
        }
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
     * Updates the selected Item's information in the database.
     * @param view The clicked view
     */
    public void saveItem(View view) {
        String expense = expenseEdt.getText().toString(), date = dateLiteralTv.getText().toString();
        double cost = -1;

        if (expense.equals("")) {
            Toast.makeText(this, "Please enter an expense title!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            cost = Double.parseDouble(costEdt.getText().toString());
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(this, "Please enter amount spend as a double!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.equals("")) {
            Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (category == null) {
            Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
            return;
        }

        item.setExpense(expense);
        item.setCost(cost);
        item.setDate(date);
        item.setCategory(category.toString());

        itemDao.update(item);

        finish();
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
                category = Category.FOOD;
                break;
            case 1:
                category = Category.TRANSPORTATION;
                break;
            case 2:
                category = Category.HOUSING;
                break;
            case 3:
                category = Category.ENTERTAINMENT;
                break;
        }
    }

    /**
     * If the selection disappears from the spinner, sets category to null.
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = Category.FOOD;
    }

    /**
     * Inner class used to query the database
     */
    private class QueryDatabase extends AsyncTask<String, Void, String> {

        /**
         * Determines whether to search by category and criteria of category alone.
         * @return An empty string
         */
        @Override
        protected String doInBackground(String... id) {
            getItems(id[0]);

            return "";
        }

        /**
         * Queries the Database for Items by unique id.
         * @return The result of the query
         */
        private Item getItems(String id) {
            item = itemDao.getItemsById(id).get(0);
            return item;
        }

        /**
         * Triggers UI to display the returned Items
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            initializeDisplay();
        }
    }
}