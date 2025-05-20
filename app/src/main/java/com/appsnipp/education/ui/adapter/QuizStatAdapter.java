package com.appsnipp.education.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.data.converter.DateConverter;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.model.Quiz;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class QuizStatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_VIEW_MORE = 1;
    private boolean isExpanded;
    private List<LessonStatus> lessonStatuses;
    private Map<String, Course> courseMap;

    public QuizStatAdapter(List<LessonStatus> ls, Map<String, Course> courseMap) {
        this.lessonStatuses = ls;
        this.courseMap = courseMap;
        this.isExpanded = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            LessonStatus lessonStatus = this.lessonStatuses.get(position);
            QuizStatViewHolder quizHolder = (QuizStatViewHolder) holder;
            quizHolder.courseNameTextView.setText(courseMap.get(lessonStatus.getCourseId()).getTitle());
            quizHolder.quizScoreTextView.setText(getScoreTextView(position));
            quizHolder.lessonNameTextView.setText(getLessonName(position));
            quizHolder.quizDateTextView.setText(DateConverter.getDateFormated(lessonStatus.getCompletedAt()));
            int progressPercentage = getPercentageProgress(position);
            quizHolder.progressQuiz.setProgress(progressPercentage);
            quizHolder.accuracyPercentageTextView.setText(String.format(Locale.getDefault(), "%d%%", progressPercentage));
        } else if (holder instanceof ButtonViewAllHolder) {
            ButtonViewAllHolder buttonViewAllHolder = (ButtonViewAllHolder) holder;
            buttonViewAllHolder.materialButton.setOnClickListener(v -> {
                isExpanded = true;
                notifyDataSetChanged(); // Cập nhật danh sách
            });
        }
    }

    @Override
    public int getItemCount() {
        return isExpanded ? lessonStatuses.size() : Math.min(lessonStatuses.size(), 3) + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == lessonStatuses.size()) {
            return VIEW_TYPE_VIEW_MORE;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    static class QuizStatViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private final TextView quizScoreTextView;
        private final TextView lessonNameTextView;
        private final TextView quizDateTextView;
        private final TextView accuracyPercentageTextView;
        private final LinearProgressIndicator progressQuiz;
        public QuizStatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.courseNameTextView = itemView.findViewById(R.id.course_name_tv);
            this.quizScoreTextView = itemView.findViewById(R.id.quiz_score_tv);
            this.lessonNameTextView = itemView.findViewById(R.id.lesson_name_tv);
            this.quizDateTextView = itemView.findViewById(R.id.quiz_date_tv);
            this.accuracyPercentageTextView = itemView.findViewById(R.id.accuracy_percentage_tv);
            this.progressQuiz = itemView.findViewById(R.id.progress_quiz);
        }
    }

    static class ButtonViewAllHolder extends RecyclerView.ViewHolder {
        private final MaterialButton materialButton;
        public ButtonViewAllHolder(@NonNull View itemView) {
            super(itemView);
            materialButton = itemView.findViewById(R.id.view_all_btn);
        }
    }

    private String getScoreTextView(int position){
        LessonStatus lessonStatus = this.lessonStatuses.get(position);
        int score = lessonStatus.getQuizScore();
        int totalQuiz = 0;
        for(Lesson ls : this.courseMap.get(lessonStatus.getCourseId()).getLessons()) {
            if(ls.getId().equals(lessonStatus.getLessonId())) {
                totalQuiz = ls.getQuiz().getQuestions().size();
            }
        }
        return String.format(Locale.getDefault(), "%d/%d", score, totalQuiz);
    }

    private String getLessonName(int position) {
        LessonStatus lessonStatus = this.lessonStatuses.get(position);
        for(Lesson ls : this.courseMap.get(lessonStatus.getCourseId()).getLessons()) {
            if(ls.getId().equals(lessonStatus.getLessonId())) {
                return ls.getTitle();
            }
        }
        return "";
    }

    private int getPercentageProgress(int position) {
        LessonStatus lessonStatus = this.lessonStatuses.get(position);
        int score = lessonStatus.getQuizScore();
        int totalQuiz = 0;
        for(Lesson ls : this.courseMap.get(lessonStatus.getCourseId()).getLessons()) {
            if(ls.getId().equals(lessonStatus.getLessonId())) {
                totalQuiz = ls.getQuiz().getQuestions().size();
            }
        }
        return (int) Math.ceil((double) score/totalQuiz * 100);
    }
}
