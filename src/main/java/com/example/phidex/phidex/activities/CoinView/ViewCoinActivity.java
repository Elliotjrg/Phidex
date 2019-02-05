package com.example.phidex.phidex.activities.CoinView;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.activities.CoinView.fragments.StatsFragment;
import com.example.phidex.phidex.activities.CoinView.fragments.ChartsFragment;
import com.example.phidex.phidex.activities.CoinView.fragments.NewsFragment;
import com.example.phidex.phidex.activities.CoinView.fragments.TransactionsFragment;

public class ViewCoinActivity extends AppCompatActivity  {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static String PACKAGE_NAME;
    private ViewPager mViewPager;
    private TextView coinNameTextView;
    private ImageView coinImageView;
    private String coinId;
    private TransactionsFragment transactionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        //Grab the passed in coinName value
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");

        coinId = intent.getStringExtra("coinId");

        AppDatabase ad = AppDatabase.getAppDatabase(getApplicationContext());
        Coin coin = ad.coinDao().getCoinGivenId(coinId);
        float price = coin.getPrice();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_coin);

        //Set coinName value to TextView
        coinNameTextView = findViewById(R.id.coinName);
        coinNameTextView.setText(name);

        //set Image according to Coin name
        coinImageView = findViewById(R.id.tabCoinImage);
        int resId = getResources().getIdentifier(coinId, "drawable", PACKAGE_NAME);
        coinImageView.setImageResource(resId);

        TextView balanceTextView;
        balanceTextView = findViewById(R.id.viewCoinBalance);
        balanceTextView.setText("$ " + Float.toString(price));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_coin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTransactionsFragment() {
        if (transactionsFragment != null) {
            transactionsFragment.updateFragment();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("coinId", coinId);

            //returning current tab
            switch (position){
                case 0:
                    StatsFragment statsFragment = new StatsFragment();
                    statsFragment.setArguments(bundle);
                    return statsFragment;
                case 1:
                    ChartsFragment chartsFragment = new ChartsFragment();
                    chartsFragment.setArguments(bundle);
                    return chartsFragment;
                case 2:
                    return new NewsFragment();
                case 3:
                    transactionsFragment = new TransactionsFragment();
                    transactionsFragment.setArguments(bundle);
                    return transactionsFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "STATS";
                case 1:
                    return "CHARTS";
                case 2:
                    return "NEWS";
                case 3:
                    return "PORTFOLIO HISTORY";
            }
            return null;
        }
    }
}
