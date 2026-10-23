package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryDataSource;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Locale;

public class AddEditPantryActivity extends AppCompatActivity {

    // key used by PantryListActivity to pass the id of the item to edit
    public static final String EXTRA_ITEM_ID = "itemId";

    private static final double MAX_QUANTITY = 100000;

    private TextInputLayout tilName, tilQuantity;
    private MaterialAutoCompleteTextView etName;
    private TextInputEditText etQuantity, etExpiry;
    private Spinner spUnit;
    private ArrayAdapter<String> unitAdapter;

    private PantryDataSource dataSource;
    private PantryItem existingItem = null;   // null = ADD mode, not null = EDIT mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry);

        // back arrow in the toolbar
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        // find views
        tilName = findViewById(R.id.tilName);
        tilQuantity = findViewById(R.id.tilQuantity);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnClearExpiry = findViewById(R.id.btnClearExpiry);

        // name suggestions
        String[] suggestions = getResources().getStringArray(R.array.ingredient_suggestions);
        etName.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions));

        // unit spinner
        String[] units = getResources().getStringArray(R.array.unit_options);
        unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, units);
        spUnit.setAdapter(unitAdapter);

        // database
        dataSource = new PantryDataSource(this);
        dataSource.open();

        // ADD or EDIT? Check whether the Intent carried an item id
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            existingItem = dataSource.getPantryItem(itemId);
        }

        if (existingItem != null) {
            setTitle("Edit Ingredient");
            fillForm(existingItem);
        } else {
            setTitle("Add Ingredient");
            // pre-select the user's preferred unit (from Settings)
            String defaultUnit = new SettingsManager(this).getDefaultUnit();
            int pos = unitAdapter.getPosition(defaultUnit);
            if (pos >= 0) spUnit.setSelection(pos);
        }

        // listeners
        etExpiry.setOnClickListener(v -> showDatePicker());
        btnClearExpiry.setOnClickListener(v -> etExpiry.setText(""));
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> save());
    }

    // ---------- EDIT mode: show the saved values ----------
    private void fillForm(PantryItem item) {
        etName.setText(item.getName());
        etName.dismissDropDown();

        double q = item.getQuantity();
        etQuantity.setText(q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q));

        int pos = unitAdapter.getPosition(item.getUnit());
        if (pos >= 0) spUnit.setSelection(pos);

        etExpiry.setText(item.getExpiryDate());
    }

    // ---------- Date picker ----------
    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();

        // if a date is already in the box, start the picker on it
        String current = etExpiry.getText() == null ? "" : etExpiry.getText().toString();
        if (current.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] p = current.split("-");
            cal.set(Integer.parseInt(p[0]), Integer.parseInt(p[1]) - 1, Integer.parseInt(p[2]));
        }

        new DatePickerDialog(this, (view, year, month, day) ->
                etExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // ---------- Validation ----------
    private boolean validate() {
        boolean valid = true;

        // clear old errors
        tilName.setError(null);
        tilQuantity.setError(null);

        // 1. NAME: required, 2-40 chars, letters/spaces/hyphens/apostrophes only
        String name = etName.getText().toString().trim().replaceAll("\\s+", " ");
        if (name.isEmpty()) {
            tilName.setError("Please enter an ingredient name");
            valid = false;
        } else if (name.length() < 2) {
            tilName.setError("Name is too short");
            valid = false;
        } else if (!name.matches("[A-Za-z][A-Za-z '\\-]*")) {
            tilName.setError("Use letters only (no numbers or symbols)");
            valid = false;
        }

        // 2. QUANTITY: required, a number, more than 0, not silly-large
        String qtyText = etQuantity.getText().toString().trim();
        if (qtyText.isEmpty()) {
            tilQuantity.setError("Please enter a quantity");
            valid = false;
        } else {
            try {
                double qty = Double.parseDouble(qtyText);
                if (qty <= 0) {
                    tilQuantity.setError("Quantity must be greater than 0");
                    valid = false;
                } else if (qty > MAX_QUANTITY) {
                    tilQuantity.setError("Quantity is too large");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                tilQuantity.setError("Enter a valid number");
                valid = false;
            }
        }

        // 3. UNIT: the spinner always has a value, but check anyway
        if (spUnit.getSelectedItem() == null) {
            Toast.makeText(this, "Please choose a unit", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // 4. EXPIRY: optional, but if filled it must look like yyyy-MM-dd
        String expiry = etExpiry.getText() == null ? "" : etExpiry.getText().toString().trim();
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(this, "Invalid expiry date", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    // ---------- Save (create or update) ----------
    private void save() {
        if (!validate()) return;   // stop here if anything is wrong

        String name = etName.getText().toString().trim().replaceAll("\\s+", " ");
        double qty = Double.parseDouble(etQuantity.getText().toString().trim());
        String unit = spUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText() == null ? "" : etExpiry.getText().toString().trim();

        boolean success;
        if (existingItem == null) {
            // CREATE
            PantryItem item = new PantryItem(name, qty, unit, expiry);
            success = dataSource.insertPantryItem(item);
        } else {
            // UPDATE
            existingItem.setName(name);
            existingItem.setQuantity(qty);
            existingItem.setUnit(unit);
            existingItem.setExpiryDate(expiry);
            success = dataSource.updatePantryItem(existingItem);
        }

        if (success) {
            Toast.makeText(this,
                    existingItem == null ? "Ingredient added" : "Ingredient updated",
                    Toast.LENGTH_SHORT).show();
            finish();   // back to the pantry list, whose onResume reloads the data
        } else {
            Toast.makeText(this, "Could not save. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    // back arrow in the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}