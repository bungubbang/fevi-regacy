package com.app.fevir;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by dusskapark on 7/19/15.
 */
public class FaviApplication extends Application {

    private GoogleAnalytics analytics;
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1);
        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

        tracker = analytics.newTracker(R.xml.analytics_global_config); // Replace with actual tracker/property Id
        tracker.enableAdvertisingIdCollection(true);

    }
}
