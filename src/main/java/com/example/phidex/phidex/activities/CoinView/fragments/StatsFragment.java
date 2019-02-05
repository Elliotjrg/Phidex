package com.example.phidex.phidex.activities.CoinView.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;

public class StatsFragment extends Fragment{

    private float dChange;
    private String coinId;
    private Coin coin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_fragment, container, false);

        Intent intent = this.getActivity().getIntent();
        coinId = intent.getStringExtra("coinId");
        AppDatabase ad = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        coin = ad.coinDao().getCoinGivenId(coinId);

        dChange = (coin.getPrice() *(coin.getPercentChange())/100);

        TextView volume = rootView.findViewById(R.id.volume);
        TextView percentChange = rootView.findViewById(R.id.percent_change);
        TextView holding = rootView.findViewById(R.id.holding);
        TextView dollarChange = rootView.findViewById(R.id.dollar_change);
        TextView coinCode = rootView.findViewById(R.id.coin_code);
        TextView marketCap = rootView.findViewById(R.id.market_cap);
        TextView supply = rootView.findViewById(R.id.supply);

        marketCap.setText("Market Cap: " + String.format("%.0f", coin.getMarketCap()));
        supply.setText("Supply: " + String.format("%.0f", coin.getSupply()));
        volume.setText("24hr Volume: " + String.format("%.0f", coin.getVolume24hr()));
        coinCode.setText("(" + coin.getCoinCode() + ")");
        holding.setText("Holding: " + String.format("%.2f", (coin.getHolding())));
        percentChange.setText(String.format("%.2f", (coin.getPercentChange())));
        dollarChange.setText(String.format("%.2f", (dChange)));

        if (dChange < 0) {
            dollarChange.setTextColor(Color.RED);
            percentChange.setTextColor(Color.RED);
        } else {
            dollarChange.setTextColor(Color.GREEN);
            percentChange.setTextColor(Color.GREEN);
        }

        return rootView;
    }
}
