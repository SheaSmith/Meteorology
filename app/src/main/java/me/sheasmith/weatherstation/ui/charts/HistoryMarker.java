package me.sheasmith.weatherstation.ui.charts;

import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.sheasmith.weatherstation.R;
import me.sheasmith.weatherstation.helpers.FormatHelper;

/**
 * Created by Shea Smith on 18/07/2020.
 */
public class HistoryMarker extends MarkerView {

    private TextView high;
    private TextView low;
    private TextView average;
    private TextView dateTime;
    private ConstraintLayout container;

    private List<Entry> highData, lowData, averageData;

    private String units;
    private String format;

    boolean onlyAverage;

    public HistoryMarker(Context context, int layoutResource, boolean onlyAverage, String units, String format, List<Entry> highData, List<Entry> lowData, List<Entry> averageData) {
        super(context, layoutResource);

        high = findViewById(R.id.maximum);
        low = findViewById(R.id.minimum);
        average = findViewById(R.id.average);
        dateTime = findViewById(R.id.dateTime);
        container = findViewById(R.id.container);

        TextView highTitle = findViewById(R.id.maximumTitle);
        TextView lowTitle = findViewById(R.id.minimumTitle);

        int visibility = onlyAverage ? GONE : VISIBLE;
        highTitle.setVisibility(visibility);
        high.setVisibility(visibility);
        lowTitle.setVisibility(visibility);
        low.setVisibility(visibility);

        this.highData = highData;
        this.averageData = averageData;
        this.lowData = lowData;

        this.units = units;
        this.format = format;

        this.onlyAverage = onlyAverage;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (!onlyAverage && lowData.contains(e) && averageData.contains(e)) {
            container.setVisibility(GONE);
        }
        else {
            container.setVisibility(VISIBLE);

            if (!onlyAverage) {
                Entry highEntry = null;

                for (Entry item : highData) {
                    if (item.getX() == e.getX()) {
                        highEntry = item;
                        break;
                    }
                }

                assert highEntry != null;
                high.setText(format(highEntry.getY()));

                Entry lowEntry = null;

                for (Entry item : lowData) {
                    if (item.getX() == e.getX()) {
                        lowEntry = item;
                        break;
                    }
                }

                assert lowEntry != null;
                low.setText(format(lowEntry.getY()));

                Entry averageEntry = null;

                for (Entry item : averageData) {
                    if (item.getX() == e.getX()) {
                        averageEntry = item;
                        break;
                    }
                }

                assert averageEntry != null;
                average.setText(format(averageEntry.getY()));
            }
            else {
                average.setText(format(e.getY()));
            }

            dateTime.setText(FormatHelper.formatLongerDate(new Date((long) e.getX() * 1000L)));
        }

        super.refreshContent(e, highlight);
    }

    private String format(Object value) {
        if (units != null)
            return String.format(Locale.ENGLISH, format, value, units);
        else
            return String.format(Locale.ENGLISH, format, value);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2f), -getHeight());
        }

        return mOffset;
    }
}
