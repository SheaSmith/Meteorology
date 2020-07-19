package me.sheasmith.weatherstation.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shea Smith on 19/07/2020.
 */
public class Location {
    public String address;
    public String adminDistrict;
    public String adminDistrictCode;
    public String city;
    public String country;
    public String countryCode;
    public String displayName;
    public String ianaTimeZone;
    public double latitude;
    public double longitude;
    public String neighbourhood;
    public String placeId;
    public String postalCode;
    public String postalKey;
    public boolean disputedArea;
    public String iataCode;
    public String icaoCode;
    public String locId;
    public String locationCategory;
    public String type;

    public Location(JSONObject json, int index) throws JSONException {
        address = json.getJSONArray("address").getString(index);
        adminDistrict = json.getJSONArray("adminDistrict").getString(index);
        adminDistrictCode = json.getJSONArray("adminDistrictCode").getString(index);
        city = json.getJSONArray("city").getString(index);
        country = json.getJSONArray("country").getString(index);
        countryCode = json.getJSONArray("countryCode").getString(index);
        displayName = json.getJSONArray("displayName").getString(index);
        ianaTimeZone = json.getJSONArray("ianaTimeZone").getString(index);
        latitude = json.getJSONArray("latitude").getDouble(index);
        longitude = json.getJSONArray("longitude").getDouble(index);
        neighbourhood = json.getJSONArray("neighborhood").getString(index);
        placeId = json.getJSONArray("placeId").getString(index);
        postalCode = json.getJSONArray("postalCode").getString(index);
        postalKey = json.getJSONArray("postalKey").getString(index);
        disputedArea = json.getJSONArray("disputedArea").getBoolean(index);
        iataCode = json.getJSONArray("iataCode").getString(index);
        icaoCode = json.getJSONArray("icaoCode").getString(index);
        locId = json.getJSONArray("locId").getString(index);
        locationCategory = json.getJSONArray("locationCategory").getString(index);
        type = json.getJSONArray("type").getString(index);
    }
}
