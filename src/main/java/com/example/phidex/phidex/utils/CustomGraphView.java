package com.example.phidex.phidex.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

// This class exists due to some issues I encountered when loading data into the graph
// I got a workaround for it from here:
// https://github.com/jjoe64/GraphView/issues/515

public class CustomGraphView extends GraphView {
    public CustomGraphView(Context context) {
        super(context);
    }

    public CustomGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        super.init();
        getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this.getContext()));
    }
}