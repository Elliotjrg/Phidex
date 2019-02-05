package com.example.phidex.phidex.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phidex.phidex.activities.PortfolioActivity;
import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.activities.CoinView.ViewCoinActivity;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolders> {

    List<Coin> coins;
    Context mContext;
    private static AppDatabase ad;
    Resources res;
    public static float coinHolding;

    public WatchListAdapter(List<Coin> coins, AppDatabase ad, Context context) {
        this.coins = coins;
        this.ad = ad;
        this.res = context.getResources();
        this.mContext = context;
    }

    @Override
    public WatchListAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_coin_row, parent, false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(final WatchListAdapter.ViewHolders holder, int position) {

        final Coin thisCoin = coins.get(position);

        holder.coinName.setText(thisCoin.getCoinName());
        holder.price.setText("Price: $" + Float.toString(thisCoin.getPrice()));
        holder.percentChange.setText(Float.toString(thisCoin.getPercentChange()));


        // dynamic image change based on Coin name
        String imageString = thisCoin.getCoinName().toLowerCase().replaceAll("\\s","");
        int resId = res.getIdentifier(imageString, "drawable", PortfolioActivity.PACKAGE_NAME);
        holder.coinIcon.setImageResource(resId);

        if(thisCoin.getPercentChange() > 0) {
            holder.percentChange.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.arrow_up);
        } else {
            holder.percentChange.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.arrow_down);
        }

        holder.holding.setText(thisCoin.getHolding() + " " + thisCoin.getCoinCode() + ": $" + Float.toString(thisCoin.getPrice() * thisCoin.getHolding()));
        coinHolding = thisCoin.getPrice();

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewCoinActivity.class);
                intent.putExtra("name", thisCoin.getCoinName());
                intent.putExtra("coinId", thisCoin.getCoinId());
                mContext.startActivity(intent);
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final PopupMenu popup = new PopupMenu(view.getContext(), holder.itemView, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.portfolio_delete_coin_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ad.coinDao().removeCoinFromWatchlist(thisCoin.getCoinId());
                        Toast.makeText(mContext, "Coin deleted from watchlist.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (coins == null) {
            System.out.println("No Coins!\n\n\n");
            return 0;
        }
        return coins.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        public View itemView;
        public LinearLayout parentLayout;
        public TextView coinName;
        public TextView price;
        public TextView percentChange;
        public TextView holding;
        public ImageView arrow;
        public ImageView coinIcon;


        public ViewHolders(View itemView) {
            super(itemView);
            this.itemView = itemView;
            parentLayout = itemView.findViewById(R.id.portfolio_parent);
            coinName = itemView.findViewById(R.id.coin_name);
            price = itemView.findViewById(R.id.price);
            percentChange = itemView.findViewById(R.id.percent_change);
            holding = itemView.findViewById(R.id.holding);
            arrow = itemView.findViewById(R.id.arrow);
            coinIcon = itemView.findViewById(R.id.coin_icon);
        }
    }
}
