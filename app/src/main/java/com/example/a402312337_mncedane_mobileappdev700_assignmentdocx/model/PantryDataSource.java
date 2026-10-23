package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class PantryDataSource {

    private SQLiteDatabase database;
    private final PantryDBHelper dbHelper;

    public PantryDataSource(Context context) {
        dbHelper = new PantryDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // ---------- CREATE ----------
    public boolean insertPantryItem(PantryItem p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("quantity", p.getQuantity());
        v.put("unit", p.getUnit());
        v.put("expiry", p.getExpiryDate());
        return database.insert("pantry", null, v) > 0;
    }

    // ---------- UPDATE ----------
    public boolean updatePantryItem(PantryItem p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("quantity", p.getQuantity());
        v.put("unit", p.getUnit());
        v.put("expiry", p.getExpiryDate());
        return database.update("pantry", v, "_id=?",
                new String[]{String.valueOf(p.getId())}) > 0;
    }

    // ---------- DELETE ----------
    public boolean deletePantryItem(int id) {
        return database.delete("pantry", "_id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ---------- READ ----------
    public ArrayList<PantryItem> getAllPantryItems() {
        ArrayList<PantryItem> items = new ArrayList<>();
        Cursor c = database.rawQuery(
                "SELECT _id, name, quantity, unit, expiry FROM pantry ORDER BY name COLLATE NOCASE", null);
        while (c.moveToNext()) {
            items.add(readPantryItem(c));
        }
        c.close();
        return items;
    }

    public PantryItem getPantryItem(int id) {
        PantryItem item = null;
        Cursor c = database.rawQuery(
                "SELECT _id, name, quantity, unit, expiry FROM pantry WHERE _id=?",
                new String[]{String.valueOf(id)});
        if (c.moveToFirst()) item = readPantryItem(c);
        c.close();
        return item;
    }

    private PantryItem readPantryItem(Cursor c) {
        PantryItem p = new PantryItem();
        p.setId(c.getInt(0));
        p.setName(c.getString(1));
        p.setQuantity(c.getDouble(2));
        p.setUnit(c.getString(3));
        p.setExpiryDate(c.isNull(4) ? "" : c.getString(4));
        return p;
    }

    // ---------- RECIPES (read-only, used by the matcher) ----------
    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT _id, name, steps FROM recipe ORDER BY name", null);
        while (c.moveToNext()) {
            Recipe r = new Recipe();
            r.setId(c.getInt(0));
            r.setName(c.getString(1));
            r.setSteps(c.getString(2));
            r.setIngredients(getIngredientsForRecipe(r.getId()));
            recipes.add(r);
        }
        c.close();
        return recipes;
    }

    public Recipe getRecipe(int id) {
        Recipe r = null;
        Cursor c = database.rawQuery("SELECT _id, name, steps FROM recipe WHERE _id=?",
                new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            r = new Recipe();
            r.setId(c.getInt(0));
            r.setName(c.getString(1));
            r.setSteps(c.getString(2));
            r.setIngredients(getIngredientsForRecipe(id));
        }
        c.close();
        return r;
    }

    private ArrayList<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        ArrayList<RecipeIngredient> list = new ArrayList<>();
        Cursor c = database.rawQuery(
                "SELECT name, quantity, unit FROM recipe_ingredient WHERE recipe_id=?",
                new String[]{String.valueOf(recipeId)});
        while (c.moveToNext()) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setName(c.getString(0));
            ri.setQuantity(c.getDouble(1));
            ri.setUnit(c.getString(2));
            list.add(ri);
        }
        c.close();
        return list;
    }
}
