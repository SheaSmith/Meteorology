package me.sheasmith.weatherstation.ui.charts;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheDiamondPicks on 28/02/2019.
 */
public class DateValueFormatter extends ValueFormatter {

    private SimpleDateFormat format;

    public DateValueFormatter(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public String getFormattedValue(float value) {
        return format.format(new Date((long) (value) * 1000L));
    }
}
