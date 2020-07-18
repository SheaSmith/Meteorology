package me.sheasmith.weatherstation.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.loader
import kotlinx.android.synthetic.main.activity_history.mainView
import kotlinx.android.synthetic.main.activity_history.rootView
import kotlinx.android.synthetic.main.activity_history.scrollView
import me.sheasmith.weatherstation.ApiManager
import me.sheasmith.weatherstation.ui.charts.DateValueFormatter
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.FormatHelper
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.helpers.UnitsHelper
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.Observation
import me.sheasmith.weatherstation.ui.charts.HistoryMarker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

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

        val selectedPeriod = PreferencesHelper.getHistoryPeriodDefault(this)
        chipGroup.check(if (selectedPeriod == 0) R.id.rapid else if (selectedPeriod == 1) R.id.hourly else R.id.daily)

        when (selectedPeriod) {
            0 -> doRequestRapid()
            1 -> doRequestHourly()
            else -> doRequestDaily()
        }

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val number = if (checkedId == R.id.rapid) 0 else if (checkedId == R.id.hourly) 1 else 2
            PreferencesHelper.setHistoryPeriodDefault(this, number)
            when (number) {
                0 -> doRequestRapid()
                1 -> doRequestHourly()
                else -> doRequestDaily()
            }
        }

        back.setOnClickListener {
            finish()
        }

        when (intent.getStringExtra("type")) {
            "temperature" -> pageTitle.setText(R.string.temperature)
            "humidity" -> pageTitle.setText(R.string.humidity)
            "windSpeed" -> pageTitle.setText(R.string.wind_speed)
            "windGust" -> pageTitle.setText(R.string.wind_gust)
            "windDirection" -> pageTitle.setText(R.string.wind_direction)
            "rainAccumulation" -> pageTitle.setText(R.string.rain_accumulation)
            "rainRate" -> pageTitle.setText(R.string.rain_rate)
            "pressure" -> pageTitle.setText(R.string.pressure)
            "uvIndex" -> pageTitle.setText(R.string.uv_index)
            "solarRadiation" -> pageTitle.setText(R.string.solar_radiation)
        }
    }

    private fun doRequestHourly() {
        loader.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -31)
        val sevenDays = cal.time

        ApiManager.getHourlyHistory(object : ApiResponse<List<Observation>> {
            override fun success(value: List<Observation>) {
                processRequest(value, true)
            }

            override fun error(e: Exception?) {
                TODO("Not yet implemented")
            }
        }, sevenDays, Date(), this)
    }

    private fun doRequestDaily() {
        loader.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -31)
        val sevenDays = cal.time

        ApiManager.getDailyHistory(object : ApiResponse<List<Observation>> {
            override fun success(value: List<Observation>) {
                processRequest(value, true)
            }

            override fun error(e: Exception?) {
                TODO("Not yet implemented")
            }
        }, sevenDays, Date(), this)
    }

    private fun doRequestRapid() {
        loader.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        val totalObservations = ArrayList<List<Observation>>()

        for (i in -7..0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, i)
            val rapidTime = cal.time

            ApiManager.getRapidHistory(object : ApiResponse<List<Observation>> {
                override fun success(value: List<Observation>) {
                    totalObservations.add(value)

                    if (totalObservations.size == 8) {
                        val list = ArrayList<Observation>()
                        totalObservations.forEach {
                            list.addAll(it)
                        }

                        list.sortWith(compareBy { it.observationTime })
                        processRequest(list, false)
                    }
                }

                override fun error(e: Exception?) {
                    TODO("Not yet implemented")
                }
            }, rapidTime, this)
        }
    }

    fun processRequest(observations: List<Observation>, multipleInput: Boolean) {
        val type = intent.getStringExtra("type")

        val unitsSystem = PreferencesHelper.getUnitsSystem(this)
        var units: String? = ""
        var format = ""

        when (type) {
            "temperature" -> {
                units = UnitsHelper.getTemperatureUnit(unitsSystem)
                format = FormatHelper.getTemperatureFormat(true, true)
            }
            "humidity" -> {
                units = null
                format = FormatHelper.getHumidityFormat()
            }
            "windSpeed" -> {
                units = UnitsHelper.getSpeedUnit(unitsSystem)
                format = FormatHelper.getWindSpeedFormat()
            }
            "windGust" -> {
                units = UnitsHelper.getSpeedUnit(unitsSystem)
                format = FormatHelper.getWindSpeedFormat()
            }
            "windDirection" -> {
                units = null
                format = FormatHelper.getWindDirectionFormat()
            }
            "rainAccumulation" -> {
                units = UnitsHelper.getRainUnit(unitsSystem)
                format = FormatHelper.getRainFormat()
            }
            "rainRate" -> {
                units = UnitsHelper.getRainRateUnit(unitsSystem)
                format = FormatHelper.getRainFormat()
            }
            "pressure" -> {
                units = UnitsHelper.getPressureUnit(unitsSystem)
                format = FormatHelper.getPressureFormat()
            }
            "uvIndex" -> {
                units = null
                format = FormatHelper.getUvIndexFormat(false)
            }
            "solarRadiation" -> {
                units = null
                format = FormatHelper.getSolarRadiationFormat()
            }
        }

        var multiple = multipleInput
        val highEntries = ArrayList<Entry>()
        val averageEntries = ArrayList<Entry>()
        val lowEntries = ArrayList<Entry>()

        var min = 9000f
        val totalAverage: Float
        var max = -9000f

        observations.forEach {
            var average = 0f

            when (type) {
                "temperature" -> average = it.temperatureAverage.toFloat()
                "humidity" -> average = it.humidityAverage.toFloat()
                "windSpeed" -> average = it.windSpeedAverage.toFloat()
                "windGust" -> average = it.windGustAverage.toFloat()
                "windDirection" -> {
                    average = it.windDirection.toFloat()
                    multiple = false
                }
                "rainAccumulation" -> {
                    average = it.precipitationTotal.toFloat()
                    multiple = false
                }
                "rainRate" -> {
                    average = it.precipitationRate.toFloat()
                    multiple = false
                }
                "pressure" -> average = ((it.pressureMax + it.pressureMin) / 2).toFloat()
                "uvIndex" -> {
                    average = it.uv.toFloat()
                    multiple = false
                }
                "solarRadiation" -> {
                    average = it.solarRadiation.toFloat()
                    multiple = false
                }
            }

            averageEntries.add(Entry((it.observationTime.time / 1000).toFloat(), average))

            if (multiple) {
                var high = 0f
                var low = 0f

                when (type) {
                    "temperature" -> {
                        high = it.temperatureHigh.toFloat()
                        low = it.temperatureLow.toFloat()
                    }
                    "humidity" -> {
                        high = it.humidityHigh.toFloat()
                        low = it.humidityLow.toFloat()
                    }
                    "windSpeed" -> {
                        high = it.windSpeedHigh.toFloat()
                        low = it.windSpeedLow.toFloat()
                    }
                    "windGust" -> {
                        high = it.windGustHigh.toFloat()
                        low = it.windGustLow.toFloat()
                    }
                    "pressure" -> {
                        high = it.pressureMax.toFloat()
                        low = it.pressureMin.toFloat()
                    }
                }

                highEntries.add(Entry((it.observationTime.time / 1000).toFloat(), high))
                lowEntries.add(Entry((it.observationTime.time / 1000).toFloat(), low))
            }
        }

        val averageDataSet = LineDataSet(averageEntries, "Average")

        averageDataSet.color = ContextCompat.getColor(this, R.color.orange)
        averageDataSet.lineWidth = 2f
        averageDataSet.setDrawCircles(false)
        averageDataSet.setDrawValues(false)
        averageDataSet.highLightColor = ContextCompat.getColor(this, R.color.chartgrey)
        averageDataSet.enableDashedHighlightLine(5f, 10f, 15f)
        averageDataSet.highlightLineWidth = 2f
        averageDataSet.setDrawHorizontalHighlightIndicator(false)

        if (multiple) {
            val highDataSet = LineDataSet(highEntries, "High")
            val lowDataSet = LineDataSet(lowEntries, "Low")

            highDataSet.color = ContextCompat.getColor(this, R.color.red)
            highDataSet.lineWidth = 2f
            highDataSet.setDrawCircles(false)
            highDataSet.setDrawValues(false)
            highDataSet.highLightColor = ContextCompat.getColor(this, R.color.chartgrey)
            highDataSet.enableDashedHighlightLine(5f, 10f, 15f)
            highDataSet.highlightLineWidth = 2f
            highDataSet.setDrawHorizontalHighlightIndicator(false)

            lowDataSet.color = ContextCompat.getColor(this, R.color.blue)
            lowDataSet.lineWidth = 2f
            lowDataSet.setDrawCircles(false)
            lowDataSet.setDrawValues(false)
            lowDataSet.highLightColor = ContextCompat.getColor(this, R.color.chartgrey)
            lowDataSet.enableDashedHighlightLine(5f, 10f, 15f)
            lowDataSet.highlightLineWidth = 2f
            lowDataSet.setDrawHorizontalHighlightIndicator(false)

            highEntries.forEach {
                if (it.y > max)
                    max = it.y
            }

            lowEntries.forEach {
                if (it.y < min)
                    min = it.y
            }

            totalAverage = ((highEntries.sumByDouble { it.y.toDouble() } + lowEntries.sumByDouble { it.y.toDouble() } + averageEntries.sumByDouble { it.y.toDouble() }) / (highEntries.size + lowEntries.size + averageEntries.size)).toFloat()

            runOnUiThread {
                historyChart.data = LineData(highDataSet, averageDataSet, lowDataSet)
            }
        }
        else {
            averageEntries.forEach {
                if (it.y > max)
                    max = it.y

                if (it.y < min)
                    min = it.y
            }

            totalAverage = (averageEntries.sumByDouble { it.y.toDouble() } / averageEntries.size).toFloat()

            runOnUiThread {
                historyChart.data = LineData(averageDataSet)
            }
        }

        historyChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        historyChart.xAxis.valueFormatter = DateValueFormatter(SimpleDateFormat("MMM d, ha", Locale.ENGLISH))
        val description = Description()
        description.text = ""
        historyChart.description = description

        historyChart.xAxis.setDrawGridLines(false)
        historyChart.axisLeft.granularity = 5f
        historyChart.axisRight.isEnabled = false
        historyChart.isHighlightPerDragEnabled = true

        historyChart.xAxis.granularity = (6 * 60 * 60 * 1000).toFloat()

        historyChart.legend.isEnabled = false

        historyChart.xAxis.textSize = 12f
        historyChart.xAxis.textColor = ContextCompat.getColor(this, R.color.charttext)
        historyChart.axisLeft.textSize = 12f
        historyChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.charttext)
        historyChart.axisLeft.gridColor = ContextCompat.getColor(this, R.color.grey)
        historyChart.axisLeft.gridLineWidth = 2f
        historyChart.axisLeft.axisLineColor = ContextCompat.getColor(this, R.color.chartgrey)
        historyChart.axisLeft.axisLineWidth = 1.5f
        historyChart.xAxis.axisLineColor = ContextCompat.getColor(this, R.color.chartgrey)
        historyChart.xAxis.axisLineWidth = 1.5f

        historyChart.setDrawMarkers(false)
        historyChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onNothingSelected() {
                // Do nothing
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                historyChart.setDrawMarkers(true)
            }
        })

        val markerView = HistoryMarker(this, R.layout.history_marker, !multiple, units, format, highEntries, averageEntries, lowEntries)
        historyChart.marker = markerView

        runOnUiThread {
            val cal = Calendar.getInstance()
            cal.time = observations.first().observationTime
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val timeDifference = cal.time
            val difference = ((observations.first().observationTime.time - timeDifference.time) / 1000).toFloat()

            historyChart.setVisibleXRangeMaximum(difference)
            historyChart.moveViewToX((observations.last().observationTime.time / 1000).toFloat() - 8.64e4f)

            maximum.text = if (units != null) String.format(format, max, units) else String.format(format, max)
            minimum.text = if (units != null) String.format(format, min, units) else String.format(format, min)
            average.text = if (units != null) String.format(format, totalAverage, units) else String.format(format, totalAverage)

            loader.visibility = View.GONE
            scrollView.visibility = View.VISIBLE


            historyChart.animateXY(300, 300)
        }


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