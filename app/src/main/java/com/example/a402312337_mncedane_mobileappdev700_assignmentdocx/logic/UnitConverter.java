package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic;

import java.util.*;

public final class UnitConverter {

    public enum Dimension { WEIGHT, VOLUME, COUNT, UNKNOWN }

    private static final Map<String, Object[]> UNITS = new HashMap<>();
    private static void put(Dimension d, double factor, String... names) {
        for (String n : names) UNITS.put(n, new Object[]{d, factor});
    }
    static {
        // weight -> grams
        put(Dimension.WEIGHT, 1,       "g", "gram", "grams");
        put(Dimension.WEIGHT, 1000,    "kg", "kilogram", "kilograms");
        put(Dimension.WEIGHT, 0.001,   "mg");
        put(Dimension.WEIGHT, 28.35,   "oz", "ounce", "ounces");
        put(Dimension.WEIGHT, 453.6,   "lb", "lbs", "pound", "pounds");
        // volume -> millilitres
        put(Dimension.VOLUME, 1,       "ml", "millilitre", "milliliter", "millilitres", "milliliters");
        put(Dimension.VOLUME, 10,      "cl");
        put(Dimension.VOLUME, 1000,    "l", "litre", "liter", "litres", "liters");
        put(Dimension.VOLUME, 5,       "tsp", "teaspoon", "teaspoons");
        put(Dimension.VOLUME, 15,      "tbsp", "tablespoon", "tablespoons");
        put(Dimension.VOLUME, 250,     "cup", "cups");
        // count -> pieces
        put(Dimension.COUNT, 1, "pcs", "pc", "piece", "pieces", "each", "unit", "units", "whole", "");
    }

    private UnitConverter() {}

    public static Dimension dimensionOf(String unit) {
        Object[] u = UNITS.get(clean(unit));
        return u == null ? Dimension.UNKNOWN : (Dimension) u[0];
    }

    /** Quantity expressed in the base unit (g, ml or pcs). */
    public static double toBase(double qty, String unit) {
        Object[] u = UNITS.get(clean(unit));
        return u == null ? qty : qty * (Double) u[1];
    }

    private static String clean(String unit) {
        return unit == null ? "" : unit.toLowerCase().replace(".", "").trim();
    }
}
