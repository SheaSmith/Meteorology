package me.sheasmith.weatherstation.helpers;

import android.content.Context;

import java.util.Objects;

import me.sheasmith.weatherstation.models.UnitsSystem;

public class PreferencesHelper {
    public static boolean isSouthernHemisphere(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getBoolean("isSouthernHemisphere", true);
    }

    public static void setSouthernHemisphere(Context context, boolean southernHemisphere) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putBoolean("isSouthernHemisphere", southernHemisphere).apply();
    }


    public static UnitsSystem getUnitsSystem(Context context) {
        return UnitsSystem.fromString(Objects.requireNonNull(context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("unitsSystem", "metric")));
    }

    public static void setUnitsSystem(Context context, UnitsSystem unitsSystem) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putString("unitsSystem", unitsSystem.toString()).apply();
    }


    public static int getHistoryPeriodDefault(Context context) {
        return context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).getInt("historyPeriod", 1);
    }

    public static void setHistoryPeriodDefault(Context context, int period) {
        context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).edit().putInt("historyPeriod", period).apply();
    }


    public static String getApiKey(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("apiKey", null);
    }

    public static void setApiKey(Context context, String apiKey) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putString("apiKey", apiKey).apply();
    }


    public static String getPws(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("pws", null);
    }

    public static void setPws(Context context, String pws) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putString("pws", pws).apply();
    }


    public static String getPwsLocation(Context context) {
        return context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).getString("pwsLocation", null);
    }

    public static void setPwsLocation(Context context, String pwsLocation) {
        context.getSharedPreferences("Defaults", Context.MODE_PRIVATE).edit().putString("pwsLocation", pwsLocation).apply();
    }
}
