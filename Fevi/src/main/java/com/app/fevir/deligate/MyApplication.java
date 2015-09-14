package com.app.fevir.deligate;

import android.app.Application;

import com.app.fevir.R;
import com.app.fevir.deligate.component.DaggerApplicationComponent;
import com.app.fevir.deligate.module.ApplicationModule;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

public class MyApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        startTracking();

        DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build()
                .inject(this);
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