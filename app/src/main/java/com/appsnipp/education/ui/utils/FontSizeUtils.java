package com.appsnipp.education.ui.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

public class FontSizeUtils {
    // Base text size in SP
    private static final float MEDIUM_TEXT_SIZE = 16; // Default size
    private static final float SIZE_DIFFERENCE = 3;   // Difference between sizes

    // Text sizes for different categories
    private static final float[] HEADER_SIZES = {25, 28, 31};       // Large headers (e.g. Profile title)
    private static final float[] TITLE_SIZES = {17, 20, 23};        // Section titles
    private static final float[] SUBTITLE_SIZES = {13, 16, 19};     // Section subtitles
    private static final float[] BODY_SIZES = {13, 16, 19};         // Normal text
    private static final float[] CAPTION_SIZES = {11, 14, 17};      // Small text, captions
    private static final float[] HINT_SIZES = {11, 14, 17};         // Hint text sizes

    public static float getTextSize(Context context, int textSizeType, TextView textView) {
        // Get the base size array based on text type
        float[] sizeArray;
        
        int textAppearance = textView.getId();
        float currentSize = textView.getTextSize() / textView.getContext().getResources().getDisplayMetrics().scaledDensity;

        // Determine text category
        if (textAppearance == R.id.profileTitle || 
            currentSize >= 24) {
            sizeArray = HEADER_SIZES;
        }
        else if (textAppearance == android.R.id.title || 
                 currentSize >= 20 ||
                 (textView.getTypeface() != null && textView.getTypeface().getStyle() == Typeface.BOLD)) {
            sizeArray = TITLE_SIZES;
        }
        else if (currentSize >= MEDIUM_TEXT_SIZE) {
            sizeArray = SUBTITLE_SIZES;
        }
        else if (currentSize < 14) {
            sizeArray = CAPTION_SIZES;
        }
        else {
            sizeArray = BODY_SIZES;
        }

        // Return appropriate size based on preference
        switch (textSizeType) {
            case FontSizePrefManager.FONT_SMALL:
                return sizeArray[0];
            case FontSizePrefManager.FONT_BIG:
                return sizeArray[2];
            case FontSizePrefManager.FONT_MEDIUM:
            default:
                return sizeArray[1];
        }
    }

    public static void applyFontSize(TextView textView, int textSizeType) {
        float finalSize = getTextSize(textView.getContext(), textSizeType, textView);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, finalSize);
    }

    public static void applyHintTextSize(EditText editText, int textSizeType) {
        // Áp dụng size cho hint text
        float hintSize = HINT_SIZES[textSizeType == FontSizePrefManager.FONT_SMALL ? 0 : 
                                  textSizeType == FontSizePrefManager.FONT_BIG ? 2 : 1];
        
        // Thiết lập kích thước hint bằng cách tạo TextPaint mới
        TextPaint paint = editText.getPaint();
        paint.setTextSize(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            hintSize,
            editText.getResources().getDisplayMetrics()
        ));
        editText.setHint(editText.getHint()); // Trigger redraw hint
    }

    public static void applyTextInputLayoutSize(TextInputLayout textInputLayout, int textSizeType) {
        // Áp dụng size cho hint và error text của TextInputLayout
        int styleResId = getTextAppearanceStyle(textSizeType);
        textInputLayout.setHintTextAppearance(styleResId);
        textInputLayout.setErrorTextAppearance(styleResId);
        
        // Áp dụng size cho EditText bên trong nếu có
        if (textInputLayout.getEditText() != null) {
            applyFontSize(textInputLayout.getEditText(), textSizeType);
        }
    }

    private static int getTextAppearanceStyle(int textSizeType) {
        switch (textSizeType) {
            case FontSizePrefManager.FONT_SMALL:
                return R.style.TextAppearance_App_Hint_Small;
            case FontSizePrefManager.FONT_BIG:
                return R.style.TextAppearance_App_Hint_Large;
            case FontSizePrefManager.FONT_MEDIUM:
            default:
                return R.style.TextAppearance_App_Hint;
        }
    }
} 