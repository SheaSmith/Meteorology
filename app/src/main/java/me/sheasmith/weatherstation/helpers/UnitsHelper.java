package me.sheasmith.weatherstation.helpers;

/**
 * Created by Shea Smith on 20/06/2020.
 */
public class UnitsHelper {
    public static String getSpeedUnit(String system) {
        switch (system) {
            case "imperial":
                return "mph";
            case "metric_si":
                return "m/s";
            default:
                return "km/h";
        }
    }

    public static String getTemperatureUnit(String system) {
        if (system.equals("imperial")) {
            return "°F";
        }
        return "°C";
    }

    public static String getPressureUnit(String system) {
        if (system.equals("imperial"))
            return "in";
        return "hPa";
    }

    public static String getRainUnit(String system) {
        if (system.equals("imperial"))
            return "in";
        return "mm";
    }

    public static String getRainRateUnit(String system) {
        if (system.equals("imperial"))
            return "in/h";
        return "mm/h";
    }

    public static String getElevationUnit(String system) {
        if (system.equals("imperial") || system.equals("hybrid"))
            return "ft";
        return "m";
    }
}
