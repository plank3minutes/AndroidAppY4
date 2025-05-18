package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.JoinedCourseCardBinding;
import com.appsnipp.education.databinding.SeeAllCardBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class JoinedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COURSE = 0;
    private static final int VIEW_TYPE_SEE_ALL = 1;

    private final ItemClickListener<Course> itemClickListener;
    private List<Pair<Course, UserProgress>> items;

    public JoinedCoursesAdapter(List<Pair<Course, UserProgress>> items, ItemClickListener<Course> listener) {
        this.items = items;
        this.itemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Pair<Course, UserProgress>> listItems) {
        this.items = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size() >= 2 ? items.size() + 1 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size()) {
            return VIEW_TYPE_SEE_ALL; // Last item is "See All"
        } else {
            return VIEW_TYPE_COURSE;   // Normal course item
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SEE_ALL) {
            SeeAllCardBinding binding = SeeAllCardBinding.inflate(inflater, parent, false);
            return new SeeAllViewHolder(binding);
        } else {
            JoinedCourseCardBinding binding = JoinedCourseCardBinding.inflate(inflater, parent, false);
            return new JoinedCourseViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size()) {
            ((SeeAllViewHolder) holder).bind(itemClickListener);
        } else {
            Pair<Course, UserProgress> item = items.get(position);
            ((JoinedCourseViewHolder) holder).bind(item, itemClickListener);
        }
    }

    public static class SeeAllViewHolder extends RecyclerView.ViewHolder {
        SeeAllCardBinding binding;

        public SeeAllViewHolder(SeeAllCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemClickListener<Course> itemClickListener) {
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(null, null));
        }
    }

    public static class JoinedCourseViewHolder extends RecyclerView.ViewHolder {
        JoinedCourseCardBinding binding;

        public JoinedCourseViewHolder(JoinedCourseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pair<Course, UserProgress> item, ItemClickListener<Course> itemClickListener) {
            Course course = item.first;
            UserProgress progress = item.second;

            int completionPercentage = (progress.getCompletedLessons() * 100) / course.getLessons().size();
            String progressText = completionPercentage + "% Completed";

            binding.tvCourseTitle.setText(course.getCourseTitle());
            binding.progressBar.setProgress(completionPercentage);
            binding.tvPercentage.setText(progressText);
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
