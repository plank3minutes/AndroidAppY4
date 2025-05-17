package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

public class FontSizeUtils {
    // Base text sizes in SP
    private static final float SMALL_TEXT_SIZE = 12;
    private static final float MEDIUM_TEXT_SIZE = 16;
    private static final float LARGE_TEXT_SIZE = 20;

    // Multipliers for different text styles
    private static final float TITLE_MULTIPLIER = 1.5f;      // For titles
    private static final float SUBTITLE_MULTIPLIER = 1.25f;  // For subtitles
    private static final float SMALL_TEXT_MULTIPLIER = 0.875f; // For small text like captions

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
        float baseTextSize = getTextSize(textView.getContext(), textSizeType);
        float finalSize = baseTextSize;

        // Áp dụng hệ số nhân cho các loại text khác nhau
        int textAppearance = textView.getId();
        if (textAppearance == android.R.id.title || 
            textView.getTextSize() > MEDIUM_TEXT_SIZE * 1.2f) {
            // Nếu là title hoặc text size hiện tại lớn hơn medium
            finalSize = baseTextSize * TITLE_MULTIPLIER;
        } else if (textView.getTextSize() > MEDIUM_TEXT_SIZE) {
            // Nếu text size hiện tại lớn hơn medium một chút
            finalSize = baseTextSize * SUBTITLE_MULTIPLIER;
        } else if (textView.getTextSize() < MEDIUM_TEXT_SIZE) {
            // Nếu text size hiện tại nhỏ hơn medium
            finalSize = baseTextSize * SMALL_TEXT_MULTIPLIER;
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, finalSize);
    }
} 