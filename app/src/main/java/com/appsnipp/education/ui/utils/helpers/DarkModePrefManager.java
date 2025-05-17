/*
 * Copyright (c) 2024. rogergcc
 */

package com.appsnipp.education.ui.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;


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
        // Force light mode by default
        if (!pref.contains(IS_NIGHT_MODE)) {
            setDarkMode(false);
        }
    }

    public void setDarkMode(boolean isFirstTime) {
        editor.putBoolean(IS_NIGHT_MODE, isFirstTime);
        editor.commit(); // Using commit instead of apply for immediate effect
    }

    public boolean isNightMode() {
        return pref.getBoolean(IS_NIGHT_MODE, false);
    }

}