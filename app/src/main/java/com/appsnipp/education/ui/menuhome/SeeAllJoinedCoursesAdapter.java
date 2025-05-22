package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.CardJoinedCourseSaBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SeeAllJoinedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ItemClickListener<Course> itemClickListener;
    private List<Pair<Course, UserProgress>> items;
    private final Context context;
    private final FontSizePrefManager fontSizePrefManager;

    public SeeAllJoinedCoursesAdapter(Context context, List<Pair<Course, UserProgress>> items, ItemClickListener<Course> listener) {
        this.items = items;
        this.itemClickListener = listener;
        this.context = context;
        this.fontSizePrefManager = new FontSizePrefManager(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Pair<Course, UserProgress>> listItems) {
        this.items = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardJoinedCourseSaBinding binding = CardJoinedCourseSaBinding.inflate(inflater, parent, false);
        return new SeeAllJoinedCoursesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pair<Course, UserProgress> item = items.get(position);
        ((SeeAllJoinedCoursesViewHolder) holder).bind(item, itemClickListener);
        // Set font size for the title and description
        FontSizeUtils.applyFontSize(((SeeAllJoinedCoursesViewHolder) holder).binding.tvCourseTitle, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(((SeeAllJoinedCoursesViewHolder) holder).binding.tvLessonProgress, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(((SeeAllJoinedCoursesViewHolder) holder).binding.tvFeaturedProgressPercentage, fontSizePrefManager.getFontSize());
    }

    public static class SeeAllJoinedCoursesViewHolder extends RecyclerView.ViewHolder {
        CardJoinedCourseSaBinding binding;

        public SeeAllJoinedCoursesViewHolder(CardJoinedCourseSaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pair<Course, UserProgress> item, ItemClickListener<Course> itemClickListener) {
            Course course = item.first;
            UserProgress progress = item.second;

            int completionPercentage = (progress.getCompletedLessons() * 100) / course.getLessons().size();
            String progressText = completionPercentage + "% Completed";
            String lessonProgressText = "Finish " + progress.getCompletedLessons() + "/" + course.getLessons().size() + " lessons";

            binding.tvCourseTitle.setText(course.getCourseTitle());
            binding.progressBar.setProgress(completionPercentage);
            binding.tvFeaturedProgressPercentage.setText(progressText);
            binding.tvLessonProgress.setText(lessonProgressText);
            Glide.with(itemView.getContext())
                    .load(course.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);

            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(course, binding.imvCoursePhoto);
            });
        }
    }
}

