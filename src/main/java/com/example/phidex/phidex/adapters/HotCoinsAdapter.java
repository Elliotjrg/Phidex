package com.example.phidex.phidex.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phidex.phidex.activities.HotCoinsActivity;
import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.activities.CoinView.ViewCoinActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class HotCoinsAdapter extends RecyclerView.Adapter<HotCoinsAdapter.ViewHolder> {

    private static final String TAG = "HotCoinsAdapter";

    private final DecimalFormatSymbols symbols;
    private final DecimalFormat format;
    private Context mContext;
    private ArrayList<HotCoinsActivity.HotCoin> mCoins;
    private AppDatabase ad;

    private static Coin defaultCoin = new Coin(
            null,
            null,
            null,
            null,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    );

    public HotCoinsAdapter(Context mContext, ArrayList<HotCoinsActivity.HotCoin> mCoins) {
        this.mContext = mContext;
        this.mCoins = mCoins;
        this.ad = AppDatabase.getAppDatabase(mContext);

        this.symbols = new DecimalFormatSymbols();
        this.format = new DecimalFormat("#,###", symbols);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hot_coin_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final HotCoinsActivity.HotCoin p = mCoins.get(position);

        Coin c = ad.coinDao().getCoinGivenName(p.getName());

        if (c == null) {
            Log.e(TAG, p.getName() + " was not found in the database");
            c = defaultCoin;
        }

        holder.rank.setText(p.getNumber());
        Glide.with(mContext).asBitmap().load(p.getImage_url()).into(holder.icon);
        holder.code.setText(p.getCode());
        holder.name.setText(p.getName());
        holder.market_cap.setText("Market Cap: $" + format.format(c.getMarketCap()));
        holder.price.setText("Price $" + Float.toString(c.getPrice()));

        holder.change.setText("1D% Change: " + Float.toString(c.getPercentChange()) + "%");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an out link");

                Coin c = ad.coinDao().getCoinGivenName(p.getName());
                if (c == null) {
                    Toast.makeText(mContext, "Don't bother: this coin is not tracked in this app!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, ViewCoinActivity.class);
                    intent.putExtra("name", c.getCoinName());
                    intent.putExtra("coinId", c.getCoinId());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView rank;
        ImageView icon;
        TextView code;
        TextView name;
        TextView market_cap;
        TextView price;
        TextView change;

        public ViewHolder (View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.hcr_constraint_layout);
            rank = itemView.findViewById(R.id.hcr_rank);
            icon = itemView.findViewById(R.id.hcr_icon);
            code = itemView.findViewById(R.id.hcr_code);
            name = itemView.findViewById(R.id.hcr_name);
            market_cap = itemView.findViewById(R.id.hcr_market_cap);
            price = itemView.findViewById(R.id.hcr_price);
            change = itemView.findViewById(R.id.hcr_change);
        }
    }
}
