package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.Recipe;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.RecipeIngredient;

import java.util.*;
public class RecipeMatcher {

    private static final double EPS = 1e-9;

    /** key = normalised name + dimension, so "milk (ml)" and "milk (pcs)" never mix */
    private static String key(String name, String unit) {
        return IngredientNormalizer.normalize(name) + "|" + UnitConverter.dimensionOf(unit);
    }

    private static Map<String, Double> buildPantryStock(List<PantryItem> pantry) {
        Map<String, Double> stock = new HashMap<>();
        for (PantryItem p : pantry) {
            if (p.getQuantity() <= 0) continue;
            String k = key(p.getName(), p.getUnit());
            double base = UnitConverter.toBase(p.getQuantity(), p.getUnit());
            stock.put(k, stock.containsKey(k) ? stock.get(k) + base : base);
        }
        return stock;
    }

    /** How many of this recipe's ingredients are missing or insufficient. */
    public static int countMissing(Recipe recipe, Map<String, Double> stock) {
        // combine duplicate lines inside the recipe first
        Map<String, Double> needed = new HashMap<>();
        for (RecipeIngredient ri : recipe.getIngredients()) {
            String k = key(ri.getName(), ri.getUnit());
            double base = UnitConverter.toBase(ri.getQuantity(), ri.getUnit());
            needed.put(k, needed.containsKey(k) ? needed.get(k) + base : base);
        }
        int missing = 0;
        for (Map.Entry<String, Double> e : needed.entrySet()) {
            double have = stock.containsKey(e.getKey()) ? stock.get(e.getKey()) : 0;
            if (have + EPS < e.getValue()) missing++;
        }
        return missing;
    }

    /** MAIN LIST: recipes where EVERY ingredient is available in enough quantity. */
    public static List<Recipe> getStrictMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, Double> stock = buildPantryStock(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getIngredients().isEmpty()) continue;   // never suggest an empty recipe
            if (countMissing(r, stock) == 0) result.add(r);
        }
        return result;
    }

    /** OPTIONAL BONUS: recipes missing exactly one ingredient. Show in a SEPARATE list. */
    public static List<Recipe> getAlmostThere(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, Double> stock = buildPantryStock(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : recipes) {
            if (!r.getIngredients().isEmpty() && countMissing(r, stock) == 1) result.add(r);
        }
        return result;
    }
}
