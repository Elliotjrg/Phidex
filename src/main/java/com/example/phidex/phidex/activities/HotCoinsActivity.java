package com.example.phidex.phidex.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.adapters.HotCoinsAdapter;
import com.example.phidex.phidex.utils.HotCoinsScraper;

import java.util.ArrayList;

public class HotCoinsActivity extends AppCompatActivity {

    private static final String TAG = "HotCoinsActivity";
    private ArrayList<HotCoin> mCoins = new ArrayList<>();
    private HotCoinsScraper scraper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hotcoins);

        Log.d(TAG, "onCreate: started.");

        RecyclerView rcv = findViewById(R.id.hotcoins_recycler);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter adapter = new HotCoinsAdapter(this, mCoins);
        rcv.setAdapter(adapter);

        scraper = new HotCoinsScraper(adapter);
        initCoins();

    }

    private void initCoins() {
        Log.d(TAG, "initCoins: preparing coins.");
        scraper.getHotCoins("trending", mCoins);
    }


    // these arent all actually strings, but theyre all getting printed so who cares?
    public static class HotCoin {
        private String number;
        private String image_url;
        private String code;
        private String name;
        private String market_cap;
        private String price;
        private String percent_increase;

        public HotCoin(String number, String image_url, String code, String name, String market_cap, String price, String percent_increase) {
            this.number = number;
            this.image_url = image_url;
            this.code = code;
            this.name = name;
            this.market_cap = market_cap;
            this.price = price;
            this.percent_increase = percent_increase;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMarket_cap() {
            return market_cap;
        }

        public void setMarket_cap(String market_cap) {
            this.market_cap = market_cap;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPercent_increase() {
            return percent_increase;
        }

        public void setPercent_increase(String percent_increase) {
            this.percent_increase = percent_increase;
        }
    }
}
