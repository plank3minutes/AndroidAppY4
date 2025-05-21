package com.appsnipp.education.ui.adapter;

import android.content.Context;
import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.CourseStat;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

import java.util.List;

public class CourseStatAdapter extends RecyclerView.Adapter<CourseStatAdapter.CourseStatViewHolder> {
    private List<Course> courses;
    private List<Integer> progresses;
    private CourseStatListener listener;
    private final FontSizePrefManager fontSizePrefManager;
    private final Context context;

    public CourseStatAdapter(Context context, List<Course> courses, List<Integer> progresses, CourseStatListener listener) {
        this.context = context;
        this.courses = courses;
        this.progresses = progresses;
        this.listener = listener;
        this.fontSizePrefManager = new FontSizePrefManager(context);
    }

    @NonNull
    @Override
    public CourseStatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_stat_item, parent, false);
        return new CourseStatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseStatViewHolder holder, int position) {
        Course course = courses.get(position);
        int progress = progresses.get(position);
        holder.progressTv.setText(String.format("%d%% completed", progress));
        holder.titleCourseTv.setText(course.getCourseTitle());
        holder.courseIv.setImageResource(course.getImageResource());
        holder.progressBar.setProgress(progress);
        holder.linearLayout.setOnClickListener(v -> listener.onCourseStatClicked(course.getId()));

        // Áp dụng font size cho text
        FontSizeUtils.applyFontSize(holder.titleCourseTv, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(holder.progressTv, fontSizePrefManager.getFontSize());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateData(List<Course> courses, List<Integer> progresses, CourseStatListener listener) {
        this.courses = courses;
        this.progresses = progresses;
        this.listener = listener;
        notifyDataSetChanged();
    }

    static class CourseStatViewHolder extends RecyclerView.ViewHolder {
        private final ImageView courseIv;
        private final TextView titleCourseTv;
        private final TextView progressTv;
        private final ProgressBar progressBar;
        private final LinearLayout linearLayout;
        public CourseStatViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.item_course_stat);
            courseIv = itemView.findViewById(R.id.image_course_iv);
            titleCourseTv = itemView.findViewById(R.id.title_course_tv);
            progressBar = itemView.findViewById(R.id.progress_bar_pb);
            progressTv = itemView.findViewById(R.id.progress_tv);
        }
    }

    public interface CourseStatListener {
        void onCourseStatClicked(String courseId);
    }
}
