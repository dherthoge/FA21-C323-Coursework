package com.c323proj9.dherthog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class for the User to add an Item to the database.
 */
public class MainActivity extends AppCompatActivity implements DatePicker.Observer, AdapterView.OnItemSelectedListener {
// TODO: Sort items by date, query in remote thread
    private ExpenseDatabase database;
    private ItemDao itemDao;
    private Spinner spinner;
    private Category category;
    private EditText expenseEdt, costEdt;
    private TextView dateLiteralTv;

    /**
     * Obtains references for necessary EditTexts and sets the spinner options. Also obtains a
     * reference to the database and ItemDao.
     * @param savedInstanceState if the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null. This value
     *                           may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(this, ExpenseDatabase.class, "expenseDB")
                .allowMainThreadQueries()
                .build();

        itemDao = database.getItemDAO();

        expenseEdt = findViewById(R.id.edt_expense);
        costEdt = findViewById(R.id.edt_cost);
        dateLiteralTv = findViewById(R.id.tv_date_literal);

        spinner = findViewById(R.id.sp_main_categories);
        initializeSpinner();
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
     * Starts a DatePicker to get a date from the user
     * @param view The clicked View
     */
    public void pickDate(View view) {
        DialogFragment datePicker = DatePicker.getInstance(this);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Sets the displayed date.
     * @param year the selected year
     * @param month the selected month 1-12
     * @param day the selected day of the month (1-31, depending on month)
     */
    @Override
    public void newDate(int year, int month, int day) {
        dateLiteralTv.setText(day + "/" + month + "/" + year);
    }

    /**
     * Adds the expense to the database if all fields (expense, money spend, date, and category)
     * have been populated by the user.
     * @param view The clicked view.
     */
    public void addExpense(View view) {
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

        expense = expenseEdt.getText().toString();
        date = dateLiteralTv.getText().toString();

        Item item = new Item(expense, date, category.toString(), cost);
        itemDao.insert(item);

        expenseEdt.setText("");
        dateLiteralTv.setText("");
        costEdt.setText("");
    }

    /**
     * Launches an ExpenseActivity.
     * @param view The clicked View.
     */
    public void viewExpenses(View view) {
        startActivity(new Intent(this, ExpenseListActivity.class));
    }
}