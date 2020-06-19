package me.sheasmith.weatherstation;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TheDiamondPicks on 28/02/2019.
 */
public class CustomChartMarker extends MarkerView {

    private TextView tvContent;

    private boolean round;
    private String suffix;
    private int[] colors;
    private Context context;
    private final SimpleDateFormat timeformat;

    public CustomChartMarker(Context context, int layoutResource, boolean round, String suffix, SimpleDateFormat timeformat, int... colors) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.color);

        this.round = round;
        this.suffix = suffix;
        this.colors = colors;
        this.context = context;
        this.timeformat = timeformat;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        String content = "";
        boolean showSuffix = true;

        if (context.getResources().getColor(android.R.color.black) == colors[highlight.getDataSetIndex()]) {
            content += timeformat.format(new Date((long) (e.getX() * 1000L)));
            showSuffix = false;
        }
        else if (round)
            content += (int) e.getY();
        else
            content += e.getY();

        if (showSuffix)
            content += suffix;

        if (e.getData() != null && e.getData() instanceof String)
            content += " " + e.getData();

        int color = colors[highlight.getDataSetIndex()];

        tvContent.setText(content);
        tvContent.setTextColor(color);

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
