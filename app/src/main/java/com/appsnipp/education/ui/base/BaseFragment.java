package com.appsnipp.education.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class BaseFragment extends Fragment {
    protected FontSizePrefManager fontSizePrefManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontSizePrefManager = new FontSizePrefManager(requireContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyFontSizeToView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFontSizeChanged(FontSizeChangeEvent event) {
        if (getView() != null) {
            applyFontSizeToView(getView());
        }
    }

    protected void applyFontSizeToView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
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
                    applyFontSizeToView(child);
                }
            }
        } else if (view instanceof TextView) {
            FontSizeUtils.applyFontSize((TextView) view, fontSizePrefManager.getFontSize());
        }
    }
} 