package com.app.fevir.deligate

import android.app.Application

import com.app.fevir.R
import com.app.fevir.deligate.component.DaggerApplicationComponent
import com.app.fevir.deligate.module.ApplicationModule
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Logger
import com.google.android.gms.analytics.Tracker

class MyApplication : Application() {
    // Then return the tracker
    var tracker: Tracker? = null
        private set

    override fun onCreate() {
        super.onCreate()
        MyApplication.context = this

        startTracking()

        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build().inject(this)
    }

    private fun startTracking() {

        // Initialize an Analytics tracker using a Google Analytics property ID.

        if (tracker == null) {
            val ga = GoogleAnalytics.getInstance(this)

            // Get the config data for the tracker
            tracker = ga.newTracker(R.xml.analytics_global_config)

            // Enable tracking of activities
            ga.enableAutoActivityReports(this)

            // Set the log level to verbose.
            ga.logger.logLevel = Logger.LogLevel.VERBOSE
        }
    }

    companion object {
        var context: MyApplication? = null
            get
            private set

    }

}