package com.example.phidex.phidex.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(version = 5, entities = {Coin.class, Transaction.class}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    // CoinDao is a class annotated with @Dao.
    public abstract CoinDao coinDao();
    // UserDao is a class annotated with @Dao.
    public abstract TransactionDao transactionDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                    .allowMainThreadQueries() // I read somewhere this is a bad thing? May need to change at some point
                    .fallbackToDestructiveMigration() //This nukes your database when you change the version number
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;

    }
}
