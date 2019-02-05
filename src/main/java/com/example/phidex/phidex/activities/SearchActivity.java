package com.example.phidex.phidex.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.adapters.SearchCoinAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView  recyclerViewSearch;
    private SearchView searchBox;
    private SearchCoinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchBox = findViewById(R.id.main_search);
        recyclerViewSearch = findViewById(R.id.recycler_view_search);
        AppDatabase ad = AppDatabase.getAppDatabase(getApplicationContext());

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        final List<Coin> allCoins = ad.coinDao().getCoins();
        assert recyclerViewSearch != null;
        adapter = new SearchCoinAdapter(ad, this, ALPHABETICAL_COMPARATOR);
        recyclerViewSearch.setAdapter(adapter);
        adapter.addAll(allCoins);

        // Makes entire search bar clickable, and not just the icon
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.setIconified(false);
            }
        });

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                final List<Coin> filteredModelList = filter(allCoins, s);
                adapter.replaceAll(filteredModelList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<Coin> filteredModelList = filter(allCoins, s);
                adapter.replaceAll(filteredModelList);
                return true;
            }
        });

    }

    // Method for comparing coins by alphabetical order.
    private static final Comparator<Coin> ALPHABETICAL_COMPARATOR = new Comparator<Coin>() {
        @Override
        public int compare(Coin a, Coin b) {
            return a.getCoinId().compareTo(b.getCoinId());
        }
    };

    private static List<Coin> filter(List<Coin> coin, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Coin> filteredModelList = new ArrayList<>();

        for (Coin model : coin) {
            final String text = model.getCoinId().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
