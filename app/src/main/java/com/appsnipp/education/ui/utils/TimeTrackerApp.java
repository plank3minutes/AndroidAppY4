/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

public class TimeTrackerApp {

    private static final String PREFS_NAME = "tracker";
    private static final String KEY_SECONDS = "secondsElapsed";

    private int secondsElapsed = 0;
    private Handler handler;
    private boolean isRunning = false;

    private static TimeTrackerApp instance;

    private Context appContext;

    private TimeTrackerApp(Context context) {
        this.appContext = context.getApplicationContext();
        this.handler = new Handler(Looper.getMainLooper());

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        secondsElapsed = prefs.getInt(KEY_SECONDS, 0);
    }

    public static synchronized TimeTrackerApp getInstance(Context context) {
        if (instance == null) {
            instance = new TimeTrackerApp(context);
        }
        return instance;
    }

    public void startTracking() {
        if (!isRunning) {
            isRunning = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    secondsElapsed++;
                    saveTime();
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void stopTracking() {
        handler.removeCallbacksAndMessages(null);
        isRunning = false;
        saveTime();
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    private void saveTime() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SECONDS, secondsElapsed);
        editor.apply();
    }
}
