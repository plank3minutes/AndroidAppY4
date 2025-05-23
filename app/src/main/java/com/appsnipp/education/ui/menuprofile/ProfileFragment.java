

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
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsnipp.education.R;
import com.appsnipp.education.data.repository.LessonStatusRepository;
import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.utils.TimeTrackerApp;
import com.appsnipp.education.ui.utils.helpers.FontSizeChangeEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.logging.Logger;

public class ProfileFragment extends BaseFragment {
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
    private ImageView imageViewSetting;
    private TextView textViewDate;
    private TextView courseTakeTextView;
    private TextView quizTakeTextView;
    private TextView profileTitle;
    private TextView timeTrackerTitle;
    private TextView timeTrackerDesc;
    private TextView courseAnalysisTitle;
    private TextView courseAnalysisDesc;
    private TextView quizResultTitle;
    private TextView quizResultDesc;
    private CardView courseAnalysisCardView;
    private CardView quizResultCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TimeTrackerApp app = TimeTrackerApp.getInstance(getContext());
        initComponentView(view);
        handler = new Handler(Looper.getMainLooper());

        timeProgressBar.setProgress(app.getSecondsElapsed());
        updateProgressBarColor(app.getSecondsElapsed());

        textViewDate.setText(app.getToday());

        imageViewSetting.setOnClickListener(imgView -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_fragment_to_setting_fragment);
        });

        startUpdating();

        quizResultCardView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_fragment_to_quiz_result_fragment);
        });

        courseAnalysisCardView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_fragment_to_course_analysis_fragment);
        });

        LiveData<Integer> courseTakenCount = ProgressRepository.getInstance(view.getContext()).getCourseTaken();
        courseTakenCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                courseTakeTextView.setText(String.valueOf(integer));
            }
        });

        LiveData<Integer> quizTakenCount = LessonStatusRepository.getInstance(view.getContext()).getQuizTaken();
        quizTakenCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                quizTakeTextView.setText(String.valueOf(integer));
            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFontSizeChanged(FontSizeChangeEvent event) {
        int newSize = event.getNewFontSize();
        // Update text sizes
        profileTitle.setTextSize(newSize + 12);
        timeTrackerTitle.setTextSize(newSize);
        timeTrackerDesc.setTextSize(newSize - 6);
        courseAnalysisTitle.setTextSize(newSize);
        courseAnalysisDesc.setTextSize(newSize - 6);
        quizResultTitle.setTextSize(newSize);
        quizResultDesc.setTextSize(newSize - 6);
        courseTakeTextView.setTextSize(newSize);
        quizTakeTextView.setTextSize(newSize);
        textViewDate.setTextSize(newSize - 8);
    }

    private void startUpdating() {
        if (!isUpdating) {
            isUpdating = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isUpdating && isAdded()) {
                        TimeTrackerApp app = TimeTrackerApp.getInstance(getContext());
                        int secondsElapsed = app.getSecondsElapsed();
                        timeProgressBar.setProgress(secondsElapsed);
                        updateProgressBarColor(secondsElapsed);
                        updateTimeOfWeek();
                        textViewDate.setText(app.getToday());
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
                innerDrawable = clipDrawable.getDrawable();
                if (innerDrawable instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) innerDrawable;
                    int color = getColor(secondsElapsed);
                    gradientDrawable.setColor(color);
                }
            }
        }
    }

    private void updateTimeOfWeek() {
        TimeTrackerApp app = TimeTrackerApp.getInstance(getContext());
        for(int i = 1; i < 8; i++) {
            int color = getColor(app.getTimeOnlineByDay(i));
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(color);
            switch (i) {
                case 1:
                    imageViewSunday.setBackground(drawable);
                    break;
                case 2:
                    imageViewMonday.setBackground(drawable);
                    break;
                case 3:
                    imageViewTuesday.setBackground(drawable);
                    break;
                case 4:
                    imageViewWednesday.setBackground(drawable);
                    break;
                case 5:
                    imageViewThursday.setBackground(drawable);
                    break;
                case 6:
                    imageViewFriday.setBackground(drawable);
                    break;
                case 7:
                    imageViewSaturday.setBackground(drawable);
                    break;
                default:
                    break;
            }
        }
    }

    private void initComponentView(View view) {
        timeProgressBar = view.findViewById(R.id.time_progress_bar);
        textViewDate = view.findViewById(R.id.text_view_date);
        imageViewSetting = view.findViewById(R.id.img_view_setting);
        quizResultCardView = view.findViewById(R.id.quiz_result_card_view_id);
        courseAnalysisCardView = view.findViewById(R.id.course_analysis_card_view_id);
        courseTakeTextView = view.findViewById(R.id.course_take_text_view);
        quizTakeTextView = view.findViewById(R.id.quiz_take_text_view);

        // Initialize text views for font size changes
        profileTitle = view.findViewById(R.id.profileTitle);
        timeTrackerTitle = view.findViewById(R.id.time_tracker_title);
        timeTrackerDesc = view.findViewById(R.id.time_tracker_desc);
        courseAnalysisTitle = view.findViewById(R.id.course_analysis_title);
        courseAnalysisDesc = view.findViewById(R.id.course_analysis_desc);
        quizResultTitle = view.findViewById(R.id.quiz_result_title);
        quizResultDesc = view.findViewById(R.id.quiz_result_desc);

        for(int i = 1; i < 8; i++) {
            switch (i) {
                case 1:
                    imageViewSunday = view.findViewById(R.id.img_view_sunday);
                    break;
                case 2:
                    imageViewMonday = view.findViewById(R.id.img_view_monday);
                    break;
                case 3:
                    imageViewTuesday = view.findViewById(R.id.img_view_tuesday);
                    break;
                case 4:
                    imageViewWednesday = view.findViewById(R.id.img_view_wednesday);
                    break;
                case 5:
                    imageViewThursday = view.findViewById(R.id.img_view_thursday);
                    break;
                case 6:
                    imageViewFriday = view.findViewById(R.id.img_view_friday);
                    break;
                case 7:
                    imageViewSaturday = view.findViewById(R.id.img_view_saturday);
                    break;
                default:
                    break;
            }
        }
    }

    private int getColor(int time) {
        if (time == 0) {
            return Color.parseColor("#EEEEEE"); // grey color
        } else if (time < 300) {
            return Color.parseColor("#9BE9A8"); // green color
        } else if (time < 600) {
            return Color.parseColor("#40c0c4"); // blue color
        } else if (time < 900) {
            return Color.parseColor("#e8f016"); // yellow color
        } else {
            return Color.parseColor("#e32110"); // red color
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUpdating = false;
        handler.removeCallbacksAndMessages(null);
    }
}