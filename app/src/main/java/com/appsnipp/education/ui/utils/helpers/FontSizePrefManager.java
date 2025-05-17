package com.appsnipp.education.ui.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class FontSizePrefManager {
    private static final String PREF_NAME = "FontSizePrefs";
    private static final String KEY_FONT_SIZE = "font_size";
    
    public static final int FONT_SMALL = 0;
    public static final int FONT_MEDIUM = 1;
    public static final int FONT_BIG = 2;
    
    private final SharedPreferences preferences;
    
    public FontSizePrefManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public void setFontSize(int size) {
        preferences.edit().putInt(KEY_FONT_SIZE, size).apply();
    }
    
    public int getFontSize() {
        return preferences.getInt(KEY_FONT_SIZE, FONT_MEDIUM); // Default is medium
    }
} 