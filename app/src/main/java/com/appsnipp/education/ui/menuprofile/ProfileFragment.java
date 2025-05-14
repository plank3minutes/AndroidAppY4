/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.utils.TimeTrackerApp;

import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private ProgressBar timeProgressBar;
    private Handler handler;
    private boolean isUpdating = false;
    private ImageView imageViewMonday;
    private ImageView imageViewTuesday;
    private ImageView imageViewWednesday;
    private ImageView imageViewThursday;
    private ImageView imageViewFriday;
    private ImageView imageViewSaturday;
    private ImageView imageViewSunday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        timeProgressBar = view.findViewById(R.id.time_progress_bar);
        handler = new Handler(Looper.getMainLooper());

        timeProgressBar.setProgress(TimeTrackerApp.getInstance(getContext()).getSecondsElapsed());
        updateProgressBarColor(TimeTrackerApp.getInstance(getContext()).getSecondsElapsed());
        initTimeOfWeek(view);

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
        Drawable drawable = timeProgressBar.getProgressDrawable();

        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;

            Drawable progressLayer = layerDrawable.findDrawableByLayerId(android.R.id.progress);
            if (progressLayer instanceof ClipDrawable) {
                ClipDrawable clipDrawable = (ClipDrawable) progressLayer;
                Drawable innerDrawable = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    innerDrawable = clipDrawable.getDrawable();
                }
                if (innerDrawable instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) innerDrawable;

                    if (secondsElapsed <= 600) {
                        gradientDrawable.setColor(Color.GREEN);
                    } else if (secondsElapsed <= 1200) {
                        gradientDrawable.setColor(Color.YELLOW);
                    } else if (secondsElapsed <= 1800){
                        gradientDrawable.setColor(Color.BLUE);
                    } else {
                        gradientDrawable.setColor(Color.RED);
                    }
                }
            }
        }
    }

    private void initTimeOfWeek(View view) {
        TimeTrackerApp app = TimeTrackerApp.getInstance(getContext());
        for(int i = 1; i < 8; i++) {
            int color = getColor(app.getTimeOnlineByDay(i));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(color);
            switch (i) {
                case 1:
                    imageViewSunday = view.findViewById(R.id.img_view_sunday);
                    imageViewSunday.setBackground(drawable);
                    break;
                case 2:
                    imageViewMonday = view.findViewById(R.id.img_view_monday);
                    imageViewMonday.setBackground(drawable);
                    break;
                case 3:
                    imageViewTuesday = view.findViewById(R.id.img_view_tuesday);
                    imageViewTuesday.setBackground(drawable);
                    break;
                case 4:
                    imageViewWednesday = view.findViewById(R.id.img_view_wednesday);
                    imageViewWednesday.setBackground(drawable);
                    break;
                case 5:
                    imageViewThursday = view.findViewById(R.id.img_view_thursday);
                    imageViewThursday.setBackground(drawable);
                    break;
                case 6:
                    imageViewFriday = view.findViewById(R.id.img_view_friday);
                    imageViewFriday.setBackground(drawable);
                    break;
                case 7:
                    imageViewSaturday = view.findViewById(R.id.img_view_saturday);
                    imageViewSaturday.setBackground(drawable);
                    break;
                default:
                    break;
            }
        }
    }

    private int getColor(int time) {
        if (time == 0) {
            return Color.parseColor("#E0E0E0");
        } else if (time <= 600) {
            return Color.GREEN;
        } else if(time <= 1200) {
            return Color.YELLOW;
        } else if(time <= 1800) {
            return Color.BLUE;
        } else {
            return Color.RED;
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