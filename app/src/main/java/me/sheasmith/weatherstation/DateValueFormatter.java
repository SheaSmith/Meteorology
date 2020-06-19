package me.sheasmith.weatherstation;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheDiamondPicks on 28/02/2019.
 */
public class DateValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat format;

    public DateValueFormatter(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return format.format(new Date((long) (value * 1000L)));
    }
}
