package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.Recipe;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.RecipeIngredient;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic.RecipeMatcher;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class RecipeMatcherTest {

    private PantryItem item(String name, double qty, String unit) {
        PantryItem p = new PantryItem();
        p.setName(name); p.setQuantity(qty); p.setUnit(unit);
        return p;
    }

    private Recipe recipe(String name, RecipeIngredient... ings) {
        Recipe r = new Recipe();
        r.setName(name);
        r.setIngredients(new ArrayList<>(Arrays.asList(ings)));
        return r;
    }

    private RecipeIngredient ing(String name, double qty, String unit) {
        RecipeIngredient i = new RecipeIngredient();
        i.setName(name); i.setQuantity(qty); i.setUnit(unit);
        return i;
    }

    @Test
    public void fourOfFiveIngredients_isNotSuggested() {
        Recipe r = recipe("Test", ing("a", 1, "pcs"), ing("b", 1, "pcs"),
                ing("c", 1, "pcs"), ing("d", 1, "pcs"), ing("e", 1, "pcs"));
        List<PantryItem> pantry = Arrays.asList(item("a", 1, "pcs"), item("b", 1, "pcs"),
                item("c", 1, "pcs"), item("d", 1, "pcs"));
        assertTrue(RecipeMatcher.getStrictMatches(Arrays.asList(r), pantry).isEmpty());
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        Recipe r = recipe("Salad", ing("tomato", 2, "pcs"));
        List<PantryItem> pantry = Arrays.asList(item("Tomatoes", 3, "pcs"));
        assertEquals(1, RecipeMatcher.getStrictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void unitDifferences_kgCoversGrams() {
        Recipe r = recipe("Bread", ing("flour", 250, "g"));
        List<PantryItem> pantry = Arrays.asList(item("flour", 1, "kg"));
        assertEquals(1, RecipeMatcher.getStrictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void notEnoughQuantity_isNotSuggested() {
        Recipe r = recipe("Pancakes", ing("eggs", 3, "pcs"));
        List<PantryItem> pantry = Arrays.asList(item("egg", 2, "pcs"));
        assertTrue(RecipeMatcher.getStrictMatches(Arrays.asList(r), pantry).isEmpty());
    }
}
