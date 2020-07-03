package me.sheasmith.weatherstation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_history.*
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.Observation
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

        doRequestHourly()
    }

    private fun doRequestHourly() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -31)
        val sevenDays = cal.time

        ApiManager.getHourlyHistory(object : ApiResponse<List<Observation>> {
            override fun success(value: List<Observation>) {
                processRequestMultiple(value)
            }

            override fun error(e: Exception?) {
                TODO("Not yet implemented")
            }
        }, sevenDays, Date(), this)
    }

    fun processRequestMultiple(observations: List<Observation>) {
        runOnUiThread {
            loader.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }

        val temperatureHighEntries = ArrayList<Entry>()
        val temperatureAverageEntries = ArrayList<Entry>()
        val temperatureLowEntries = ArrayList<Entry>()

        observations.forEach {
            temperatureHighEntries.add(Entry(it.observationTime.time.toFloat(), it.temperatureHigh.toFloat()))
            temperatureAverageEntries.add(Entry(it.observationTime.time.toFloat(), it.temperatureAverage.toFloat()))
            temperatureLowEntries.add(Entry(it.observationTime.time.toFloat(), it.temperatureLow.toFloat()))
        }

        val temperatureHighDataSet = LineDataSet(temperatureHighEntries, "High")
        val temperatureAverageDataSet = LineDataSet(temperatureAverageEntries, "Average")
        val temperatureLowDataSet = LineDataSet(temperatureLowEntries, "Low")

        temperatureHighDataSet.color = ContextCompat.getColor(this, R.color.red)
        temperatureHighDataSet.setDrawCircles(false)
        temperatureHighDataSet.setDrawValues(false)
        temperatureHighDataSet.highLightColor = ContextCompat.getColor(this, R.color.black)
        temperatureHighDataSet.setDrawHorizontalHighlightIndicator(false)

        temperatureAverageDataSet.color = ContextCompat.getColor(this, R.color.orange)
        temperatureAverageDataSet.setDrawCircles(false)
        temperatureAverageDataSet.setDrawValues(false)
        temperatureAverageDataSet.highLightColor = ContextCompat.getColor(this, R.color.black)
        temperatureAverageDataSet.setDrawHorizontalHighlightIndicator(false)

        temperatureLowDataSet.color = ContextCompat.getColor(this, R.color.blue)
        temperatureLowDataSet.setDrawCircles(false)
        temperatureLowDataSet.setDrawValues(false)
        temperatureLowDataSet.highLightColor = ContextCompat.getColor(this, R.color.black)
        temperatureLowDataSet.setDrawHorizontalHighlightIndicator(false)

        temperatureChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        temperatureChart.xAxis.valueFormatter = DateValueFormatter(SimpleDateFormat("d/M ha", Locale.ENGLISH))
        val description = Description()
        description.text = ""
        temperatureChart.description = description

        temperatureChart.xAxis.setDrawGridLines(false)
        temperatureChart.axisLeft.granularity = 5f
        temperatureChart.axisRight.isEnabled = false
        temperatureChart.isHighlightPerDragEnabled = true

        runOnUiThread {
            temperatureChart.data = LineData(temperatureHighDataSet, temperatureAverageDataSet, temperatureLowDataSet)

            val cal = Calendar.getInstance()
            cal.time = observations.first().observationTime
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val timeDifference = cal.time
            val difference = (observations.first().observationTime.time - timeDifference.time).toFloat();

            temperatureChart.setVisibleXRangeMaximum(difference)
            temperatureChart.moveViewToX(observations.last().observationTime.time.toFloat() - 8.64e7f)

            temperatureChart.invalidate()
        }



    }
}