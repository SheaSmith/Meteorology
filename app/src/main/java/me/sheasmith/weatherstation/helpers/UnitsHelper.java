package me.sheasmith.weatherstation.helpers;

import me.sheasmith.weatherstation.models.UnitsSystem;

/**
 * Created by Shea Smith on 20/06/2020.
 */
public class UnitsHelper {
    public static String getSpeedUnit(UnitsSystem system) {
        switch (system) {
            case IMPERIAL:
                return "mph";
            case METRIC_SI:
                return "m/s";
            default:
                return "km/h";
        }
    }

    public static String getTemperatureUnit(UnitsSystem system) {
        if (system == UnitsSystem.IMPERIAL) {
            return "°F";
        }
        return "°C";
    }

    public static String getPressureUnit(UnitsSystem system) {
        if (system == UnitsSystem.IMPERIAL)
            return "in";
        return "hPa";
    }

    public static String getRainUnit(UnitsSystem system) {
        if (system == UnitsSystem.IMPERIAL)
            return "in";
        return "mm";
    }

    public static String getRainRateUnit(UnitsSystem system) {
        if (system == UnitsSystem.IMPERIAL)
            return "in/h";
        return "mm/h";
    }

    public static String getElevationUnit(UnitsSystem system) {
        if (system == UnitsSystem.IMPERIAL || system == UnitsSystem.UK_HYBRID)
            return "ft";
        return "m";
    }
}
