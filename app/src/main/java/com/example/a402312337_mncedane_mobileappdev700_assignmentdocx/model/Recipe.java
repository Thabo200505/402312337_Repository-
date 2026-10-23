package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients = new ArrayList<>();  // never null

    public Recipe() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = (ingredients == null) ? new ArrayList<>() : ingredients;
    }
}