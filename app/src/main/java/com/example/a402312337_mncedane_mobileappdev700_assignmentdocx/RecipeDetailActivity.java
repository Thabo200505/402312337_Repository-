package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic.RecipeMatcher;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryDataSource;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.Recipe;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.RecipeIngredient;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    // key used by SuggestedRecipesActivity to pass the recipe id
    public static final String EXTRA_RECIPE_ID = "recipeId";

    private PantryDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        // 1. read the recipe id sent through the Intent
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);

        dataSource = new PantryDataSource(this);
        dataSource.open();

        // 2. load the recipe and the current pantry
        Recipe recipe = (recipeId == -1) ? null : dataSource.getRecipe(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        ArrayList<PantryItem> pantry = dataSource.getAllPantryItems();

        setTitle("Recipe");
        showRecipe(recipe, pantry);
    }

    private void showRecipe(Recipe recipe, ArrayList<PantryItem> pantry) {
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);
        LinearLayout llIngredients = findViewById(R.id.llIngredients);

        tvName.setText(recipe.getName());
        tvSteps.setText(recipe.getSteps());

        // 3. one row per ingredient, with a tick or a cross
        int missingCount = 0;
        llIngredients.removeAllViews();

        for (RecipeIngredient ri : recipe.getIngredients()) {
            boolean have = RecipeMatcher.isIngredientAvailable(ri, recipe, pantry);
            if (!have) missingCount++;

            TextView row = new TextView(this);
            row.setTextSize(16);
            row.setPadding(0, dp(4), 0, dp(4));
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            String symbol = have ? "\u2713  " : "\u2717  ";   // check mark / cross
            String text = symbol + formatQuantity(ri.getQuantity()) + " "
                    + ri.getUnit() + "  " + ri.getName();
            if (!have) text += "  (missing)";

            row.setText(text);
            row.setTextColor(ContextCompat.getColor(this,
                    have ? R.color.ingredient_have : R.color.ingredient_missing));
            llIngredients.addView(row);
        }

        // 4. status line
        if (missingCount == 0) {
            tvStatus.setText("You have everything you need to cook this now.");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.ingredient_have));
        } else {
            tvStatus.setText("Missing " + missingCount
                    + (missingCount == 1 ? " ingredient" : " ingredients") + ".");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.ingredient_missing));
        }
    }

    // 2.0 -> "2", 2.5 -> "2.5"
    private String formatQuantity(double q) {
        return (q == Math.floor(q)) ? String.valueOf((long) q) : String.valueOf(q);
    }

    // convert dp to pixels
    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
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