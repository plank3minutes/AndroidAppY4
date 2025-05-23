package com.appsnipp.education.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuizStatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_VIEW_MORE = 1;
    private static final int MAX_ITEMS_TO_SHOW = 4;
    private boolean isExpanded;
    private List<LessonStatus> lessonStatuses;
    private Map<String, Course> courseMap;
    private FontSizePrefManager fontSizePrefManager;

    public QuizStatAdapter(List<LessonStatus> ls, Map<String, Course> courseMap) {
        this.lessonStatuses = ls;
        this.courseMap = courseMap;
        this.isExpanded = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize FontSizePrefManager with parent context if not already initialized
        if (fontSizePrefManager == null) {
            fontSizePrefManager = new FontSizePrefManager(parent.getContext());
        }

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_result, parent, false);
            return new QuizStatViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button_view_all, parent, false);
            return new ButtonViewAllHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuizStatViewHolder) {
            QuizStatViewHolder quizStatViewHolder = (QuizStatViewHolder) holder;
            LessonStatus lessonStatus = lessonStatuses.get(position);
            Course course = courseMap.get(lessonStatus.getCourseId());

            quizStatViewHolder.courseNameTv.setText(course.getTitle());
            quizStatViewHolder.lessonNameTv.setText(getLessonName(position));
            quizStatViewHolder.quizScoreTv.setText(getScoreTextView(position));
            quizStatViewHolder.accuracyPercentageTv.setText(String.format("%d%%", getPercentageProgress(position)));
            quizStatViewHolder.progressQuiz.setProgress(getPercentageProgress(position));

            // Apply font sizes
            if (fontSizePrefManager != null) {
                FontSizeUtils.applyFontSize(quizStatViewHolder.courseNameTv, fontSizePrefManager.getFontSize());
                FontSizeUtils.applyFontSize(quizStatViewHolder.lessonNameTv, fontSizePrefManager.getFontSize());
                FontSizeUtils.applyFontSize(quizStatViewHolder.quizScoreTv, fontSizePrefManager.getFontSize());
                FontSizeUtils.applyFontSize(quizStatViewHolder.accuracyPercentageTv, fontSizePrefManager.getFontSize());
            }
        } else if (holder instanceof ButtonViewAllHolder) {
            ButtonViewAllHolder buttonViewAllHolder = (ButtonViewAllHolder) holder;
            if (fontSizePrefManager != null) {
                FontSizeUtils.applyFontSize(buttonViewAllHolder.viewAllBtn.findViewById(R.id.tvSeeAll), fontSizePrefManager.getFontSize());
            }
            buttonViewAllHolder.materialButton.setOnClickListener(v -> {
                isExpanded = true;
                notifyDataSetChanged(); // Cập nhật danh sách
            });
        }
    }

    @Override
    public int getItemCount() {
        if (lessonStatuses == null) return 0;
        return isExpanded ? lessonStatuses.size() : Math.min(lessonStatuses.size(), MAX_ITEMS_TO_SHOW);
    }

    @Override
    public int getItemViewType(int position) {
        if (!isExpanded && position == MAX_ITEMS_TO_SHOW - 1) {
            return VIEW_TYPE_VIEW_MORE;
        }
        return VIEW_TYPE_ITEM;
    }

    static class QuizStatViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTv;
        TextView lessonNameTv;
        TextView quizScoreTv;
        TextView accuracyPercentageTv;
        LinearProgressIndicator progressQuiz;

        QuizStatViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTv = itemView.findViewById(R.id.course_name_tv);
            lessonNameTv = itemView.findViewById(R.id.lesson_name_tv);
            quizScoreTv = itemView.findViewById(R.id.quiz_score_tv);
            accuracyPercentageTv = itemView.findViewById(R.id.accuracy_percentage_tv);
            progressQuiz = itemView.findViewById(R.id.progress_quiz);
        }
    }

    static class ButtonViewAllHolder extends RecyclerView.ViewHolder {
        MaterialCardView viewAllBtn;
        MaterialCardView materialButton;

        ButtonViewAllHolder(@NonNull View itemView) {
            super(itemView);
            viewAllBtn = itemView.findViewById(R.id.view_all_btn);
            materialButton = itemView.findViewById(R.id.view_all_btn);
        }
    }

    private String getScoreTextView(int position) {
        LessonStatus lessonStatus = this.lessonStatuses.get(position);
        int totalQuiz = 0;
        for (Lesson ls : this.courseMap.get(lessonStatus.getCourseId()).getLessons()) {
            if (ls.getId().equals(lessonStatus.getLessonId())) {
                totalQuiz += ls.getQuiz().getQuestions().size();
            }
        }
        int score = (int) Math.ceil(lessonStatus.getQuizScore() / 100.0 * totalQuiz);
        return String.format(Locale.getDefault(), "%d/%d", score, totalQuiz);
    }

    private String getLessonName(int position) {
        LessonStatus lessonStatus = this.lessonStatuses.get(position);
        for (Lesson ls : this.courseMap.get(lessonStatus.getCourseId()).getLessons()) {
            if (ls.getId().equals(lessonStatus.getLessonId())) {
                return ls.getTitle();
            }
        }
        return "";
    }

    private int getPercentageProgress(int position) {
        return lessonStatuses.get(position).getQuizScore();
    }
}
