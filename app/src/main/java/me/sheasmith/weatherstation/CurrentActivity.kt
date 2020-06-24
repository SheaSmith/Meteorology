package me.sheasmith.weatherstation

import android.animation.LayoutTransition
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.activity_current.*
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.CurrentConditions
import me.sheasmith.weatherstation.models.Forecast
import java.text.SimpleDateFormat
import java.util.*


class CurrentActivity : AppCompatActivity() {
    private var flags = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current)

        flags = neighbourhood.systemUiVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000 // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        doRequest()

        val layoutTransition: LayoutTransition = swipeRefresh.layoutTransition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }

        swipeRefresh.setOnRefreshListener {
            doRequest()
        }

        forecast.setOnClickListener {
            startActivity(Intent(this@CurrentActivity, ForecastActivity::class.java))
        }
    }

    fun directionToCardinal(x: Int): String? {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        return directions[(x % 360 / 45)]
    }

    private fun doRequest() {
        ApiManager.getCurrentConditions(object : ApiResponse<CurrentConditions?> {
            override fun success(currentConditions: CurrentConditions) {
                ApiManager.getForecast(object : ApiResponse<List<Forecast?>> {
                    override fun success(forecasts: List<Forecast?>) {
                        val thisForecast = forecasts[0]
                        val code = if (thisForecast?.iconCodeDay != -1) thisForecast?.iconCodeDay else thisForecast.iconCodeNight

                        runOnUiThread {
                            val backgroundGradient = ForecastHelper.getBackground(code!!, thisForecast?.sunsetTime, thisForecast?.sunriseTime)
                            mainView.setBackgroundResource(backgroundGradient)
                            state.text = ForecastHelper.getName(code)
                            stateIcon.setImageResource(ForecastHelper.getIcon(code, thisForecast?.sunsetTime, thisForecast?.sunriseTime))

                            neighbourhood.text = currentConditions.neighbourhood
                            lastUpdated.text = SimpleDateFormat("EE, h:mm aa", Locale.ENGLISH).format(currentConditions.observationTime)
                            temperature.text = String.format("%.0f°", currentConditions.temperature)
                            // TODO: Proper feels like temp
                            feelsLike.text = String.format("%.0f%s", currentConditions.heatIndex, UnitsHelper.getTemperatureUnit(currentConditions.unitSystem))
                            dewPoint.text = String.format("%.0f%s", currentConditions.dewPoint, UnitsHelper.getTemperatureUnit(currentConditions.unitSystem))
                            temperatureHigh.text = String.format("%.0f%%", currentConditions.humidity)
                            windSpeed.text = String.format("%.0f %s", currentConditions.windSpeed, UnitsHelper.getSpeedUnit(currentConditions.unitSystem))
                            windGust.text = String.format("%.0f %s", currentConditions.windGust, UnitsHelper.getSpeedUnit(currentConditions.unitSystem))
                            windDirection.text = String.format("%d° %s", currentConditions.windDirection, directionToCardinal(currentConditions.windDirection))
                            rainAccumulation.text = String.format("%.2f %s", currentConditions.precipitationTotal, UnitsHelper.getRainUnit(currentConditions.unitSystem))
                            rainRate.text = String.format("%.2f %s", currentConditions.precipitationRate, UnitsHelper.getRainRateUnit(currentConditions.unitSystem))
                            pressure.text = String.format("%.0f %s", currentConditions.pressure, UnitsHelper.getPressureUnit(currentConditions.unitSystem))
                            uvIndex.text = String.format("%.0f", currentConditions.uv)
                            solarRadiation.text = String.format("%.2f W/m²", currentConditions.uv)

                            setColor(backgroundGradient != R.drawable.background_snow && backgroundGradient != R.drawable.background_cloudy)

                            scrollView.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                            swipeRefresh.isRefreshing = false
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

    private fun setColor(white: Boolean) {
        val text = arrayOf(neighbourhood, lastUpdated, state, temperature, feelsLikeTitle, feelsLike, dewPointTitle, dewPoint, humidityTitle, temperatureHigh, windSpeedTitle, windSpeed, windGustTitle, windGust, windDirectionTitle, windDirection, rainAccumulationTitle, rainAccumulation, rainRateTitle, rainRate, pressureTitle, pressure, uvIndexTitle, uvIndex, solarRadiationTitle, solarRadiation)
        val images = arrayOf(stateIcon, humidityIcon, windSpeedIcon, windGustIcon, windDirectionIcon, rainAccumulationIcon, rainRateIcon, pressureIcon, uvIndexIcon, solarRadiationIcon, settings)
        val modules = arrayOf(humidityModule, windSpeedModule, windGustModule, windDirectionModule, rainAccumulationModule, rainRateModule, pressureModule, uvIndexModule, solarRadiationModule)

        text.forEach {
            it.setTextColor(if (white) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this, R.color.black))
        }

        images.forEach {
            ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(if (white) ContextCompat.getColor(this, R.color.white) else ContextCompat.getColor(this, R.color.black)))
        }

        modules.forEach {
            it.setBackgroundResource(if (white) R.drawable.module_current_light else R.drawable.module_current_dark)
        }

        if (white)
            neighbourhood.systemUiVisibility = flags
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            neighbourhood.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            if (insets.systemWindowInsetTop != 0)
                mainView.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
            insets
        }
    }
}