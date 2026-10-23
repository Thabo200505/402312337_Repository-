package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static final String PREFS_NAME = "smartpantry_prefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_UNIT_SYSTEM = "unit_system";

    public static final String METRIC = "metric";
    public static final String IMPERIAL = "imperial";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isExpiryAlertsEnabled() {
        return prefs.getBoolean(KEY_EXPIRY_ALERTS, true);   // on by default
    }

    public void setExpiryAlertsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, enabled).apply();
    }

    public String getUnitSystem() {
        return prefs.getString(KEY_UNIT_SYSTEM, METRIC);
    }

    public void setUnitSystem(String system) {
        prefs.edit().putString(KEY_UNIT_SYSTEM, system).apply();
    }

    /** The unit pre-selected on the Add Ingredient form. */
    public String getDefaultUnit() {
        return IMPERIAL.equals(getUnitSystem()) ? "oz" : "g";
    }
}
