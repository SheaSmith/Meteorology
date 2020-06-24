package me.sheasmith.weatherstation;

import java.util.Date;

public class ForecastHelper {
    public static String getName(int iconCode) {
        switch (iconCode) {
            case 0: {
                return "Tornado";
            }
            case 1: {
                return "Tropical Storm";
            }
            case 2: {
                return "Hurricane";
            }
            case 3: {
                return "Stormy";
            }
            case 4: {
                return "Thunderstorms";
            }
            case 5: {
                return "Raining & Snowing";
            }
            case 6: {
                return "Sleeting";
            }
            case 7: {
                return "Sleeting";
            }
            case 8: {
                return "Drizzling";
            }
            case 9: {
                return "Drizzling";
            }
            case 10: {
                return "Raining";
            }
            case 11: {
                return "Showers";
            }
            case 12: {
                return "Raining";
            }
            case 13: {
                return "Snowing";
            }
            case 14: {
                return "Snowing";
            }
            case 15: {
                return "Snowing";
            }
            case 16: {
                return "Snowing";
            }
            case 17: {
                return "Hailing";
            }
            case 18: {
                return "Sleeting";
            }
            case 19: {
                return "Dusty";
            }
            case 20: {
                return "Foggy";
            }
            case 21: {
                return "Hazy";
            }
            case 22: {
                return "Smokey";
            }
            case 23: {
                return "Breezy";
            }
            case 24: {
                return "Windy";
            }
            case 25: {
                return "Windy";
            }
            case 26: {
                return "Cloudy";
            }
            case 27: {
                return "Cloudy";
            }
            case 28: {
                return "Cloudy";
            }
            case 29: {
                return "Cloudy";
            }
            case 30: {
                return "Cloudy";
            }
            case 31: {
                return "Clear";
            }
            case 32: {
                return "Sunny";
            }
            case 33: {
                return "Cloudy";
            }
            case 34: {
                return "Cloudy";
            }
            case 35: {
                return "Hailing";
            }
            case 36: {
                return "Sunny";
            }
            case 37: {
                return "Thunderstorms";
            }
            case 38: {
                return "Thunderstorms";
            }
            case 39: {
                return "Showers";
            }
            case 40: {
                return "Raining";
            }
            case 41: {
                return "Snowing";
            }
            case 42: {
                return "Snowing";
            }
            case 43: {
                return "Snowing";
            }
            case 45: {
                return "Showers";
            }
            case 46: {
                return "Snowing";
            }
            case 47: {
                return "Thunderstorms";
            }
            default: {
                return "Unknown";
            }
        }
    }

    public static int getBackground(int iconCode, Date sunset, Date sunrise) {
        boolean isNight = sunset.getTime() < System.currentTimeMillis() || sunrise.getTime() > System.currentTimeMillis();
        if (isNight)
            return R.drawable.background_night;

        switch (iconCode) {
            case 0:
            case 23:
            case 24:
            case 25: {
                return R.drawable.background_wind;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 37:
            case 38:
            case 47: {
                return R.drawable.background_stormy;
            }
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 35:
            case 41:
            case 42:
            case 43:
            case 46: {
                return R.drawable.background_snow;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 39:
            case 40:
            case 45: {
                return R.drawable.background_raining;
            }
            case 19:
            case 20:
            case 21:
            case 22: {
                return R.drawable.background_fog;
            }
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 33:
            case 34: {
                return R.drawable.background_cloudy;
            }
            default: {
                return R.drawable.background_sunny;
            }
        }
    }

    public static int getBackground(int iconCode) {
        switch (iconCode) {
            case 0:
            case 23:
            case 24:
            case 25: {
                return R.drawable.background_wind;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 37:
            case 38: {
                return R.drawable.background_stormy;
            }
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 35:
            case 41:
            case 42:
            case 43: {
                return R.drawable.background_snow;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 39:
            case 40: {
                return R.drawable.background_raining;
            }
            case 19:
            case 20:
            case 21:
            case 22: {
                return R.drawable.background_fog;
            }
            case 26:
            case 28:
            case 30:
            case 34: {
                return R.drawable.background_cloudy;
            }
            case 27:
            case 29:
            case 31:
            case 33:
            case 45:
            case 46:
            case 47: {
                return R.drawable.background_night;
            }
            default: {
                return R.drawable.background_sunny;
            }
        }
    }

    public static int getIcon(int iconCode) {
        switch (iconCode) {
            case 0: {
                return R.drawable.weather_tornado;
            }
            case 1:
            case 2: {
                return R.drawable.weather_hurricane;
            }
            case 3:
            case 4:
            case 47: {
                return R.drawable.weather_lightning_rainy;
            }
            case 5:
            case 6:
            case 7:
            case 18:
            case 35: {
                return R.drawable.weather_snowy_rainy;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 39:
            case 45: {
                return R.drawable.weather_rainy;
            }
            case 12:
            case 40: {
                return R.drawable.weather_pouring;
            }
            case 13:
            case 46: {
                return R.drawable.weather_snowy;
            }
            case 14:
            case 42:
            case 43: {
                return R.drawable.weather_snowy_heavy;
            }
            case 15:
            case 16:
            case 25: {
                return R.drawable.weather_windy_variant;
            }
            case 17: {
                return R.drawable.weather_hail;
            }
            case 19:
            case 21:
            case 22: {
                return R.drawable.weather_hazy;
            }
            case 20: {
                return R.drawable.weather_fog;
            }
            case 23:
            case 24: {
                return R.drawable.weather_windy;
            }
            case 26: {
                return R.drawable.weather_cloudy;
            }
            case 27:
            case 29:
            case 33: {
                return R.drawable.weather_night_partly_cloudy;
            }
            case 28:
            case 30:
            case 34: {
                return R.drawable.weather_partly_cloudy;
            }
            case 31: {
                return R.drawable.weather_night;
            }
            case 32:
            case 36: {
                return R.drawable.weather_sunny;
            }
            case 37:
            case 38: {
                return R.drawable.weather_partly_lightning;
            }
            case 41: {
                return R.drawable.weather_partly_snowy;
            }
            default: {
                return R.drawable.weather_unknown;
            }
        }
    }

    public static int getIcon(int iconCode, Date sunset, Date sunrise) {
        boolean isNight = sunset.getTime() < System.currentTimeMillis() || sunrise.getTime() > System.currentTimeMillis();

        switch (iconCode) {
            case 0: {
                return R.drawable.weather_tornado;
            }
            case 1:
            case 2: {
                return R.drawable.weather_hurricane;
            }
            case 3:
            case 4:
            case 47: {
                return R.drawable.weather_lightning_rainy;
            }
            case 5:
            case 6:
            case 7:
            case 18:
            case 35: {
                return R.drawable.weather_snowy_rainy;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 39:
            case 45: {
                return R.drawable.weather_rainy;
            }
            case 12:
            case 40: {
                return R.drawable.weather_pouring;
            }
            case 13:
            case 46: {
                return R.drawable.weather_snowy;
            }
            case 14:
            case 42:
            case 43: {
                return R.drawable.weather_snowy_heavy;
            }
            case 15:
            case 16:
            case 25: {
                return R.drawable.weather_windy_variant;
            }
            case 17: {
                return R.drawable.weather_hail;
            }
            case 19:
            case 21:
            case 22: {
                return R.drawable.weather_hazy;
            }
            case 20: {
                return R.drawable.weather_fog;
            }
            case 23:
            case 24: {
                return R.drawable.weather_windy;
            }
            case 26: {
                return R.drawable.weather_cloudy;
            }
            case 27:
            case 29:
            case 33:
            case 28:
            case 30:
            case 34: {
                return isNight ? R.drawable.weather_night_partly_cloudy : R.drawable.weather_partly_cloudy;
            }
            case 31:
            case 32:
            case 36: {
                return isNight ? R.drawable.weather_night : R.drawable.weather_sunny;
            }
            case 37:
            case 38: {
                return isNight ? R.drawable.weather_lightning_rainy : R.drawable.weather_partly_lightning;
            }
            case 41: {
                return isNight ? R.drawable.weather_snowy : R.drawable.weather_partly_snowy;
            }
            default: {
                return R.drawable.weather_unknown;
            }
        }
    }
}
