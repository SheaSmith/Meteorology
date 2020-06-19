package me.sheasmith.weatherstation.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shea Smith on 19/06/2020.
 */
public class CurrentConditions {
    public String stationId;
    public Date observationTime;
    public String neighbourhood;
    public String softwareType;
    public String country;
    public double solarRadiation;
    public double longitude;
    public double realtimeFrequency;
    public double latitude;
    public double uv;
    public int windDirection;
    public double humidity;
    public int qcStatus;
    public double temperature;
    public double heatIndex;
    public double dewPoint;
    public double windChill;
    public double windSpeed;
    public double windGust;
    public double pressure;
    public double precipitationRate;
    public double precipitationTotal;
    public double elevation;

    public CurrentConditions(JSONObject json) throws JSONException {
        this.stationId = json.getString("stationID");
        try {
            this.observationTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(json.getString("obsTimeUtc"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.neighbourhood = json.getString("neighborhood");
        this.softwareType = json.getString("softwareType");
        this.country = json.getString("country");
        this.solarRadiation = json.getDouble("solarRadiation");
        this.longitude = json.getDouble("lon");
        this.realtimeFrequency = json.optDouble("realtimeFrequency", -1);
        this.latitude = json.getDouble("lat");
        this.uv = json.getDouble("uv");
        this.windDirection = json.getInt("winddir");
        this.humidity = json.getDouble("humidity");
        this.qcStatus = json.getInt("qcStatus");

        JSONObject unitsContainer;

        if (json.has("metric")) {
            unitsContainer = json.getJSONObject("metric");
            unitSystem = "metric";
        } else if (json.has("imperial")) {
            unitsContainer = json.getJSONObject("imperial");
            unitSystem = "imperial";
        } else if (json.has("metric_si")) {
            unitsContainer = json.getJSONObject("metric_si");
            unitSystem = "metric_si";
        } else if (json.has("uk_hybrid")) {
            unitsContainer = json.getJSONObject("uk_hybrid");
            unitSystem = "uk_hybrid";
        } else {
            unitsContainer = new JSONObject();
            unitSystem = null;
        }

        this.temperature = unitsContainer.getDouble("temp");
        this.heatIndex = unitsContainer.getDouble("heatIndex");
        this.dewPoint = unitsContainer.getDouble("dewpt");
        this.windChill = unitsContainer.getDouble("windChill");
        this.windSpeed = unitsContainer.getDouble("windSpeed");
        this.windGust = unitsContainer.getDouble("windGust");
        this.pressure = unitsContainer.getDouble("pressure");
        this.precipitationRate = unitsContainer.getDouble("precipRate");
        this.precipitationTotal = unitsContainer.getDouble("precipTotal");
        this.elevation = unitsContainer.getDouble("elev");
    }

    public String unitSystem;
}
