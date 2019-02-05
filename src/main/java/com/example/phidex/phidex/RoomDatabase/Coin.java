package com.example.phidex.phidex.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.widget.util.SortedListAdapterCallback;

@Entity(tableName = "Coins")
public class Coin {
    @NonNull
    @PrimaryKey
    private String coinId;

    @ColumnInfo(name = "coin_name")
    private String coinName;

    @ColumnInfo(name = "coin_code")
    private String coinCode;

    @ColumnInfo(name = "coin_description")
    private String coinDescription;

    @ColumnInfo(name ="price")
    private float price;

    @ColumnInfo(name="portfolio_rank")
    private int portfolioRank;

    @ColumnInfo(name="watchlist_rank")
    private int watchlistRank;

    @ColumnInfo(name="holding")
    private float holding;

    @ColumnInfo(name="volume_24hr")
    private double volume24hr;

    @ColumnInfo(name="market_cap")
    private double marketCap;

    @ColumnInfo(name="available_supply")
    private double availableSupply;

    @ColumnInfo(name="supply")
    private double supply;

    @ColumnInfo(name="percent_change")
    private float percentChange;

 /** Not sure what this is for, have left it commented out
    private float updated; // float?
**/

    public Coin(String coinId, String coinName, String coinCode, String coinDescription,
                float price, int portfolioRank, int watchlistRank, float holding, double volume24hr,
                double marketCap, double availableSupply, double supply, float percentChange) {
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinCode = coinCode;
        this.coinDescription = coinDescription;
        this.price = price;
        this.portfolioRank = portfolioRank;
        this.watchlistRank = watchlistRank;
        this.holding = holding;
        this.volume24hr = volume24hr;
        this.marketCap = marketCap;
        this.availableSupply = availableSupply;
        this.supply = supply;
        this.percentChange = percentChange;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinDescription() {
        return coinDescription;
    }

    public void setCoinDescription(String coinDescription) {
        this.coinDescription = coinDescription;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public int getPortfolioRank() {
        return portfolioRank;
    }

    public void setPortfolioRank(int portfolioRank) {
        this.portfolioRank = portfolioRank;
    }

    public int getWatchlistRank() {
        return watchlistRank;
    }

    public void setWatchlistRank(int watchlistRank) {
        this.watchlistRank = watchlistRank;
    }

    public float getHolding() {
        return holding;
    }

    public void setHolding(float holding) {
        this.holding = holding;
    }

    public double getVolume24hr() {
        return volume24hr;
    }

    public void setVolume24hr(double volume24hr) {
        this.volume24hr = volume24hr;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(double availableSupply) {
        this.availableSupply = availableSupply;
    }

    public double getSupply() {
        return supply;
    }

    public void setSupply(double supply) {
        this.supply = supply;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

}

