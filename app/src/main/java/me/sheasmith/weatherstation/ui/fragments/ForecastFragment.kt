package me.sheasmith.weatherstation.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_forecast.*
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.ForecastHelper
import me.sheasmith.weatherstation.helpers.FormatHelper
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.models.Forecast
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
        isLight = backgroundResource == R.drawable.background_snow || backgroundResource == R.drawable.background_cloudy
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
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

        isLight = backgroundResource == R.drawable.background_snow || backgroundResource == R.drawable.background_cloudy
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

        val unitsSystem = PreferencesHelper.getUnitsSystem(requireContext())
        
        if (forecast.temperatureMax != -9000)
            temperatureHigh.text = FormatHelper.formatTemperature(forecast.temperatureMax.toDouble(), unitsSystem, false)
        else
            temperatureHigh.text = "N/A"

        if (forecast.temperatureMin != -9000)
            temperatureLow.text = FormatHelper.formatTemperature(forecast.temperatureMin.toDouble(), unitsSystem, false)
        else
            temperatureLow.text = "N/A"

        if (forecast.precipitationChanceDay != -1) {
            val isSnow = forecast.precipitationTypeDay == "snow"

            chanceOfRainDay.text = FormatHelper.formatPercentage(forecast.precipitationChanceDay)
            expectedAmountDay.text = FormatHelper.formatRain(if (isSnow) forecast.expectedQuantityOfSnowDay else forecast.expectedQuantityOfRainDay, unitsSystem)
            cloudCoverDay.text = FormatHelper.formatPercentage(forecast.cloudCoverDay)

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

        chanceOfRainNight.text = FormatHelper.formatPercentage(forecast.precipitationChanceNight)
        expectedAmountNight.text = FormatHelper.formatRain(if (isSnow) forecast.expectedQuantityOfSnowNight else forecast.expectedQuantityOfRainNight, unitsSystem)
        cloudCoverNight.text = FormatHelper.formatPercentage(forecast.cloudCoverNight)

        if (forecast.windSpeedDay != -1) {
            windSpeedDay.text = FormatHelper.formatWindSpeed(forecast.windSpeedDay.toDouble(), unitsSystem)
            windDirectionDay.text = FormatHelper.formatWindDirection(forecast.windDirectionDay.toFloat(), forecast.windDirectionCardinalDay)

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

        windSpeedNight.text = FormatHelper.formatWindSpeed(forecast.windSpeedNight.toDouble(), unitsSystem)
        windDirectionNight.text = FormatHelper.formatWindDirection(forecast.windDirectionNight.toFloat(), forecast.windDirectionCardinalNight)

        if (forecast.temperatureHeatIndexDay != -1) {
            val feelsLikeDayValue = if (forecast.temperatureHeatIndexDay > forecast.temperatureDay) forecast.temperatureHeatIndexDay else if (forecast.temperatureWindChillDay < forecast.temperatureDay) forecast.temperatureWindChillDay else forecast.temperatureDay

            feelsLikeDay.text = FormatHelper.formatTemperature(feelsLikeDayValue.toDouble(), unitsSystem, false)
            humidityDay.text = FormatHelper.formatHumidity(forecast.relativeHumidityDay)
            uvIndexDay.text = FormatHelper.formatUvIndex(forecast.uvDay.toDouble(), forecast.uvDescriptionDay)

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

        val feelsLikeNightValue = if (forecast.temperatureHeatIndexNight > forecast.temperatureNight) forecast.temperatureHeatIndexNight else if (forecast.temperatureWindChillNight < forecast.temperatureNight) forecast.temperatureWindChillNight else forecast.temperatureNight

        feelsLikeNight.text = FormatHelper.formatTemperature(feelsLikeNightValue.toDouble(), unitsSystem, false)
        humidityNight.text = FormatHelper.formatHumidity(forecast.relativeHumidityNight)
        uvIndexNight.text = FormatHelper.formatUvIndex(forecast.uvNight.toDouble(), forecast.uvDescriptionNight)

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

        moonrise.text = FormatHelper.formatTime(forecast.moonriseTime)
        moonset.text = FormatHelper.formatTime(forecast.moonsetTime)

        sunrise.text = FormatHelper.formatTime(forecast.sunriseTime)
        sunset.text = FormatHelper.formatTime(forecast.sunsetTime)

        phaseDay.text = forecast.moonPhaseDay.toString()

        day.text = if (forecast.nameDay == null || forecast.nameDay == "null") forecast.nameNight else forecast.nameDay
        lastUpdated.text = String.format("Updated %s", FormatHelper.formatLongDate(lastUpdatedDate))
        mainView.setPadding(0, paddingTop, 0, paddingBottom)
    }

    fun setPadding(top: Int, bottom: Int) {
        paddingTop = top
        paddingBottom = bottom

        if (mainView != null)
            mainView.setPadding(0, top, 0, bottom)
    }

    fun isLight(): Boolean {
        return isLight
    }
}