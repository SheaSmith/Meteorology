package me.sheasmith.weatherstation;

import android.content.Context;

public class PreferencesHelper {
    public static boolean isSouthernHemisphere(Context context) {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE).getBoolean("isSouthernHemisphere", true);
    }
}
