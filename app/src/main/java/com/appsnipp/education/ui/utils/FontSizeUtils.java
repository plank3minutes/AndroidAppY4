package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

public class FontSizeUtils {
    // Base text sizes in SP
    private static final float SMALL_TEXT_SIZE = 14;
    private static final float MEDIUM_TEXT_SIZE = 16;
    private static final float LARGE_TEXT_SIZE = 18;

    public static float getTextSize(Context context, int textSizeType) {
        switch (textSizeType) {
            case FontSizePrefManager.FONT_SMALL:
                return SMALL_TEXT_SIZE;
            case FontSizePrefManager.FONT_BIG:
                return LARGE_TEXT_SIZE;
            case FontSizePrefManager.FONT_MEDIUM:
            default:
                return MEDIUM_TEXT_SIZE;
        }
    }

    public static void applyFontSize(TextView textView, int textSizeType) {
        float textSize = getTextSize(textView.getContext(), textSizeType);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }
} 