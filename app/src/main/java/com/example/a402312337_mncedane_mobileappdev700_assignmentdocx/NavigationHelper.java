package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    public static void setup(Activity activity, BottomNavigationView nav, int selectedId) {
        nav.setSelectedItemId(selectedId);   // set BEFORE the listener so it doesn't fire
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;

            Class<?> target;
            if (id == R.id.nav_pantry)         target = PantryListActivity.class;
            else if (id == R.id.nav_suggested) target = SuggestedRecipesActivity.class;
            else                               target = SettingsActivity.class;

            Intent intent = new Intent(activity, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
            return true;
        });
    }
}