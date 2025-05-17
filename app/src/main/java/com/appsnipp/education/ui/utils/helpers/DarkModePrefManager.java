/*
 * Copyright (c) 2024. rogergcc
 */

package com.appsnipp.education.ui.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;


public class DarkModePrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "education-dark-mode";

    private static final String IS_NIGHT_MODE = "IsNightMode";


    public DarkModePrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(IS_NIGHT_MODE, isDarkMode);
        editor.commit(); // Using commit for immediate effect
    }

    public boolean isNightMode() {
        return pref.getBoolean(IS_NIGHT_MODE, false);
    }

    public void applyTheme() {
        if (isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}