package me.sheasmith.weatherstation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.view_forecast.*
import me.sheasmith.weatherstation.models.Forecast
import java.text.SimpleDateFormat
import java.util.*


class ForecastFragment(forecast: Forecast, lastUpdatedDate: Date) : Fragment() {
    private var forecast: Forecast? = null
    private var lastUpdatedDate: Date? = null

    private var paddingTop = 0
    private var paddingBottom = 0

    private var isLight = false

    init {
        this.forecast = forecast
        this.lastUpdatedDate = lastUpdatedDate

        val iconCode = if (forecast.iconCodeDay != -1) forecast.iconCodeDay else forecast.iconCodeNight
        val backgroundResource = ForecastHelper.getBackground(iconCode)
        isLight = backgroundResource == R.drawable.background_snow || backgroundResource == R.drawable.background_cloudy;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        update()

        back.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun update() {
        val forecast = this.forecast!!

        val iconCode = if (forecast.iconCodeDay != -1) forecast.iconCodeDay else forecast.iconCodeNight
        val backgroundResource = ForecastHelper.getBackground(iconCode)
        mainView.setBackgroundResource(backgroundResource)

        isLight = backgroundResource == R.drawable.background_snow || backgroundResource == R.drawable.background_cloudy;
        if (!isLight) {
            lastUpdated.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            day.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            ImageViewCompat.setImageTintList(back, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
        } else {
            lastUpdated.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            day.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            ImageViewCompat.setImageTintList(back, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
        }

        if (forecast.iconCodeDay != -1) {
            dayState.setImageResource(ForecastHelper.getIcon(forecast.iconCodeDay))
            dayState.visibility = View.VISIBLE
        } else
            dayState.visibility = View.GONE

        nightState.setImageResource(ForecastHelper.getIcon(forecast.iconCodeNight))

        if (forecast.narrativeDay != null && forecast.narrativeDay != "null") {
            dayNarrative.text = forecast.narrativeDay
            dayNarrative.visibility = View.VISIBLE
            dayTitle.visibility = View.VISIBLE
        } else {
            dayNarrative.visibility = View.GONE
            dayTitle.visibility = View.GONE
        }

        nightNarrative.text = forecast.narrativeNight

        // TODO: change system
        if (forecast.temperatureMax != -9000)
            temperatureHigh.text = String.format("%d%s", forecast.temperatureMax, UnitsHelper.getTemperatureUnit("metric"))
        else
            temperatureHigh.text = "N/A"

        if (forecast.temperatureMin != -9000)
            temperatureLow.text = String.format("%d%s", forecast.temperatureMin, UnitsHelper.getTemperatureUnit("metric"))
        else
            temperatureLow.text = "N/A"

        if (forecast.precipitationChanceDay != -1) {
            val isSnow = forecast.precipitationTypeDay == "snow"

            chanceOfRainDay.text = String.format("%d%%", forecast.precipitationChanceDay)
            expectedAmountDay.text = String.format("%.2f %s", if (isSnow) forecast.expectedQuantityOfSnowDay else forecast.expectedQuantityOfRainDay, UnitsHelper.getRainUnit("metric"))
            cloudCoverDay.text = String.format("%d%%", forecast.cloudCoverDay)

            dayTitleRain.visibility = View.VISIBLE
            chanceOfRainTitleDay.visibility = View.VISIBLE
            chanceOfRainDay.visibility = View.VISIBLE
            expectedAmountTitleDay.visibility = View.VISIBLE
            expectedAmountDay.visibility = View.VISIBLE
            cloudCoverTitleDay.visibility = View.VISIBLE
            cloudCoverDay.visibility = View.VISIBLE
        } else {
            dayTitleRain.visibility = View.GONE
            chanceOfRainTitleDay.visibility = View.GONE
            chanceOfRainDay.visibility = View.GONE
            expectedAmountTitleDay.visibility = View.GONE
            expectedAmountDay.visibility = View.GONE
            cloudCoverTitleDay.visibility = View.GONE
            cloudCoverDay.visibility = View.GONE
        }

        val isSnow = forecast.precipitationTypeNight == "snow"

        chanceOfRainNight.text = String.format("%d%%", forecast.precipitationChanceNight)
        expectedAmountNight.text = String.format("%.2f %s", if (isSnow) forecast.expectedQuantityOfSnowNight else forecast.expectedQuantityOfRainNight, UnitsHelper.getRainUnit("metric"))
        cloudCoverNight.text = String.format("%d%%", forecast.cloudCoverNight)

        if (forecast.windSpeedDay != -1) {
            windSpeedDay.text = String.format("%d %s", forecast.windSpeedDay, UnitsHelper.getSpeedUnit("metric"))
            windDirectionDay.text = String.format("%d° %s", forecast.windDirectionDay, forecast.windDirectionCardinalDay)

            dayTitleWind.visibility = View.VISIBLE
            windSpeedTitleDay.visibility = View.VISIBLE
            windSpeedDay.visibility = View.VISIBLE
            windDirectionTitleDay.visibility = View.VISIBLE
            windDirectionDay.visibility = View.VISIBLE
        } else {
            dayTitleWind.visibility = View.GONE
            windSpeedTitleDay.visibility = View.GONE
            windSpeedDay.visibility = View.GONE
            windDirectionTitleDay.visibility = View.GONE
            windDirectionDay.visibility = View.GONE
        }

        windSpeedNight.text = String.format("%d %s", forecast.windSpeedNight, UnitsHelper.getSpeedUnit("metric"))
        windDirectionNight.text = String.format("%d° %s", forecast.windDirectionNight, forecast.windDirectionCardinalNight)

        if (forecast.temperatureHeatIndexDay != -1) {
            // TODO actual feels like temp
            feelsLikeDay.text = String.format("%d %s", forecast.temperatureHeatIndexDay, UnitsHelper.getTemperatureUnit("metric"))
            humidityDay.text = String.format("%.0f%%", forecast.relativeHumidityDay)
            uvIndexDay.text = String.format("%d - %s", forecast.uvDay, forecast.uvDescriptionDay)

            dayTitleTemperature.visibility = View.VISIBLE
            feelsLikeTitleDay.visibility = View.VISIBLE
            feelsLikeDay.visibility = View.VISIBLE
            humidityTitleDay.visibility = View.VISIBLE
            humidityDay.visibility = View.VISIBLE
            uvIndexTitleDay.visibility = View.VISIBLE
            uvIndexDay.visibility = View.VISIBLE
        } else {
            dayTitleTemperature.visibility = View.GONE
            feelsLikeTitleDay.visibility = View.GONE
            feelsLikeDay.visibility = View.GONE
            humidityTitleDay.visibility = View.GONE
            humidityDay.visibility = View.GONE
            uvIndexTitleDay.visibility = View.GONE
            uvIndexDay.visibility = View.GONE
        }

        // TODO actual feels like temp
        feelsLikeNight.text = String.format("%d %s", forecast.temperatureHeatIndexNight, UnitsHelper.getTemperatureUnit("metric"))
        humidityNight.text = String.format("%.0f%%", forecast.relativeHumidityNight)
        uvIndexNight.text = String.format("%d - %s", forecast.uvNight, forecast.uvDescriptionNight)

        moonStage.text = forecast.moonPhase

        var moonIcon = -1

        when (forecast.moonPhaseCode) {
            "WNG" -> moonIcon = R.drawable.moon_waning_gibbous
            "WXC" -> moonIcon = R.drawable.moon_waxing_crescent
            "FQ" -> moonIcon = R.drawable.moon_first_quarter
            "WNC" -> moonIcon = R.drawable.moon_waning_crescent
            "LQ" -> moonIcon = R.drawable.moon_last_quarter
            "F" -> moonIcon = R.drawable.moon_full
            "WXG" -> moonIcon = R.drawable.moon_waxing_gibbous
            "N" -> moonIcon = R.drawable.moon_new
        }

        moonStageIcon.setImageResource(moonIcon)

        if (PreferencesHelper.isSouthernHemisphere(requireContext()))
            moonStageIcon.rotation = 180f

        val timeFormat = SimpleDateFormat("h:mm aa", Locale.ENGLISH)
        moonrise.text = timeFormat.format(forecast.moonriseTime)
        moonset.text = timeFormat.format(forecast.moonsetTime)

        sunrise.text = timeFormat.format(forecast.sunriseTime)
        sunset.text = timeFormat.format(forecast.sunsetTime)

        phaseDay.text = forecast.moonPhaseDay.toString()

        day.text = if (forecast.nameDay == null || forecast.nameDay == "null") forecast.nameNight else forecast.nameDay
        lastUpdated.text = String.format("Updated %s", SimpleDateFormat("EE, h:mm aa", Locale.ENGLISH).format(lastUpdatedDate))
        mainView.setPadding(0, paddingTop, 0, paddingBottom)
    }

    fun setPadding(top: Int, bottom: Int) {
        paddingTop = top
        paddingBottom = bottom

        if (mainView != null)
            mainView.setPadding(0, top, 0, bottom)
    }

    fun isLight(): Boolean {
        return isLight;
    }
}