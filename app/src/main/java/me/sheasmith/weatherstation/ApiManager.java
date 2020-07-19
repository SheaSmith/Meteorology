package me.sheasmith.weatherstation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.sheasmith.weatherstation.helpers.PreferencesHelper;
import me.sheasmith.weatherstation.models.ApiResponse;
import me.sheasmith.weatherstation.models.CurrentConditions;
import me.sheasmith.weatherstation.models.Forecast;
import me.sheasmith.weatherstation.models.Location;
import me.sheasmith.weatherstation.models.Observation;
import me.sheasmith.weatherstation.models.Station;
import me.sheasmith.weatherstation.models.UnauthorisedException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Shea Smith on 27/02/2019.
 */
public class ApiManager {

    public static void getCurrentConditions(final ApiResponse<CurrentConditions> callback, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v2/pws/observations/current?stationId=%s&format=json&units=%s&apiKey=%s&numericPrecision=decimal", PreferencesHelper.getPws(context), PreferencesHelper.getUnitsSystem(context).getApiCode(), PreferencesHelper.getApiKey(context)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                assert response.body() != null;
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json).getJSONArray("observations").getJSONObject(0);
                CurrentConditions currentConditions = new CurrentConditions(jsonObject);
                callback.success(currentConditions);

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void getHourlyHistory(final ApiResponse<List<Observation>> callback, Date start, Date end, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v2/pws/history/hourly?stationId=%s&format=json&units=%s&numericPrecision=decimal&apiKey=%s&startDate=%s&endDate=%s", PreferencesHelper.getPws(context), PreferencesHelper.getUnitsSystem(context).getApiCode(), PreferencesHelper.getApiKey(context), format.format(start), format.format(end)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                if (response.code() != 204) {
                    assert response.body() != null;
                    String json = response.body().string();
                    JSONArray jsonArray = new JSONObject(json).getJSONArray("observations");

                    List<Observation> observations = new ArrayList<>();
                    for (int i = 0; i != jsonArray.length(); i++) {
                        observations.add(new Observation(jsonArray.getJSONObject(i)));
                    }

                    callback.success(observations);
                }
                else {
                    callback.success(new ArrayList<>());
                }

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void getDailyHistory(final ApiResponse<List<Observation>> callback, Date start, Date end, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v2/pws/history/daily?stationId=%s&format=json&units=%s&numericPrecision=decimal&apiKey=%s&startDate=%s&endDate=%s", PreferencesHelper.getPws(context), PreferencesHelper.getUnitsSystem(context).getApiCode(), PreferencesHelper.getApiKey(context), format.format(start), format.format(end)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                if (response.code() != 204) {
                    assert response.body() != null;
                    String json = response.body().string();
                    JSONArray jsonArray = new JSONObject(json).getJSONArray("observations");

                    List<Observation> observations = new ArrayList<>();
                    for (int i = 0; i != jsonArray.length(); i++) {
                        observations.add(new Observation(jsonArray.getJSONObject(i)));
                    }

                    callback.success(observations);
                }
                else {
                    callback.success(new ArrayList<>());
                }

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void getRapidHistory(final ApiResponse<List<Observation>> callback, Date date, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v2/pws/history/all?stationId=%s&format=json&units=%s&numericPrecision=decimal&apiKey=%s&date=%s", PreferencesHelper.getPws(context), PreferencesHelper.getUnitsSystem(context).getApiCode(), PreferencesHelper.getApiKey(context), format.format(date)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                if (response.code() != 204) {
                    assert response.body() != null;
                    String json = response.body().string();
                    JSONArray jsonArray = new JSONObject(json).getJSONArray("observations");

                    List<Observation> observations = new ArrayList<>();
                    for (int i = 0; i != jsonArray.length(); i++) {
                        observations.add(new Observation(jsonArray.getJSONObject(i)));
                    }

                    callback.success(observations);
                }
                else {
                    callback.success(new ArrayList<>());
                }

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void getForecast(final ApiResponse<List<Forecast>> callback, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v3/wx/forecast/daily/5day?geocode=%s&units=%s&language=en-US&format=json&apiKey=%s", PreferencesHelper.getPwsLocation(context), PreferencesHelper.getUnitsSystem(context).getApiCode(), PreferencesHelper.getApiKey(context)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                assert response.body() != null;
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);

                List<Forecast> forecasts = new ArrayList<>();
                for (int i = 0; i != jsonObject.getJSONArray("dayOfWeek").length(); i++) {
                    forecasts.add(new Forecast(jsonObject, i));
                }
                callback.success(forecasts);

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void searchLocation(final ApiResponse<List<Location>> callback, String query, Context context) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v3/location/search?query=%s&apiKey=%s&format=json&language=en-US", query, PreferencesHelper.getApiKey(context)))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                assert response.body() != null;
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);

                List<Location> locations = new ArrayList<>();

                if (response.code() == 404) {
                    callback.success(locations);
                    return;
                }

                for (int i = 0; i != jsonObject.getJSONObject("location").getJSONArray("address").length(); i++) {
                    locations.add(new Location(jsonObject.getJSONObject("location"), i));
                }
                callback.success(locations);

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

    public static void findNear(final ApiResponse<List<Station>> callback, String geocode, Context context) {
        findNear(callback, geocode, context, PreferencesHelper.getApiKey(context));
    }

    public static void findNear(final ApiResponse<List<Station>> callback, String geocode, Context context, String apiKey) {
        Thread webThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(String.format("https://api.weather.com/v3/location/near?geocode=%s&apiKey=%s&product=pws&format=json", geocode, apiKey))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    callback.error(new UnauthorisedException());
                    return;
                }

                assert response.body() != null;
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);

                List<Station> stations = new ArrayList<>();

                if (response.code() == 404 || response.code() == 400) {
                    callback.success(stations);
                    return;
                }

                for (int i = 0; i != jsonObject.getJSONObject("location").getJSONArray("stationName").length(); i++) {
                    stations.add(new Station(jsonObject.getJSONObject("location"), i));
                }
                callback.success(stations);

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

}
