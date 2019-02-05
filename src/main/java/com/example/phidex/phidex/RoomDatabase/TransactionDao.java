package com.example.phidex.phidex.RoomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    // Get all the transactions
    @Query("SELECT * FROM Transactions")
    List<Transaction> getAllTransactions();

    // Get all transactions that involved a particular coin (string)
    @Query("SELECT * FROM Transactions WHERE coinName = :coin")
    List<Transaction> getTransactionsFromCoinId(String coin);

    // Insert transaction into database
    @Insert
    void insert(Transaction... transaction);

    // Delete transaction with the supplied ID
    @Query("DELETE FROM Transactions WHERE transactionId = :id")
    void deleteTransactionFromId (int id);

    // Delete all transactions with supplied coin_name
    @Query("DELETE FROM Transactions WHERE coinName = :coin")
    void deleteTransactionsFromCoinId (String coin);
}