package com.app.fevir.util.picaso;

import android.text.TextUtils;

import com.app.fevir.deligate.MyApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

public class AnalyticsUtil {

    public static void sendScreenName(String screenName) {
        Tracker tracker = MyApplication.Companion.getContext().getTracker();
        tracker.setScreenName(screenName);

        tracker.send(new HitBuilders.AppViewBuilder().build());
    }

    public static void sendEvent(String category) {
        sendEvent(category, null, null);
    }

    public static void sendEvent(String category, String action) {
        sendEvent(category, action, null);
    }

    public static void sendEvent(String category, String action, String label) {
        Tracker tracker = MyApplication.Companion.getContext().getTracker();
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();
        if (!TextUtils.isEmpty(category)) {
            eventBuilder.setCategory(category);
        }
        if (!TextUtils.isEmpty(action)) {
            eventBuilder.setAction(action);
        }
        if (!TextUtils.isEmpty(label)) {
            eventBuilder.setLabel(label);
        }
        Map<String, String> event = eventBuilder
                .build();
        tracker.send(event);
    }
}
