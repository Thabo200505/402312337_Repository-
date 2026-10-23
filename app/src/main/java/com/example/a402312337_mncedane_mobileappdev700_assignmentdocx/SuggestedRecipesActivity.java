package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.adapter.RecipeAdapter;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic.RecipeMatcher;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryDataSource;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity
        implements RecipeAdapter.Listener {

    private PantryDataSource dataSource;
    private RecipeAdapter strictAdapter, almostAdapter;
    private RecyclerView rvStrict, rvAlmost;
    private TextView tvNoMatches, tvAlmostHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle("Suggested Recipes");

        tvNoMatches = findViewById(R.id.tvNoMatches);
        tvAlmostHeader = findViewById(R.id.tvAlmostHeader);
        rvStrict = findViewById(R.id.rvStrict);
        rvAlmost = findViewById(R.id.rvAlmost);

        strictAdapter = new RecipeAdapter(this);
        almostAdapter = new RecipeAdapter(this);

        rvStrict.setLayoutManager(new LinearLayoutManager(this));
        rvStrict.setAdapter(strictAdapter);
        rvStrict.setNestedScrollingEnabled(false);   // the page scrolls, not each list

        rvAlmost.setLayoutManager(new LinearLayoutManager(this));
        rvAlmost.setAdapter(almostAdapter);
        rvAlmost.setNestedScrollingEnabled(false);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        NavigationHelper.setup(this, nav, R.id.nav_suggested);

        dataSource = new PantryDataSource(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        loadSuggestions();     // re-run every time, so changes in the pantry show up
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    private void loadSuggestions() {
        ArrayList<PantryItem> pantry = dataSource.getAllPantryItems();
        ArrayList<Recipe> allRecipes = dataSource.getAllRecipes();

        // ----- STRICT list: every ingredient present in enough quantity -----
        List<Recipe> strict = RecipeMatcher.getStrictMatches(allRecipes, pantry);
        strictAdapter.setData(strict, null);

        boolean noMatches = strict.isEmpty();
        tvNoMatches.setVisibility(noMatches ? View.VISIBLE : View.GONE);
        rvStrict.setVisibility(noMatches ? View.GONE : View.VISIBLE);

        // ----- ALMOST THERE list (bonus): missing exactly one ingredient -----
        List<Recipe> almost = RecipeMatcher.getAlmostThere(allRecipes, pantry);
        Map<Integer, String> notes = new HashMap<>();
        for (Recipe r : almost) {
            List<String> missing = RecipeMatcher.getMissingNames(r, pantry);
            if (!missing.isEmpty()) {
                notes.put(r.getId(), "Missing: " + missing.get(0));
            }
        }
        almostAdapter.setData(almost, notes);

        boolean hasAlmost = !almost.isEmpty();
        tvAlmostHeader.setVisibility(hasAlmost ? View.VISIBLE : View.GONE);
        rvAlmost.setVisibility(hasAlmost ? View.VISIBLE : View.GONE);
    }

    // tap a recipe -> Recipe Detail screen (Intent passes the recipe id)
    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}