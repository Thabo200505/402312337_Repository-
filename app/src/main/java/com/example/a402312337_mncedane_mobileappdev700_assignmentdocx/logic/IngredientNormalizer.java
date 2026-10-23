package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.logic;

import java.util.*;

public final class IngredientNormalizer {

    // words that end in "s" but are NOT plurals
    private static final Set<String> KEEP = new HashSet<>(Arrays.asList(
            "asparagus", "couscous", "hummus", "molasses", "watercress", "lemongrass"));

    // irregular plurals
    private static final Map<String, String> IRREGULAR = new HashMap<>();
    static {
        IRREGULAR.put("loaves", "loaf");
        IRREGULAR.put("leaves", "leaf");
        IRREGULAR.put("halves", "half");
        IRREGULAR.put("knives", "knife");
    }

    private IngredientNormalizer() {}

    /** "  Cherry Tomatoes! " -> "cherry tomato" */
    public static String normalize(String raw) {
        if (raw == null) return "";
        String s = raw.toLowerCase().replaceAll("[^a-z ]", " ").trim().replaceAll("\\s+", " ");
        if (s.isEmpty()) return "";
        String[] words = s.split(" ");
        // only the LAST word carries the plural ("cherry tomatoes")
        words[words.length - 1] = singular(words[words.length - 1]);
        return String.join(" ", words);
    }

    private static String singular(String w) {
        if (KEEP.contains(w) || w.length() <= 3) return w;
        if (IRREGULAR.containsKey(w)) return IRREGULAR.get(w);
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y"; // berries -> berry
        if (w.endsWith("oes")) return w.substring(0, w.length() - 2);        // tomatoes -> tomato
        if (w.endsWith("ches") || w.endsWith("shes") || w.endsWith("xes") || w.endsWith("sses"))
            return w.substring(0, w.length() - 2);                            // peaches -> peach
        if (w.endsWith("ss")) return w;                                       // watercress
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);           // eggs -> egg
        return w;
    }
}