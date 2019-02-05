package com.example.phidex.phidex.RoomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CoinDao {

    // Get all coins in database
    @Query("SELECT * FROM Coins")
    List<Coin> getCoins();

    // Get all coins in the portfolio
    @Query("SELECT * FROM Coins WHERE portfolio_rank > -1")
    List<Coin> getCoinsInPortfolio();

    // Get all coins in the portfolio
    @Query("SELECT * FROM Coins WHERE watchlist_rank > -1")
    List<Coin> getCoinsInWatchList();

    // Get all names of coins in portfolio
    @Query("SELECT coin_name FROM Coins WHERE portfolio_rank > -1")
    List<String> getCoinNamesInPortfolio();

    @Query("SELECT coin_name FROM Coins WHERE watchlist_rank > -1")
    List<String> getCoinNamesInWatchlist();

    // Get portfolio rank given coin_name
    @Query("SELECT portfolio_rank FROM Coins WHERE coinId = :coinId")
    int getCoinPortfolioRankGivenId(String coinId);

    // Get watchlist rank given coin_name
    @Query("SELECT watchlist_rank FROM Coins WHERE coinId = :coinId")
    int getCoinWatchlistRankGivenId(String coinId);

    // Get Coin given coinId
    @Query("SELECT * FROM Coins WHERE coinId = :coinId")
    Coin getCoinGivenId(String coinId);

    // Get Coin given coin_name
    @Query("SELECT * FROM Coins WHERE coin_name = :coin_name")
    Coin getCoinGivenName(String coin_name);

    // Get coin code given coinId
    @Query("SELECT coin_code FROM Coins WHERE coinId = :coinId")
    String getCoinCode(String coinId);

    // Get number of coins in portfolio
    @Query("SELECT COUNT(*) FROM Coins WHERE portfolio_rank > -1")
    int getNumberOfCoinsInPortfolio();


    // Insert coin(s) into the database
    @Insert
    void insertAll(Coin... coin);

    // Update a coin by replacing its fields piece by piece
    @Query("UPDATE Coins " +
            "SET price = :price, volume_24hr = :volume24hr, market_cap = :marketCap, " +
            "available_supply = :availableSupply, percent_change = :percentChange " +
            "WHERE coinId = :coinId")
    int updateCoinData(String coinId, float price, double volume24hr, double marketCap, double availableSupply, float percentChange);

    // Remove coin from portfolio
    @Query("UPDATE Coins SET holding=0, portfolio_rank=-1 WHERE coinId=:coinId")
    void removeCoinFromPortfolio(String coinId);

    // Remove coin from watchlist
    @Query("UPDATE Coins Set watchlist_rank=-1 WHERE coinId=:coinId")
    void removeCoinFromWatchlist(String coinId);

    // Add amount to coin's holdings
    @Query("UPDATE Coins " +
            "SET holding = holding + :amount " +
            "WHERE coinId = :coinId")
    void addToCoinHolding(String coinId, float amount);

    // Set coin's portfolio rank
    @Query("UPDATE Coins SET portfolio_rank = :portfolioRank WHERE coinId = :coinId")
    void setCoinPortfolioRank(int portfolioRank, String coinId);

    // Set coin's watchlist rank
    @Query("UPDATE Coins SET watchlist_rank = :watchlistRank WHERE coinId = :coinId")
    void setCoinWatchlistRank(int watchlistRank, String coinId);
}


