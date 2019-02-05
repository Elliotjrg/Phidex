package com.example.phidex.phidex.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CoinGraphUtil {
    private static final String BASE_URL = "https://min-api.cryptocompare.com/data/histoday";

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private CustomGraphView graph;
    private GridLabelRenderer glr;
    private int days;
    private Activity activity;

    private View view;

    public CoinGraphUtil(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }


    /**
     * Update graph tab with latest historical data
     */
    public void createGraph(int days, CustomGraphView graph, String coinCode) {
        this.graph = graph;
        this.days = days;

        glr = graph.getGridLabelRenderer();

        setupGraph();
        getGraphData(coinCode, days);
    }

    private void setupGraph() {
        glr = graph.getGridLabelRenderer();

        // Determining the X-axis bounds (the graph's first date value and the last date value)
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -days);
        Date firstDate = calendar.getTime();
        graph.getViewport().setMinX(firstDate.getTime());
        graph.getViewport().setMaxX(today.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // Setting label styles
        glr.setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        glr.setHorizontalLabelsAngle(70);
        glr.setPadding(32);
        glr.setVerticalLabelsColor(Color.WHITE);
        glr.setHorizontalLabelsColor(Color.WHITE);
    }


    /**
     * Given a time period and coinId, gets daily historical data
     */
    private void getGraphData(String coinCode, int days) {

        RequestParams params = new RequestParams();
        params.put("tsym", "AUD");
        params.put("fsym", coinCode);
        params.put("limit", days);
        params.put("aggregate", 1); // aggregate is the number of days between each data point returned

        try { //Grabbing the data from the API. This is done asynchronously outside of the main Android UI thread
            get("", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                    final JSONObject finalData = data;

                    //Editing graph needs to be done in the UI thread, thus we need this run()
                    graph.post(new Runnable(){
                        @Override
                        public void run(){
                            // generate the points along the graph
                            LineGraphSeries<DataPoint> series = createDataPointsFromJson(finalData);

                            // Allows you to click on a dataPoint and see the exact date/price
                            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                                @Override
                                public void onTap(Series series, DataPointInterface dataPoint) {
                                    Date date = new Date((long) dataPoint.getX());
                                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                                    Toast.makeText(activity, formattedDate+": $"+dataPoint.getY(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            graph.init();
                            setupGraph();
                            graph.addSeries(series);
                        }
                    });
                }
            });
        } catch (Exception e) {
            //API call failed
        }
    }

    /**
     * Turn raw data received from API into usable DataPoints
     */
    private LineGraphSeries<DataPoint> createDataPointsFromJson(JSONObject object) {
        try {
            JSONArray data = object.getJSONArray("Data");

            DataPoint[] dataPoints = new DataPoint[data.length()];
            for (int i = 0 ; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                Long unixTimestamp= obj.getLong("time");
                Date date = new Date(TimeUnit.MILLISECONDS.convert(unixTimestamp, TimeUnit.SECONDS));
                double close = obj.getDouble("close");
                Log.d("CoinGraphUtil", Double.toString(close));
                Log.d("CoinGraphUtil", date.toString());
                dataPoints[i] = new DataPoint(date, close);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            return series;

        } catch (JSONException e) {
            return null;
        }
    }
}
