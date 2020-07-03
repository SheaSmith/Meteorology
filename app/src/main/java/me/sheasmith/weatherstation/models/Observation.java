package me.sheasmith.weatherstation.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Observation {
    public String stationId;
    public String timezone;
    public Date observationTime;
    public double latitude;
    public double longitude;
    public double solarRadiation;
    public double uv;
    public int windDirection;
    public double humidityHigh;
    public double humidityAverage;
    public double humidityLow;
    public int qcStatus;
    public double temperatureHigh;
    public double temperatureAverage;
    public double temperatureLow;
    public double windSpeedHigh;
    public double windSpeedAverage;
    public double windSpeedLow;
    public double windGustHigh;
    public double windGustAverage;
    public double windGustLow;
    public double dewPointHigh;
    public double dewPointAverage;
    public double dewPointLow;
    public double windChillHigh;
    public double windChillAverage;
    public double windChillLow;
    public double heatIndexHigh;
    public double heatIndexAverage;
    public double heatIndexLow;
    public double pressureMin;
    public double pressureMax;
    public double pressureTrend;
    public double precipitationRate;
    public double precipitationTotal;

    public String unitSystem;

    public Observation(JSONObject json) throws JSONException, ParseException {
        stationId = json.getString("stationID");
        timezone = json.getString("tz");
        observationTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(json.getString("obsTimeUtc"));
        latitude = json.getDouble("lat");
        longitude = json.getDouble("lon");
        solarRadiation = json.getDouble("solarRadiationHigh");
        uv = json.getDouble("uvHigh");
        windDirection = json.getInt("winddirAvg");
        humidityHigh = json.getDouble("humidityHigh");
        humidityAverage = json.getDouble("humidityAvg");
        humidityLow = json.getDouble("humidityLow");
        qcStatus = json.getInt("qcStatus");

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

        temperatureHigh = unitsContainer.getDouble("tempHigh");
        temperatureAverage = unitsContainer.getDouble("tempAvg");
        temperatureLow = unitsContainer.getDouble("tempLow");
        windSpeedHigh = unitsContainer.getDouble("windspeedHigh");
        windSpeedAverage = unitsContainer.getDouble("windspeedAvg");
        windSpeedLow = unitsContainer.getDouble("windspeedLow");
        windGustHigh = unitsContainer.getDouble("windgustHigh");
        windGustAverage = unitsContainer.getDouble("windgustAvg");
        windGustLow = unitsContainer.getDouble("windgustLow");
        dewPointHigh = unitsContainer.getDouble("dewptHigh");
        dewPointAverage = unitsContainer.getDouble("dewptAvg");
        dewPointLow = unitsContainer.getDouble("dewptLow");
        windChillHigh = unitsContainer.getDouble("windchillHigh");
        windChillAverage = unitsContainer.getDouble("windchillAvg");
        windChillLow = unitsContainer.getDouble("windchillLow");
        heatIndexHigh = unitsContainer.getDouble("heatindexHigh");
        heatIndexAverage = unitsContainer.getDouble("heatindexAvg");
        heatIndexLow = unitsContainer.getDouble("heatindexLow");
        pressureMax = unitsContainer.getDouble("pressureMax");
        pressureMin = unitsContainer.getDouble("pressureMin");
        pressureTrend = unitsContainer.getDouble("pressureTrend");
        precipitationRate = unitsContainer.getDouble("precipRate");
        precipitationTotal = unitsContainer.getDouble("precipTotal");
    }
}
