package me.sheasmith.weatherstation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.sheasmith.weatherstation.models.ApiResponse;
import me.sheasmith.weatherstation.models.CurrentConditions;
import me.sheasmith.weatherstation.models.Forecast;
import me.sheasmith.weatherstation.models.Observation;
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

            // TODO units
            Request request = new Request.Builder()
                    .url("https://api.weather.com/v2/pws/observations/current?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&apiKey=" + context.getString(R.string.api_key) + "&numericPrecision=decimal")
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();
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
                    .url("https://api.weather.com/v2/pws/history/hourly?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&numericPrecision=decimal&apiKey=" + context.getString(R.string.api_key) + "&startDate=" + format.format(start) + "&endDate=" + format.format(end))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();
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
                    .url("https://api.weather.com/v2/pws/history/daily?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&numericPrecision=decimal&apiKey=" + context.getString(R.string.api_key) + "&startDate=" + format.format(start) + "&endDate=" + format.format(end))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();
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
                    .url("https://api.weather.com/v2/pws/history/all?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&numericPrecision=decimal&apiKey=" + context.getString(R.string.api_key) + "&date=" + format.format(date))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();
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
                    .url("https://api.weather.com/v3/wx/forecast/daily/5day?geocode=" + context.getString(R.string.geocode) + "&units=m&language=en-US&format=json&apiKey=" + context.getString(R.string.api_key))
                    .get()
                    .build();

            try {

                Response response = client.newCall(request).execute();
                assert response.body() != null;
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);

                List<Forecast> forecasts = new ArrayList<>();
                for (int i = 0 ; i != jsonObject.getJSONArray("dayOfWeek").length() ; i++) {
                    forecasts.add(new Forecast(jsonObject, i));
                }
                callback.success(forecasts);

            } catch (Exception e) {
                callback.error(e);
            }
        });
        webThread.start();
    }

}
