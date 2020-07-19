package me.sheasmith.weatherstation.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.sheasmith.weatherstation.models.UnitsSystem;

/**
 * Created by Shea Smith on 18/07/2020.
 */
public class FormatHelper {
    public static String formatLongDate(Date date) {
        return getLongDateFormat().format(date);
    }

    public static SimpleDateFormat getLongDateFormat() {
        return new SimpleDateFormat("EE, h:mm aa", Locale.ENGLISH);
    }


    public static String formatLongerDate(Date date) {
        return getLongerDateFormat().format(date);
    }

    public static SimpleDateFormat getLongerDateFormat() {
        return new SimpleDateFormat("d MMM, h:mm aa", Locale.ENGLISH);
    }



    public static String formatTemperature(double temperature, boolean decimals) {
        return String.format(Locale.ENGLISH, getTemperatureFormat(false, decimals), temperature);
    }

    public static String formatTemperature(double temperature, UnitsSystem unitsSystem, boolean decimals) {
        return String.format(Locale.ENGLISH, getTemperatureFormat(true, decimals), temperature, UnitsHelper.getTemperatureUnit(unitsSystem));
    }

    public static String getTemperatureFormat(boolean units, boolean decimals) {
        return (decimals ? "%.1f" : "%.0f") + (units ? "%s" : "°");
    }



    public static String formatHumidity(double humidity) {
        return String.format(Locale.ENGLISH, getHumidityFormat(), humidity);
    }

    public static String getHumidityFormat() {
        return "%.0f%%";
    }


    public static String formatWindSpeed(double windSpeed, UnitsSystem unitsSystem) {
        return String.format(Locale.ENGLISH, getWindSpeedFormat(), windSpeed, UnitsHelper.getSpeedUnit(unitsSystem));
    }

    public static String getWindSpeedFormat() {
        return "%.0f %s";
    }


    public static String formatWindDirection(float windDirection) {
        return String.format(Locale.ENGLISH, getWindDirectionFormat(), windDirection, degreesToCardinal(windDirection));
    }

    public static String formatWindDirection(float windDirection, String windDirectionCardinal) {
        return String.format(Locale.ENGLISH, getWindDirectionFormat(), windDirection, windDirectionCardinal);
    }

    public static String getWindDirectionFormat() {
        return "%.0f° %s";
    }

    public static String degreesToCardinal(float degrees) {
        String[] directions = new String[]{"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};
        return directions[(int) Math.floor(((degrees + 11.25) % 360) / 22.5)];
    }


    public static String formatRain(double rain, UnitsSystem unitsSystem) {
        return String.format(Locale.ENGLISH, getRainFormat(), rain, UnitsHelper.getRainUnit(unitsSystem));
    }

    public static String getRainFormat() {
        return "%.2f %s";
    }

    public static String formatRainRate(double rainRate, UnitsSystem unitsSystem) {
        return String.format(getRainFormat(), rainRate, UnitsHelper.getRainRateUnit(unitsSystem));
    }


    public static String formatPressure(double pressure, UnitsSystem unitsSystem) {
        return String.format(getPressureFormat(), pressure, UnitsHelper.getPressureUnit(unitsSystem));
    }

    public static String getPressureFormat() {
        return "%.0f %s";
    }



    public static String formatUvIndex(double uvIndex) {
        return String.format(getUvIndexFormat(false), uvIndex);
    }

    public static String formatUvIndex(double uvIndex, String description) {
        return String.format(getUvIndexFormat(true), uvIndex, description);
    }

    public static String getUvIndexFormat(boolean hasDescription) {
        return hasDescription ? "%.0f - %s" : "%.0f";
    }



    public static String formatSolarRadiation(double solarRadiation) {
        return String.format(Locale.ENGLISH, getSolarRadiationFormat(), solarRadiation);
    }

    public static String getSolarRadiationFormat() {
        return "%.2f W/m²";
    }



    public static String formatPercentage(int chance) {
        return String.format(Locale.ENGLISH, getPercentageFormat(), chance);
    }

    public static String getPercentageFormat() {
        return "%d%%";
    }


    public static String formatTime(Date time) {
        return getTimeFormat().format(time);
    }

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
    }


    public static String formatElevation(double elevation, UnitsSystem unitsSystem) {
        return String.format(getElevationFormat(), elevation, UnitsHelper.getElevationUnit(unitsSystem));
    }

    public static String getElevationFormat() {
        return "%.1f %s";
    }
}
