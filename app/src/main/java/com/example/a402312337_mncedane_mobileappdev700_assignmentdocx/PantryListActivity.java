package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryDataSource;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.adapter.PantryAdapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class PantryListActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private PantryDataSource dataSource;
    private PantryAdapter adapter;
    private RecyclerView rvPantry;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        setTitle("My Pantry");

        rvPantry = findViewById(R.id.rvPantry);
        tvEmpty = findViewById(R.id.tvEmpty);

        adapter = new PantryAdapter(this);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        rvPantry.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditPantryActivity.class)));  // no extra = ADD mode

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        NavigationHelper.setup(this, nav, R.id.nav_pantry);

        dataSource = new PantryDataSource(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        loadPantry();            // refresh every time we come back from add/edit
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    private void loadPantry() {
        ArrayList<PantryItem> items = dataSource.getAllPantryItems();

        boolean alerts = new SettingsManager(this).isExpiryAlertsEnabled();
        adapter.setAlertsEnabled(alerts);
        adapter.setItems(items);

        boolean empty = items.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvPantry.setVisibility(empty ? View.GONE : View.VISIBLE);

        // expiry banner
        TextView banner = findViewById(R.id.tvExpiryBanner);
        int expired = 0, soon = 0;
        if (alerts) {
            for (PantryItem p : items) {
                if (ExpiryHelper.isExpired(p.getExpiryDate())) expired++;
                else if (ExpiryHelper.isExpiringSoon(p.getExpiryDate())) soon++;
            }
        }
        if (expired + soon > 0) {
            String text = "";
            if (expired > 0) text += expired + (expired == 1 ? " item has" : " items have") + " expired";
            if (expired > 0 && soon > 0) text += ", ";
            if (soon > 0) text += soon + (soon == 1 ? " item expires" : " items expire") + " within 3 days";
            banner.setText(text);
            banner.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(this, AddEditPantryActivity.class);
        intent.putExtra(AddEditPantryActivity.EXTRA_ITEM_ID, item.getId());   // EDIT mode
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (dataSource.deletePantryItem(item.getId())) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        loadPantry();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}