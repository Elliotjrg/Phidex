package com.example.phidex.phidex.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.RoomDatabase.Transaction;
import com.example.phidex.phidex.adapters.PortfolioAdapter;
import com.example.phidex.phidex.utils.CoinDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListActivity {

    public static String PACKAGE_NAME;

    private RecyclerView  recyclerView;
    private PortfolioAdapter adapter;
    private List<Coin> coins;
    private AppDatabase ad;
    private CoinDataUtil coinDataUtil;
    private Map<String, String> coinsMap;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("!", "onCreate has been called for portfolioActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        recyclerView = findViewById(R.id.recycler_view);
        ad = AppDatabase.getAppDatabase(getApplicationContext());
        coinDataUtil = new CoinDataUtil(ad, this);

        if (coinsMap == null) {
            coinsMap = new HashMap<>();
        }

        coinDataUtil.addTopCoinsToDatabase(coinsMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ImageView button_arrow = findViewById(R.id.button_arrow);
        button_arrow.setImageResource(R.drawable.arrow);


        new CountDownTimer(3000, 1000) { // 3000 = 3 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                button_arrow.setVisibility(View.GONE);
            }
        }.start();



        PortfolioStats result = updateActivity();

        float dollarChange = result.getChange();
        float totalBalance = result.getBalance();

        TextView balance = findViewById(R.id.balance);
        balance.setText("$" + String.format("%.2f", totalBalance));

        TextView portfolioChange = findViewById(R.id.portfolioChange);
        portfolioChange.setText("$" + String.format("%.2f", dollarChange));
        ImageView arrow = findViewById(R.id.arrowPortfolio);

        if (dollarChange < 0) {
            portfolioChange.setTextColor(Color.RED);
            arrow.setImageResource(R.drawable.arrow_down);
        } else {
            portfolioChange.setTextColor(Color.GREEN);
            arrow.setImageResource(R.drawable.arrow_up);
        }


        myDialog = new Dialog(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateActivity();
    }

    public void showPopUp(View view) {

        if (coinsMap == null) {
            coinsMap = new HashMap<>();
        }
        coinDataUtil.populateMapOfCoinNames(coinsMap);

        myDialog.setContentView(R.layout.pop_up);

        // AUTOCOMPLETE TEXT BOX CODE
        // map of (k: v) = (name: Id)
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
                TextView amountView = myDialog.findViewById(R.id.amount);
                TextView priceView = myDialog.findViewById(R.id.price);

                try {
                    String coin = coinView.getText().toString();
                    float amount = Float.parseFloat(amountView.getText().toString());
                    float price = Float.parseFloat(priceView.getText().toString());

                    if (!coinsMap.containsKey(coin)) {
                        throw new RuntimeException("invalid coin: " + coin);
                    }

                    pushTransactionToDatabase(coinsMap.get(coin), amount, price);
                } catch (Exception e) {
                    // maybe show an error message here?
                    Log.e("popup", e.toString());
                    Log.e("coinsMap = ", coinsMap.toString());
                    myDialog.dismiss();
                }


                if (view.getId() == R.id.confirmButton) {
                    PortfolioStats result = updateActivity();
                    myDialog.dismiss();
                }
            }
        });

        myDialog.show();
    }

    public void pushTransactionToDatabase(String coin, float amount, float price) {
        Transaction t = new Transaction(coin, amount, price);
        ad.transactionDao().insert(t);
        coinDataUtil.increaseCoinHolding(coin, amount);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.portfolio, menu);
        MenuItem searchItem = menu.findItem(R.id.nav_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_portfolio) {
            updateActivity();

        } else if (id == R.id.nav_transactions) {
            Intent intent = new Intent(this, TransactionsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_hot_coins) {
            Intent intent = new Intent(this, HotCoinsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {
            Intent intent = new Intent(this, WatchListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // Used to refresh the coins shown on the portfolio page
    public PortfolioStats updateActivity() {
        coins = ad.coinDao().getCoinsInPortfolio();
        adapter = new PortfolioAdapter(coins, ad, this, this);
        recyclerView.setAdapter(adapter);

        // Calculate sum & change of current holdings.
        float totalBalance = 0;
        float dollarChange = 0;
        for (int i = 0; i < coins.size(); i++) {
            totalBalance = totalBalance + (coins.get(i).getPrice() * coins.get(i).getHolding());
            dollarChange = dollarChange + ((coins.get(i).getPrice() * coins.get(i).getHolding())*(coins.get(i).getPercentChange())/100);
        }

        TextView balance = findViewById(R.id.balance);
        balance.setText("$" + String.format("%.2f", totalBalance));

        TextView portfolioChange = findViewById(R.id.portfolioChange);
        portfolioChange.setText("$" + String.format("%.2f", dollarChange));
        ImageView arrow = findViewById(R.id.arrowPortfolio);

        if (dollarChange < 0) {
            portfolioChange.setTextColor(Color.RED);
            arrow.setImageResource(R.drawable.arrow_down);
        } else {
            portfolioChange.setTextColor(Color.GREEN);
            arrow.setImageResource(R.drawable.arrow_up);
        }

        return new PortfolioStats(totalBalance, dollarChange);
    }


    final class PortfolioStats {
        private final float balance;
        private final float change;

        public PortfolioStats(float balance, float change) {
            this.balance = balance;
            this.change = change;
        }

        public float getBalance() {
            return balance;
        }

        public float getChange() {
            return change;
        }
    }
}