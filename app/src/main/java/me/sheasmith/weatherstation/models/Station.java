package me.sheasmith.weatherstation.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shea Smith on 19/07/2020.
 */
public class Station {
    public String stationName;
    public String stationId;
    public int qcStatus;
    public long updateTimeUtc;
    public String partnerId;
    public double latitude;
    public double longitude;
    public double distanceKm;
    public double distanceMi;

    public Station(JSONObject json, int index) throws JSONException {
        stationName = json.getJSONArray("stationName").getString(index);
        stationId = json.getJSONArray("stationId").getString(index);
        qcStatus = json.getJSONArray("qcStatus").getInt(index);
        updateTimeUtc = json.getJSONArray("updateTimeUtc").getLong(index);
        partnerId = json.getJSONArray("partnerId").getString(index);
        latitude = json.getJSONArray("latitude").getDouble(index);
        longitude = json.getJSONArray("longitude").getDouble(index);
        distanceKm = json.getJSONArray("distanceKm").getDouble(index);
        distanceMi = json.getJSONArray("distanceMi").getDouble(index);
    }
}
