package me.sheasmith.weatherstation;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.sheasmith.weatherstation.models.ApiResponse;
import me.sheasmith.weatherstation.models.CurrentConditions;
import me.sheasmith.weatherstation.models.Forecast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Shea Smith on 27/02/2019.
 */
public class ApiManager {

    public static void getCurrentConditions(final ApiResponse<CurrentConditions> callback, Context context) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                // TODO units
                Request request = new Request.Builder()
                        .url("https://api.weather.com/v2/pws/observations/current?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&apiKey=" + context.getString(R.string.api_key) + "&numericPrecision=decimal")
                        .get()
                        .build();

                try {

                    Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json).getJSONArray("observations").getJSONObject(0);
                    CurrentConditions currentConditions = new CurrentConditions(jsonObject);
                    callback.success(currentConditions);

                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

//    public static void getHourlyHistory(final ApiResponse<HourlyHistory> callback, Context context) {
//        Thread webThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//
//                Request request = new Request.Builder()
//                        .url("https://api.weather.com/v2/pws/observations/hourly/7day?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&apiKey=" + context.getString(R.string.api_key))
//                        .get()
//                        .build();
//
//                try {
//
//                    Response response = client.newCall(request).execute();
//                    String json = response.body().string();
//                    Gson gson = new Gson();
//                    HourlyHistory hourlyHistory = gson.fromJson(json, HourlyHistory.class);
//                    callback.success(hourlyHistory);
//
//                } catch (Exception e) {
//                    callback.error(e);
//                }
//            }
//        });
//        webThread.start();
//    }
//
//    public static void getDailyHistory(final ApiResponse<DailyHistory> callback, Context context) {
//        Thread webThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//
//                Request request = new Request.Builder()
//                        .url("https://api.weather.com/v2/pws/dailysummary/7day?stationId=" + context.getString(R.string.station_id) + "&format=json&units=m&apiKey=" + context.getString(R.string.api_key))
//                        .get()
//                        .build();
//
//                try {
//
//                    Response response = client.newCall(request).execute();
//                    String json = response.body().string();
//                    Gson gson = new Gson();
//                    DailyHistory dailyHistory = gson.fromJson(json, DailyHistory.class);
//                    callback.success(dailyHistory);
//
//                } catch (Exception e) {
//                    callback.error(e);
//                }
//            }
//        });
//        webThread.start();
//    }

    public static void getForecast(final ApiResponse<List<Forecast>> callback, Context context) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://api.weather.com/v3/wx/forecast/daily/5day?geocode=" + context.getString(R.string.geocode) + "&units=m&language=en-US&format=json&apiKey=" + context.getString(R.string.api_key))
                        .get()
                        .build();

                try {

                    Response response = client.newCall(request).execute();
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
            }
        });
        webThread.start();
    }

}
