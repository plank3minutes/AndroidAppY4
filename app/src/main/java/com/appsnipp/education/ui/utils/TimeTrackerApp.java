/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeTrackerApp {
    private static final String TAG = "TIME_TRACKER_APP";
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

        String todayString = getCurrentDate();
        String lastDateString = prefs.getString(KEY_LAST_TRACK_DATE, "");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date lastDate = null;
        try {
            lastDate = sdf.parse(lastDateString);
        } catch (ParseException e) {
            Log.e(TAG, "TimeTrackerApp: ", e);
            lastDate = new Date();
        }

        if (!todayString.equals(lastDateString)) {
            prefs.edit()
                    .putString(KEY_LAST_TRACK_DATE, todayString).commit();
            if (lastDate.before(getDayWeekStart())) {
                clearDataLastWeek(prefs);
            } else {
                String previousTimeOnlineKey = getDayOfWeek(lastDateString) + "_TIME_ONLINE";
                prefs.edit().putInt(previousTimeOnlineKey, prefs.getInt(KEY_SECONDS, 0)).commit();
            }
            prefs.edit().putInt(KEY_SECONDS, 0).commit();
            secondsElapsed = 0;
        } else {
            secondsElapsed = prefs.getInt(KEY_SECONDS, 0);
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
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    private Date getDayWeekStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    public int getDayOfWeek(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
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

    public int getTimeOnlineByDay(int dayOfWeek) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(dayOfWeek + "_TIME_ONLINE", 0);
    }

    public String getToday() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LAST_TRACK_DATE, getCurrentDate());
    }
}
