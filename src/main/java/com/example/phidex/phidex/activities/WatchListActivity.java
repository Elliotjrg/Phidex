package com.example.phidex.phidex.activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.adapters.WatchListAdapter;
import com.example.phidex.phidex.utils.CoinDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchListActivity extends AppCompatActivity implements ListActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Coin> coins;
    AppDatabase ad;
    Dialog myDialog;
    Map<String, String> coinsMap;
    CoinDataUtil coinDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        recyclerView = findViewById(R.id.recycler_view_watch_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager recyclerViewLayoutManager1 = layoutManager;

        recyclerView.setLayoutManager(recyclerViewLayoutManager1);

        ad = AppDatabase.getAppDatabase(getApplicationContext());

        myDialog = new Dialog(this);
        coinDataUtil = new CoinDataUtil(ad, this);

        if (coinsMap == null) {
            coinsMap = new HashMap<>();
        }

        coinDataUtil.populateMapOfCoinNames(coinsMap);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateActivity();
    }

    public PortfolioActivity.PortfolioStats updateActivity() {
        coins = ad.coinDao().getCoinsInWatchList();
        adapter = new WatchListAdapter(coins, ad, this);
        recyclerView.setAdapter(adapter);
        return null;
    }

    public void showPopUp(View view) {
        myDialog.setContentView(R.layout.wl_pop_up);

        // AUTOCOMPLETE TEXT BOX CODE
        List<String> coinsList = new ArrayList<>(); // arrayadapter requires a list, not a map
        coinsList.addAll(coinsMap.keySet());
        Log.d("coinsList", coinsMap.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, coinsList );
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView coin = (AutoCompleteTextView) myDialog.findViewById(R.id.actvCoin);
        coin.setThreshold(2);
        coin.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        // END AUTOCOMPLETE TEXT BOX CODE

        TextView closeButton = (TextView) myDialog.findViewById(R.id.closePopUp);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        Button cancelButton = (Button) myDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        Button confirmButton = (Button) myDialog.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView coinView = myDialog.findViewById(R.id.actvCoin);

                try {
                    String coinName = coinView.getText().toString();
                    String coinId = coinsMap.get(coinName);

                    coinDataUtil.addCoinToWatchlist(coinId);

                    if (!coinsMap.containsKey(coinName)) {
                        throw new RuntimeException("invalid coin: " + coinName);
                    }
                } catch (Exception e) {
                    // maybe show an error message here?
                    Log.e("popup", e.toString());
                    Log.e("coinsMap = ", coinsMap.toString());
                    myDialog.dismiss();
                }


                if (view.getId() == R.id.confirmButton) {
                    updateActivity();
                    myDialog.dismiss();
                }
            }
        });

        myDialog.show();
    }
}
