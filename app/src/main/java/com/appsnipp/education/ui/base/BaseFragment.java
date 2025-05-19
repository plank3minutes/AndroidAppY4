package com.appsnipp.education.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

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
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            applyFontSizeToView(getView());
        }
    }

    protected void applyFontSizeToView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                applyFontSizeToView(child);
            }
        } else if (view instanceof TextView) {
            FontSizeUtils.applyFontSize((TextView) view, fontSizePrefManager.getFontSize());
        }
    }
} 