package com.example.phidex.phidex.RoomDatabase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "Transactions")
public class Transaction {
    @PrimaryKey (autoGenerate = true)
    private int transactionId;

    private String date;
    private String coinName;
    private float coinAmount;
    private float pricePaid;

    public Transaction(String coinName, float coinAmount, float pricePaid) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss");
        this.date = df.format(new Date());

        this.coinName = coinName;
        this.coinAmount = coinAmount;
        this.pricePaid = pricePaid;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public float getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(float coinAmount) {
        this.coinAmount = coinAmount;
    }

    public float getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(float pricePaid) {
        this.pricePaid = pricePaid;
    }
}
