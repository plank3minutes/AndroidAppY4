package com.appsnipp.education.ui.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

public class BaseActivity extends AppCompatActivity {
    protected FontSizePrefManager fontSizePrefManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        fontSizePrefManager = new FontSizePrefManager(newBase);
        super.attachBaseContext(newBase);
    }

    protected void applyFontSizeToAllViews() {
        ViewGroup root = findViewById(android.R.id.content);
        applyFontSizeToViewGroup(root);
    }

    private void applyFontSizeToViewGroup(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            
            if (child instanceof TextView) {
                FontSizeUtils.applyFontSize((TextView) child, fontSizePrefManager.getFontSize());
            } else if (child instanceof ViewGroup) {
                applyFontSizeToViewGroup((ViewGroup) child);
            }
        }
    }
} 