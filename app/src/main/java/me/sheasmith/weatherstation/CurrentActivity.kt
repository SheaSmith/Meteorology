package me.sheasmith.weatherstation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_current.*
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.CurrentConditions
import me.sheasmith.weatherstation.models.Forecast
import java.text.SimpleDateFormat

class CurrentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        ApiManager.getCurrentConditions(object : ApiResponse<CurrentConditions?> {
            override fun success(currentConditions: CurrentConditions) {
                ApiManager.getForecast(object : ApiResponse<List<Forecast?>> {
                    override fun success(forecasts: List<Forecast?>) {
                        val thisForecast = forecasts[0]
                        val code = if (thisForecast?.iconCodeDay != -1) thisForecast?.iconCodeDay else thisForecast.iconCodeNight

                        var backgroundGradient = -1
                        var stateText = ""
                        var stateIconInt = -1

                        when (code) {
                            0 -> {
                                backgroundGradient = R.drawable.background_wind
                                stateText = "Tornado"
                                stateIconInt = R.drawable.weather_tornado
                            }
                            1 -> {
                                backgroundGradient = R.drawable.background_stormy;
                                stateText = "Tropical Storm"
                                stateIconInt = R.drawable.weather_hurricane
                            }
                            2 -> {
                                backgroundGradient = R.drawable.background_stormy
                                stateText = "Hurricane"
                                stateIconInt = R.drawable.weather_hurricane
                            }
                            3 -> {
                                backgroundGradient = R.drawable.background_stormy
                                stateText = "Stormy"
                                stateIconInt = R.drawable.weather_lightning_rainy
                            }
                            4 -> {
                                backgroundGradient = R.drawable.background_stormy
                                stateText = "Thunderstorms"
                                stateIconInt = R.drawable.weather_lightning_rainy
                            }
                            5 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Raining & Snowing"
                                stateIconInt = R.drawable.weather_snowy_rainy
                            }
                            6 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Sleeting"
                                stateIconInt = R.drawable.weather_snowy_rainy
                            }
                            7 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Sleeting"
                                stateIconInt = R.drawable.weather_snowy_rainy
                            }
                            8 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Drizzling"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            9 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Drizzling"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            10 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Raining"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            11 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Showers"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            12 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Raining"
                                stateIconInt = R.drawable.weather_pouring
                            }
                            13 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_snowy
                            }
                            14 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_snowy_heavy
                            }
                            15 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_windy_variant
                            }
                            16 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_windy_variant
                            }
                            17 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Hailing"
                                stateIconInt = R.drawable.weather_hail
                            }
                            18 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Sleeting"
                                stateIconInt = R.drawable.weather_snowy_rainy
                            }
                            19 -> {
                                backgroundGradient = R.drawable.background_fog
                                stateText = "Dusty"
                                stateIconInt = R.drawable.weather_hazy
                            }
                            20 -> {
                                backgroundGradient = R.drawable.background_fog
                                stateText = "Foggy"
                                stateIconInt = R.drawable.weather_fog
                            }
                            21 -> {
                                backgroundGradient = R.drawable.background_fog
                                stateText = "Hazy"
                                stateIconInt = R.drawable.weather_hazy
                            }
                            22 -> {
                                backgroundGradient = R.drawable.background_fog
                                stateText = "Smokey"
                                stateIconInt = R.drawable.weather_hazy
                            }
                            23 -> {
                                backgroundGradient = R.drawable.background_wind
                                stateText = "Breezy"
                                stateIconInt = R.drawable.weather_windy
                            }
                            24 -> {
                                backgroundGradient = R.drawable.background_wind
                                stateText = "Windy"
                                stateIconInt = R.drawable.weather_windy
                            }
                            25 -> {
                                backgroundGradient = R.drawable.background_wind
                                stateText = "Windy"
                                stateIconInt = R.drawable.weather_windy_variant
                            }
                            26 -> {
                                backgroundGradient = R.drawable.background_cloudy
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_cloudy
                            }
                            27 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_night_partly_cloudy
                            }
                            28 -> {
                                backgroundGradient = R.drawable.background_cloudy
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_partly_cloudy
                            }
                            29 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_night_partly_cloudy
                            }
                            30 -> {
                                backgroundGradient = R.drawable.background_cloudy
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_partly_cloudy
                            }
                            31 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Clear"
                                stateIconInt = R.drawable.weather_night
                            }
                            32 -> {
                                backgroundGradient = R.drawable.background_sunny
                                stateText = "Sunny"
                                stateIconInt = R.drawable.weather_sunny
                            }
                            33 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_night_partly_cloudy
                            }
                            34 -> {
                                backgroundGradient = R.drawable.background_cloudy
                                stateText = "Cloudy"
                                stateIconInt = R.drawable.weather_partly_cloudy
                            }
                            35 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Hailing"
                                stateIconInt = R.drawable.weather_snowy_rainy
                            }
                            36 -> {
                                backgroundGradient = R.drawable.background_sunny
                                stateText = "Sunny"
                                stateIconInt = R.drawable.weather_sunny
                            }
                            37 -> {
                                backgroundGradient = R.drawable.background_stormy
                                stateText = "Thunderstorms"
                                stateIconInt = R.drawable.weather_partly_lightning
                            }
                            38 -> {
                                backgroundGradient = R.drawable.background_stormy
                                stateText = "Thunderstorms"
                                stateIconInt = R.drawable.weather_partly_lightning
                            }
                            39 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Showers"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            40 -> {
                                backgroundGradient = R.drawable.background_raining
                                stateText = "Raining"
                                stateIconInt = R.drawable.weather_pouring
                            }
                            41 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_partly_snowy
                            }
                            42 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_snowy_heavy
                            }
                            43 -> {
                                backgroundGradient = R.drawable.background_snow
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_snowy_heavy
                            }
                            44 -> {
                                backgroundGradient = R.drawable.background_sunny
                                stateText = "Unknown"
                                stateIconInt = R.drawable.weather_unknown
                            }
                            45 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Showers"
                                stateIconInt = R.drawable.weather_rainy
                            }
                            46 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Snowing"
                                stateIconInt = R.drawable.weather_snowy
                            }
                            47 -> {
                                backgroundGradient = R.drawable.background_night
                                stateText = "Thunderstorms"
                                stateIconInt = R.drawable.weather_lightning_rainy
                            }
                        }

                        runOnUiThread {
                            mainView.setBackgroundResource(backgroundGradient)
                            state.text = stateText
                            stateIcon.setImageResource(stateIconInt)

                            neighbourhood.text = currentConditions.neighbourhood
                            lastUpdated.text = SimpleDateFormat("EE, h:mm aa").format(currentConditions.observationTime)
                            temperature.text = String.format("%.0f°", currentConditions.temperature)
                            // TODO: Proper feels like temp
                            feelsLike.text = String.format("%.0f%s", currentConditions.heatIndex, UnitsHelper.getTemperatureUnit(currentConditions.unitSystem))
                            dewPoint.text = String.format("%.0f%s", currentConditions.dewPoint, UnitsHelper.getTemperatureUnit(currentConditions.unitSystem))
                            humidity.text = String.format("%.0f%%", currentConditions.humidity)
                            windSpeed.text = String.format("%.0f %s", currentConditions.windSpeed, UnitsHelper.getSpeedUnit(currentConditions.unitSystem))
                            windGust.text = String.format("%.0f %s", currentConditions.windGust, UnitsHelper.getSpeedUnit(currentConditions.unitSystem))
                            windDirection.text = String.format("%d° %s", currentConditions.windDirection, directionToCardinal(currentConditions.windDirection))
                            rainAccumulation.text = String.format("%.2f %s", currentConditions.precipitationTotal, UnitsHelper.getRainUnit(currentConditions.unitSystem))
                            rainRate.text = String.format("%.2f %s", currentConditions.precipitationRate, UnitsHelper.getRainRateUnit(currentConditions.unitSystem))
                            pressure.text = String.format("%.0f %s", currentConditions.pressure, UnitsHelper.getPressureUnit(currentConditions.unitSystem))
                            uvIndex.text = String.format("%.0f", currentConditions.uv)
                            solarRadiation.text = String.format("%.2f W/m²", currentConditions.uv)

                            scrollView.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                        }
                    }
                    override fun error(e: Exception) {
                        e.printStackTrace()
                    }
                }, this@CurrentActivity)
            }

            override fun error(e: Exception) {
                e.printStackTrace()
            }
        }, this)
    }

    fun directionToCardinal(x: Int): String? {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        return directions[(x % 360 / 45)]
    }
}