package com.appsnipp.education.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessons;
    private final LessonListener lessonListener;

    public LessonAdapter(List<Lesson> lessons, LessonListener lessonListener) {
        this.lessons = lessons;
        this.lessonListener = lessonListener;
    }

    public void updateLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.bind(lesson, position);
    }

    @Override
    public int getItemCount() {
        return lessons != null ? lessons.size() : 0;
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLessonTitle;
        private final TextView textLessonDescription;
        private final ImageView imageVideoIndicator;
        private final ImageView imageBookmark;
        private final ImageView imageCompleted;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            textLessonTitle = itemView.findViewById(R.id.textLessonTitle);
            textLessonDescription = itemView.findViewById(R.id.textLessonDescription);
            imageVideoIndicator = itemView.findViewById(R.id.imageVideoIndicator);
            imageBookmark = itemView.findViewById(R.id.imageBookmark);
            imageCompleted = itemView.findViewById(R.id.imageCompleted);
        }

        public void bind(Lesson lesson, int position) {
            textLessonTitle.setText(lesson.getTitle());
            textLessonDescription.setText(lesson.getContent().substring(0, Math.min(lesson.getContent().length(), 100)) + "...");
            
            // Show video indicator if video URL exists
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                imageVideoIndicator.setVisibility(View.VISIBLE);
            } else {
                imageVideoIndicator.setVisibility(View.GONE);
            }
            
            // Set bookmark icon
            imageBookmark.setImageResource(lesson.isBookmarked() ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_border);
            
            // Setup click listeners
            itemView.setOnClickListener(v -> {
                if (lessonListener != null) {
                    lessonListener.onLessonClicked(lesson, position);
                }
            });
            
            imageBookmark.setOnClickListener(v -> {
                if (lessonListener != null) {
                    lessonListener.onBookmarkClicked(lesson, position);
                }
            });
        }
    }

    public interface LessonListener {
        void onLessonClicked(Lesson lesson, int position);
        void onBookmarkClicked(Lesson lesson, int position);
    }
} 