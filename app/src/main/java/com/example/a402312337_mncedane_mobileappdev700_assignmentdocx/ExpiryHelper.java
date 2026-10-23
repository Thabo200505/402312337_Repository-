package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class ExpiryHelper {

    public static final int SOON_DAYS = 3;   // "expiring soon" = within 3 days

    private ExpiryHelper() {}

    /**
     * Days from today until the expiry date.
     * Negative = already expired, 0 = today. Returns null if there is no valid date.
     */
    public static Long daysUntil(String expiry) {
        if (expiry == null || !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) return null;
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            f.setLenient(false);
            Date date = f.parse(expiry);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            double diff = date.getTime() - today.getTimeInMillis();
            return Math.round(diff / 86400000.0);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isExpired(String expiry) {
        Long d = daysUntil(expiry);
        return d != null && d < 0;
    }

    public static boolean isExpiringSoon(String expiry) {
        Long d = daysUntil(expiry);
        return d != null && d >= 0 && d <= SOON_DAYS;
    }
}
