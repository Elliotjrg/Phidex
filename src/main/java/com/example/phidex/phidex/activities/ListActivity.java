package com.example.phidex.phidex.activities;

/**
 * This interface allows both Portfolio and Watch list activities to be accessed interchangeably.
 */
public interface ListActivity {

    PortfolioActivity.PortfolioStats updateActivity();
}
