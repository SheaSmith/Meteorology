package me.sheasmith.weatherstation.helpers;

import android.content.Context;

public class PreferencesHelper {
    public static boolean isSouthernHemisphere(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getBoolean("isSouthernHemisphere", true);
    }

    public static void setSouthernHemisphere(Context context, boolean southernHemisphere) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putBoolean("isSouthernHemisphere", southernHemisphere).apply();
    }


    public static String getUnitsSystem(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("unitsSystem", "metric");
    }

    public static void setUnitsSystem(Context context, String unitsSystem) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putString("unitsSystem", unitsSystem).apply();
    }


    public static int getHistoryPeriodDefault(Context context) {
        return context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).getInt("historyPeriod", 1);
    }

    public static void setHistoryPeriodDefault(Context context, int period) {
        context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).edit().putInt("historyPeriod", period).apply();
    }
}
