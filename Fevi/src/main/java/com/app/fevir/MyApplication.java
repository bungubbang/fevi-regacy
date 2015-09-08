package com.app.fevir;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

//import android.util.Log;


public class MyApplication extends Application {
    public Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        startTracking();

    }

    private void startTracking() {

        // Initialize an Analytics tracker using a Google Analytics property ID.

        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            // Get the config data for the tracker
            mTracker = ga.newTracker(R.xml.analytics_global_config);

            // Enable tracking of activities
            ga.enableAutoActivityReports(this);

            // Set the log level to verbose.
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    public Tracker getTracker() {
        // Then return the tracker
        return mTracker;
    }

}