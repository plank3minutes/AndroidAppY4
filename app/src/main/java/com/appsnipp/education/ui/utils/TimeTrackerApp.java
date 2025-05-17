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

        initAndUpdateInRealTime();
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
                    initAndUpdateInRealTime();
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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
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

    private void clearDataLastWeek(SharedPreferences.Editor editor) {
        for (int i = 1; i < 8; i++) {
            editor.putInt(i + "_TIME_ONLINE", 0);
        }
    }

    public int getTimeOnlineByDay(int dayOfWeek) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(dayOfWeek + "_TIME_ONLINE", 0);
    }

    public String getToday() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LAST_TRACK_DATE, getCurrentDate());
    }

    private void initAndUpdateInRealTime() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String todayString = sdf.format(new Date());
        String lastDateString = prefs.getString(KEY_LAST_TRACK_DATE, "");

        Date lastDate = null;
        try {
            lastDate = lastDateString.isEmpty() ? null : sdf.parse(lastDateString);
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse lastDateString: " + lastDateString, e);
        }

        // Nếu lastDate không hợp lệ, gán mặc định là ngày hôm nay
        if (lastDate == null) {
            lastDate = new Date();
        }

        SharedPreferences.Editor editor = prefs.edit();
        // Nếu ngày hôm nay khác ngày lưu lần trước, cần cập nhật lại dữ liệu
        if (!todayString.equals(lastDateString)) {
            editor.putString(KEY_LAST_TRACK_DATE, todayString);

            // Kiểm tra nếu lastDate nằm trước tuần hiện tại, xóa dữ liệu tuần trước
            if (lastDate.before(getDayWeekStart())) {
                clearDataLastWeek(editor);  // Sửa clearDataLastWeek nhận editor để gom commit
            } else {
                // Lưu thời gian online của ngày trước vào key riêng theo thứ
                String previousTimeOnlineKey = getDayOfWeek(lastDateString) + "_TIME_ONLINE";
                System.out.println(previousTimeOnlineKey);
                int seconds = prefs.getInt(KEY_SECONDS, 0);
                editor.putInt(previousTimeOnlineKey, seconds);
            }
            // Reset thời gian online cho ngày hôm nay
            editor.putInt(KEY_SECONDS, 0);
            editor.apply();

            secondsElapsed = 0;
        } else {
            // Vẫn trong ngày, lấy lại số giây đã tính
            secondsElapsed = prefs.getInt(KEY_SECONDS, 0);
            editor.putString(KEY_LAST_TRACK_DATE, todayString);
        }
    }
}
