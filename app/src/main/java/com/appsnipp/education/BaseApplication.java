package com.appsnipp.education;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsnipp.education.ui.utils.AppLogger;
import com.appsnipp.education.ui.utils.TimeTrackerApp;


public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private static Resources resources;
    private static BaseApplication mInstance;

    private int activityCount = 0;

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public static Resources getAppResources() {
        return resources;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resources = getResources();
        AppLogger.init();
        registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (activityCount == 0) {
            Log.d("App Tracker", "Running");
            TimeTrackerApp.getInstance(this).startTracking();
        }
        activityCount++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityCount--;
        if (activityCount == 0) {
            TimeTrackerApp.getInstance(this).stopTracking();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
