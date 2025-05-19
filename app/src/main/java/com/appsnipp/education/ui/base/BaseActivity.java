package com.appsnipp.education.ui.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.appsnipp.education.ui.utils.helpers.FontSizeChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity {
    protected FontSizePrefManager fontSizePrefManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        fontSizePrefManager = new FontSizePrefManager(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFontSizeChanged(FontSizeChangeEvent event) {
        applyFontSizeToAllViews();
    }

    protected void applyFontSizeToAllViews() {
        ViewGroup root = findViewById(android.R.id.content);
        applyFontSizeToViewGroup(root);
    }

    private void applyFontSizeToViewGroup(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            
            if (child instanceof TextView || 
                child instanceof EditText || 
                child instanceof Button ||
                child instanceof AppCompatTextView ||
                child instanceof AppCompatEditText ||
                child instanceof AppCompatButton ||
                child instanceof AutoCompleteTextView ||
                child instanceof MultiAutoCompleteTextView) {
                
                // Áp dụng font size cho text
                FontSizeUtils.applyFontSize((TextView) child, fontSizePrefManager.getFontSize());
                
                // Nếu là EditText hoặc các dạng input, áp dụng cho hint text
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    FontSizeUtils.applyHintTextSize(editText, fontSizePrefManager.getFontSize());
                }
                
                // Xử lý đặc biệt cho TextInputLayout
                if (child instanceof TextInputLayout) {
                    TextInputLayout textInputLayout = (TextInputLayout) child;
                    FontSizeUtils.applyTextInputLayoutSize(textInputLayout, fontSizePrefManager.getFontSize());
                }
            }
            
            // Đệ quy cho các ViewGroup con
            if (child instanceof ViewGroup) {
                applyFontSizeToViewGroup((ViewGroup) child);
            }
        }
    }
} 