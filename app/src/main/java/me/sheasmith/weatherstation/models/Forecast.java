package me.sheasmith.weatherstation.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Shea Smith on 19/06/2020.
 */
public class Forecast {
    public String dayOfWeek;
    public Date expirationTime;
    public String moonPhase;
    public String moonPhaseCode;
    public int moonPhaseDay;
    public Date moonriseTime;
    public Date moonsetTime;
    public String narrativeOverall;
    public double expectedQuantityOfRainOverall;
    public double expectedQuantityOfSnowOverall;
    public Date sunriseTime;
    public Date sunsetTime;
    public int temperatureMax;
    public int temperatureMin;
    public Date validTime;

    public int cloudCoverDay;
    public int cloudCoverNight;
    public String nameDay;
    public String nameNight;
    public int iconCodeDay;
    public int iconCodeNight;
    public int iconCodeExtendedDay;
    public int iconCodeExtendedNight;
    public String narrativeDay;
    public String narrativeNight;
    public int precipitationChanceDay;
    public int precipitationChanceNight;
    public String precipitationTypeDay;
    public String precipitationTypeNight;
    public double expectedQuantityOfRainDay;
    public double expectedQuantityOfRainNight;
    public double expectedQuantityOfSnowDay;
    public double expectedQuantityOfSnowNight;
    public String qualifierCodeDay;
    public String qualifierCodeNight;
    public String qualifierPhraseDay;
    public String qualifierPhraseNight;
    public double relativeHumidityDay;
    public double relativeHumidityNight;
    public String snowRangeDay;
    public String snowRangeNight;
    public int temperatureDay;
    public int temperatureNight;
    public int temperatureHeatIndexDay;
    public int temperatureHeatIndexNight;
    public int temperatureWindChillDay;
    public int temperatureWindChillNight;
    public String thunderCategoryDay;
    public String thunderCategoryNight;
    public int thunderIndexDay;
    public int thunderIndexNight;
    public String uvDescriptionDay;
    public String uvDescriptionNight;
    public int uvDay;
    public int uvNight;
    public int windDirectionDay;
    public int windDirectionNight;
    public String windDirectionCardinalDay;
    public String windDirectionCardinalNight;
    public String windPhraseDay;
    public String windPhraseNight;
    public int windSpeedDay;
    public int windSpeedNight;
    public String weatherPhraseLongDay;
    public String weatherPhraseLongNight;
    public String weatherPhraseShortDay;
    public String weatherPhraseShortNight;

    public Forecast(JSONObject allForecasts, int index) throws JSONException {
        this.dayOfWeek = allForecasts.getJSONArray("dayOfWeek").getString(index);
        this.expirationTime = new Date(allForecasts.getJSONArray("expirationTimeUtc").getLong(index) * 1000);
        this.moonPhase = allForecasts.getJSONArray("moonPhase").getString(index);
        this.moonPhaseCode = allForecasts.getJSONArray("moonPhaseCode").getString(index);
        this.moonPhaseDay = allForecasts.getJSONArray("moonPhaseDay").getInt(index);
        this.moonriseTime = new Date(allForecasts.getJSONArray("moonriseTimeUtc").optLong(index, 0) * 1000);
        this.moonsetTime = new Date(allForecasts.getJSONArray("moonsetTimeUtc").optLong(index, 0) * 1000);
        this.narrativeOverall = allForecasts.getJSONArray("narrative").getString(index);
        this.expectedQuantityOfRainOverall = allForecasts.getJSONArray("qpf").getDouble(index);
        this.expectedQuantityOfSnowOverall = allForecasts.getJSONArray("qpfSnow").getDouble(index);
        this.sunriseTime = new Date(allForecasts.getJSONArray("sunriseTimeUtc").getLong(index) * 1000);
        this.sunsetTime = new Date(allForecasts.getJSONArray("sunsetTimeUtc").getLong(index) * 1000);
        this.temperatureMax = allForecasts.getJSONArray("temperatureMax").optInt(index, -9000);
        this.temperatureMin = allForecasts.getJSONArray("temperatureMin").optInt(index, -9000);
        this.validTime = new Date(allForecasts.getJSONArray("validTimeUtc").getLong(index) * 1000);

        JSONObject dayParts = allForecasts.getJSONArray("daypart").getJSONObject(0);

        int dayIndex = index * 2;

        this.cloudCoverDay = dayParts.getJSONArray("cloudCover").optInt(dayIndex, -1);
        this.cloudCoverNight = dayParts.getJSONArray("cloudCover").optInt(dayIndex + 1, -1);
        this.nameDay = dayParts.getJSONArray("daypartName").optString(dayIndex, null);
        this.nameNight = dayParts.getJSONArray("daypartName").optString(dayIndex + 1, null);
        this.iconCodeDay = dayParts.getJSONArray("iconCode").optInt(dayIndex, -1);
        this.iconCodeNight = dayParts.getJSONArray("iconCode").optInt(dayIndex + 1, -1);
        this.iconCodeExtendedDay = dayParts.getJSONArray("iconCodeExtend").optInt(dayIndex, -1);
        this.iconCodeExtendedNight = dayParts.getJSONArray("iconCodeExtend").optInt(dayIndex + 1, -1);
        this.narrativeDay = dayParts.getJSONArray("narrative").optString(dayIndex, null);
        this.narrativeNight = dayParts.getJSONArray("narrative").optString(dayIndex + 1, null);
        this.precipitationChanceDay = dayParts.getJSONArray("precipChance").optInt(dayIndex, -1);
        this.precipitationChanceNight = dayParts.getJSONArray("precipChance").optInt(dayIndex + 1, -1);
        this.precipitationTypeDay = dayParts.getJSONArray("precipType").optString(dayIndex, null);
        this.precipitationTypeNight = dayParts.getJSONArray("precipType").optString(dayIndex + 1, null);
        this.expectedQuantityOfRainDay = dayParts.getJSONArray("qpf").optDouble(dayIndex, -1);
        this.expectedQuantityOfRainNight = dayParts.getJSONArray("qpf").optDouble(dayIndex + 1, -1);
        this.expectedQuantityOfSnowDay = dayParts.getJSONArray("qpfSnow").optDouble(dayIndex, -1);
        this.expectedQuantityOfSnowNight = dayParts.getJSONArray("qpfSnow").optDouble(dayIndex + 1, -1);
        this.qualifierCodeDay = dayParts.getJSONArray("qualifierCode").optString(dayIndex, null);
        this.qualifierCodeNight = dayParts.getJSONArray("qualifierCode").optString(dayIndex + 1, null);
        this.qualifierPhraseDay = dayParts.getJSONArray("qualifierPhrase").optString(dayIndex, null);
        this.qualifierPhraseNight = dayParts.getJSONArray("qualifierPhrase").optString(dayIndex + 1, null);
        this.relativeHumidityDay = dayParts.getJSONArray("relativeHumidity").optInt(dayIndex, -1);
        this.relativeHumidityNight = dayParts.getJSONArray("relativeHumidity").optInt(dayIndex + 1, -1);
        this.snowRangeDay = dayParts.getJSONArray("snowRange").optString(dayIndex, null);
        this.snowRangeNight = dayParts.getJSONArray("snowRange").optString(dayIndex + 1, null);
        this.temperatureDay = dayParts.getJSONArray("temperature").optInt(dayIndex, -1);
        this.temperatureNight = dayParts.getJSONArray("temperature").optInt(dayIndex + 1, -1);
        this.temperatureHeatIndexDay = dayParts.getJSONArray("temperatureHeatIndex").optInt(dayIndex, -1);
        this.temperatureHeatIndexNight = dayParts.getJSONArray("temperatureHeatIndex").optInt(dayIndex + 1, -1);
        this.temperatureWindChillDay = dayParts.getJSONArray("temperatureWindChill").optInt(dayIndex, -1);
        this.temperatureWindChillNight = dayParts.getJSONArray("temperatureWindChill").optInt(dayIndex + 1, -1);
        this.thunderCategoryDay = dayParts.getJSONArray("thunderCategory").optString(dayIndex, null);
        this.thunderCategoryNight = dayParts.getJSONArray("thunderCategory").optString(dayIndex + 1, null);
        this.thunderIndexDay = dayParts.getJSONArray("thunderIndex").optInt(dayIndex, -1);
        this.thunderIndexNight = dayParts.getJSONArray("thunderIndex").optInt(dayIndex + 1, -1);
        this.uvDescriptionDay = dayParts.getJSONArray("uvDescription").optString(dayIndex, null);
        this.uvDescriptionNight = dayParts.getJSONArray("uvDescription").optString(dayIndex + 1, null);
        this.uvDay = dayParts.getJSONArray("uvIndex").optInt(dayIndex, -1);
        this.uvNight = dayParts.getJSONArray("uvIndex").optInt(dayIndex + 1, -1);
        this.windDirectionDay = dayParts.getJSONArray("windDirection").optInt(dayIndex, -1);
        this.windDirectionNight = dayParts.getJSONArray("windDirection").optInt(dayIndex + 1, -1);
        this.windDirectionCardinalDay = dayParts.getJSONArray("windDirectionCardinal").optString(dayIndex, null);
        this.windDirectionCardinalNight = dayParts.getJSONArray("windDirectionCardinal").optString(dayIndex + 1, null);
        this.windPhraseDay = dayParts.getJSONArray("windPhrase").optString(dayIndex, null);
        this.windPhraseNight = dayParts.getJSONArray("windPhrase").optString(dayIndex + 1, null);
        this.windSpeedDay = dayParts.getJSONArray("windSpeed").optInt(dayIndex, -1);
        this.windSpeedNight = dayParts.getJSONArray("windSpeed").optInt(dayIndex + 1, -1);
        this.weatherPhraseLongDay = dayParts.getJSONArray("wxPhraseLong").optString(dayIndex, null);
        this.weatherPhraseLongNight = dayParts.getJSONArray("wxPhraseLong").optString(dayIndex + 1, null);
        this.weatherPhraseShortDay = dayParts.getJSONArray("wxPhraseShort").optString(dayIndex, null);
        this.weatherPhraseShortNight = dayParts.getJSONArray("wxPhraseShort").optString(dayIndex + 1, null);
    }
}
