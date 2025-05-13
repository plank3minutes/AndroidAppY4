/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeTrackerApp {

    private static final String PREFS_NAME = "tracker";
    private static final String KEY_SECONDS = "secondsElapsed";
    private static final String KEY_LAST_TRACK_DATE = "lastTrackDate";
    private int secondsElapsed = 0;
    private Handler handler;
    private boolean isRunning = false;

    private static TimeTrackerApp instance;

    private Context appContext;

    private TimeTrackerApp(Context context) {
        this.appContext = context.getApplicationContext();
        this.handler = new Handler(Looper.getMainLooper());

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String today = getCurrentDate();
        String lastDate = prefs.getString(KEY_LAST_TRACK_DATE, "");
        if (!today.equals(lastDate)) {
            prefs.edit()
                    .putString(KEY_LAST_TRACK_DATE, today).commit();
            int dayOfWeek = getCurrentDayOfWeek();
            if (dayOfWeek == 1) {
                prefs.edit().putInt(KEY_SECONDS, 0).commit();
                clearDataLastWeek(prefs);
            }
            else {
                String previousDayKey = getCurrentDayString(dayOfWeek) + "_TIME_ONLINE";
                prefs.edit()
                        .putInt(previousDayKey, prefs.getInt(KEY_SECONDS, 0));
            }
        }
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

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String getCurrentDayString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            default:
                return "UNKNOWN";
        }
    }

    private void clearDataLastWeek(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor
                .putInt("MONDAY_TIME_ONLINE", 0)
                .putInt("TUESDAY_TIME_ONLINE", 0)
                .putInt("WEDNESDAY_TIME_ONLINE", 0)
                .putInt("THURSDAY_TIME_ONLINE", 0)
                .putInt("FRIDAY_TIME_ONLINE", 0)
                .putInt("SATURDAY_TIME_ONLINE", 0)
                .putInt("SUNDAY_TIME_ONLINE", 0);
        editor.commit();
    }
}
