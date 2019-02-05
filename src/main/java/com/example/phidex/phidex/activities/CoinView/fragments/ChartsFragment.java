package com.example.phidex.phidex.activities.CoinView.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.utils.CoinGraphUtil;
import com.example.phidex.phidex.utils.CustomGraphView;
import com.jjoe64.graphview.GridLabelRenderer;

public class ChartsFragment extends Fragment{

    private static String coinId;
    private CustomGraphView graph;
    private CoinGraphUtil coinGraphUtil;
    private View rootView;
    private AppDatabase ad;
    private Spinner spinner;
    private String coinCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        coinId = this.getArguments().getString("coinId");
        ad = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        coinCode = ad.coinDao().getCoinCode(coinId);
        rootView = inflater.inflate(R.layout.charts_fragment, container, false);
        graph = rootView.findViewById(R.id.chart);
        coinGraphUtil = new CoinGraphUtil(rootView, getActivity());
        createChart();

        // Initialise spinner and add styles
        spinner = rootView.findViewById(R.id.date_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.date_range_array, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // By default, the date range spinner is set to 7 days (the first value in the dropdown)
        // When the page is loaded for the first time onItemSelected will be automatically calle
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                int days = Integer.parseInt(getResources().getStringArray(R.array.date_range_array_values)[position]);
                coinGraphUtil.createGraph(days, graph, coinCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return rootView;
    }

    public void createChart() {
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
    }
}
