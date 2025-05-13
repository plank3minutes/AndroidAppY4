/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.utils.TimeTrackerApp;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private ProgressBar timeProgressBar;
    private Handler handler;
    private boolean isUpdating = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        timeProgressBar = view.findViewById(R.id.time_progress_bar);
        handler = new Handler(Looper.getMainLooper());

        // Cập nhật ProgressBar ban đầu
        timeProgressBar.setProgress(TimeTrackerApp.getInstance(getContext()).getSecondsElapsed());
        updateProgressBarColor(TimeTrackerApp.getInstance(getContext()).getSecondsElapsed());

        // Bắt đầu cập nhật thực thời
        startUpdating();

        return view;
    }

    private void startUpdating() {
        if (!isUpdating) {
            isUpdating = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isUpdating && isAdded()) {
                        int secondsElapsed = TimeTrackerApp.getInstance(getContext()).getSecondsElapsed();
                        timeProgressBar.setProgress(secondsElapsed);
                        updateProgressBarColor(secondsElapsed);
                        handler.postDelayed(this, 1000);
                    }
                }
            });
        }
    }

    private void updateProgressBarColor(int secondsElapsed) {
        LayerDrawable layerDrawable = (LayerDrawable) timeProgressBar.getProgressDrawable();

        // Thường progress nằm ở index 1 hoặc 2 tùy vào theme/style
        Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);

        // Kiểm tra và cast đúng kiểu
        if (progressDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) progressDrawable;
            if (secondsElapsed <= 600) {
                gradientDrawable.setColor(0xFF00FF00); // Xanh lá
            } else if (secondsElapsed <= 1200) {
                gradientDrawable.setColor(0xFFFFEB3B); // Vàng
            } else {
                gradientDrawable.setColor(0xFFFF0000); // Đỏ
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        isUpdating = false;
    }
}