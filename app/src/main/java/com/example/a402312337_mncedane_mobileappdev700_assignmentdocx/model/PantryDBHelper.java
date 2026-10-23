package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 1;

    // ---------- Table creation SQL ----------
    private static final String CREATE_PANTRY =
            "CREATE TABLE pantry ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "quantity REAL NOT NULL, "
                    + "unit TEXT NOT NULL, "
                    + "expiry TEXT);";

    private static final String CREATE_RECIPE =
            "CREATE TABLE recipe ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "steps TEXT NOT NULL);";

    private static final String CREATE_RECIPE_INGREDIENT =
            "CREATE TABLE recipe_ingredient ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "recipe_id INTEGER NOT NULL, "
                    + "name TEXT NOT NULL, "
                    + "quantity REAL NOT NULL, "
                    + "unit TEXT NOT NULL, "
                    + "FOREIGN KEY(recipe_id) REFERENCES recipe(_id) ON DELETE CASCADE);";

    public PantryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Runs ONCE, the first time the database is created = "seeded on first run"
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PANTRY);
        db.execSQL(CREATE_RECIPE);
        db.execSQL(CREATE_RECIPE_INGREDIENT);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredient");
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    // ---------- Seeding helpers ----------

    /**
     * Inserts one recipe plus its ingredients.
     * ingredients = repeating groups of: name (String), quantity (number), unit (String)
     */
    private void recipe(SQLiteDatabase db, String name, String steps, Object... ingredients) {
        ContentValues rv = new ContentValues();
        rv.put("name", name);
        rv.put("steps", steps);
        long recipeId = db.insert("recipe", null, rv);

        for (int i = 0; i < ingredients.length; i += 3) {
            ContentValues iv = new ContentValues();
            iv.put("recipe_id", recipeId);
            iv.put("name", (String) ingredients[i]);
            iv.put("quantity", ((Number) ingredients[i + 1]).doubleValue());
            iv.put("unit", (String) ingredients[i + 2]);
            db.insert("recipe_ingredient", null, iv);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {

        recipe(db, "Scrambled Eggs on Toast",
                "1. Beat the eggs with a pinch of salt.\n"
                        + "2. Melt the butter in a pan over low heat.\n"
                        + "3. Pour in the eggs and stir gently until just set.\n"
                        + "4. Toast the bread and serve the eggs on top.",
                "egg", 2, "pcs", "bread", 2, "pcs", "butter", 10, "g");

        recipe(db, "Plain Pancakes",
                "1. Whisk the flour, milk and egg into a smooth batter.\n"
                        + "2. Heat a little oil in a frying pan.\n"
                        + "3. Pour in a ladle of batter and tilt the pan to spread it.\n"
                        + "4. Cook until golden, flip, and cook the other side.",
                "flour", 100, "g", "milk", 200, "ml", "egg", 1, "pcs",
                "cooking oil", 1, "tbsp");

        recipe(db, "Cheese Omelette",
                "1. Beat the eggs in a bowl.\n"
                        + "2. Melt the butter in a pan and pour in the eggs.\n"
                        + "3. When almost set, sprinkle the cheese over one half.\n"
                        + "4. Fold the omelette over and cook for another minute.",
                "egg", 3, "pcs", "cheese", 30, "g", "butter", 10, "g");

        recipe(db, "Tomato Pasta",
                "1. Boil the pasta in salted water until tender, then drain.\n"
                        + "2. Fry the chopped onion and garlic in olive oil until soft.\n"
                        + "3. Add the chopped tomatoes and simmer for 10 minutes.\n"
                        + "4. Toss the pasta through the sauce and serve.",
                "pasta", 200, "g", "tomato", 4, "pcs", "onion", 1, "pcs",
                "garlic", 2, "pcs", "olive oil", 2, "tbsp");

        recipe(db, "Garlic Butter Pasta",
                "1. Cook the pasta in boiling water, then drain.\n"
                        + "2. Melt the butter and gently fry the crushed garlic.\n"
                        + "3. Toss the pasta in the garlic butter.\n"
                        + "4. Top with grated cheese and serve.",
                "pasta", 200, "g", "butter", 30, "g", "garlic", 3, "pcs",
                "cheese", 20, "g");

        recipe(db, "Egg Fried Rice",
                "1. Cook the rice and let it cool.\n"
                        + "2. Fry the chopped onion and grated carrot in oil.\n"
                        + "3. Push to one side, scramble the eggs in the pan.\n"
                        + "4. Mix in the rice and stir-fry for 3 minutes.",
                "rice", 200, "g", "egg", 2, "pcs", "onion", 1, "pcs",
                "carrot", 1, "pcs", "cooking oil", 1, "tbsp");

        recipe(db, "Cheese Toastie",
                "1. Butter the outside of both bread slices.\n"
                        + "2. Place the cheese between the slices, butter side out.\n"
                        + "3. Fry in a pan for 2-3 minutes per side until golden and melted.",
                "bread", 2, "pcs", "cheese", 40, "g", "butter", 10, "g");

        recipe(db, "Creamy Mashed Potatoes",
                "1. Peel and chop the potatoes, then boil until soft.\n"
                        + "2. Drain well and return to the pot.\n"
                        + "3. Add the butter, milk and salt.\n"
                        + "4. Mash until smooth and creamy.",
                "potato", 4, "pcs", "butter", 30, "g", "milk", 50, "ml",
                "salt", 1, "tsp");

        recipe(db, "Garlic Roast Potatoes",
                "1. Heat the oven to 200 C.\n"
                        + "2. Chop the potatoes and toss with olive oil, salt and crushed garlic.\n"
                        + "3. Spread on a baking tray.\n"
                        + "4. Roast for 40 minutes, turning once, until crispy.",
                "potato", 500, "g", "olive oil", 3, "tbsp", "salt", 1, "tsp",
                "garlic", 2, "pcs");

        recipe(db, "Banana Pancakes",
                "1. Mash the bananas in a bowl.\n"
                        + "2. Mix in the eggs and flour until combined.\n"
                        + "3. Fry spoonfuls of batter in a lightly oiled pan.\n"
                        + "4. Flip when bubbles appear and cook until golden.",
                "banana", 2, "pcs", "egg", 2, "pcs", "flour", 50, "g");

        recipe(db, "Honey Porridge",
                "1. Put the oats and milk in a saucepan.\n"
                        + "2. Heat gently, stirring, for 5 minutes until thick.\n"
                        + "3. Pour into a bowl and drizzle with honey.",
                "oats", 80, "g", "milk", 300, "ml", "honey", 1, "tbsp");

        recipe(db, "Banana Oat Smoothie",
                "1. Peel the banana and break into pieces.\n"
                        + "2. Blend the banana, milk, oats and honey until smooth.\n"
                        + "3. Pour into a glass and drink straight away.",
                "banana", 1, "pcs", "milk", 250, "ml", "oats", 30, "g",
                "honey", 1, "tsp");

        recipe(db, "French Toast",
                "1. Whisk the eggs, milk and sugar in a shallow dish.\n"
                        + "2. Dip each slice of bread so both sides are soaked.\n"
                        + "3. Melt the butter in a pan.\n"
                        + "4. Fry the bread for 2 minutes per side until golden.",
                "bread", 4, "pcs", "egg", 2, "pcs", "milk", 100, "ml",
                "butter", 10, "g", "sugar", 1, "tbsp");

        recipe(db, "Tuna Mayo Sandwich",
                "1. Drain the tuna and mix with the mayonnaise.\n"
                        + "2. Spread the mixture on one slice of bread.\n"
                        + "3. Top with the second slice and cut in half.",
                "bread", 2, "pcs", "tuna", 1, "pcs", "mayonnaise", 1, "tbsp");

        recipe(db, "One-Pan Chicken and Rice",
                "1. Brown the diced chicken in oil in a large pan.\n"
                        + "2. Add the chopped onion and garlic and cook for 3 minutes.\n"
                        + "3. Stir in the rice and enough water to cover.\n"
                        + "4. Cover and simmer for 20 minutes until the rice is cooked.",
                "chicken", 300, "g", "rice", 200, "g", "onion", 1, "pcs",
                "garlic", 2, "pcs", "cooking oil", 1, "tbsp");

        recipe(db, "Simple Tomato Soup",
                "1. Fry the chopped onion and garlic in butter until soft.\n"
                        + "2. Add the chopped tomatoes and salt.\n"
                        + "3. Add a cup of water and simmer for 20 minutes.\n"
                        + "4. Blend until smooth and serve hot.",
                "tomato", 6, "pcs", "onion", 1, "pcs", "garlic", 2, "pcs",
                "butter", 20, "g", "salt", 1, "tsp");

        recipe(db, "Quick Vegetable Stir-Fry",
                "1. Slice the carrots, bell pepper and onion into thin strips.\n"
                        + "2. Heat the oil in a wok or large pan.\n"
                        + "3. Fry the garlic for 30 seconds, then add the vegetables.\n"
                        + "4. Stir-fry on high heat for 5 minutes.",
                "carrot", 2, "pcs", "bell pepper", 1, "pcs", "onion", 1, "pcs",
                "cooking oil", 1, "tbsp", "garlic", 2, "pcs");

        recipe(db, "Quick Egg and Cheese Pasta",
                "1. Cook the pasta and drain, keeping a little cooking water.\n"
                        + "2. Beat the eggs with the grated cheese and crushed garlic.\n"
                        + "3. Toss the hot pasta with the egg mixture off the heat.\n"
                        + "4. Add a splash of pasta water to make it creamy.",
                "pasta", 200, "g", "egg", 2, "pcs", "cheese", 50, "g",
                "garlic", 1, "pcs");

        recipe(db, "Honey Lemon Drink",
                "1. Squeeze the lemon into a mug.\n"
                        + "2. Add the honey.\n"
                        + "3. Top up with hot water and stir until dissolved.",
                "lemon", 1, "pcs", "honey", 1, "tbsp");

        recipe(db, "Beans on Toast",
                "1. Heat the baked beans in a small saucepan.\n"
                        + "2. Toast the bread and spread with butter.\n"
                        + "3. Spoon the hot beans over the toast.",
                "baked beans", 200, "g", "bread", 2, "pcs", "butter", 10, "g");
    }
}