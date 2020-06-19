package me.sheasmith.weatherstation.models;

import java.util.Date;

/**
 * Created by Shea Smith on 19/06/2020.
 */
public class History {
    public String stationId;
    public String timezone;
    public Date observationDate;
    public double latitude;
    public double longitude;
    public double solarRadiation;
    public double uv;
    public double windDirection;
    public double humidityHigh;
    public double humidityAverage;
    public double humidityLow;
}
