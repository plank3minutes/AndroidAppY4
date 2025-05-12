/*
 * Copyright (c) 2024. rogergcc
 */

package com.appsnipp.education.ui.menusearch;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.ItemPopularCourseBinding;
import com.appsnipp.education.ui.model.Course;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private static ClickListener mClickListener;
    private List<Course> mCoursesList;

    public CoursesAdapter(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setListDataItems(List<Course> listItems) {
        this.mCoursesList = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCoursesList == null)
            return 0;
        else
            return mCoursesList.size();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemPopularCourseBinding binding = ItemPopularCourseBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, int i) {
        Course item = mCoursesList.get(i);
        if (item != null) {
            viewHolder.bind(item);
        }
    }

    public interface ClickListener {
        void onClick(Course course, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPopularCourseBinding binding;

        public ViewHolder(@NonNull ItemPopularCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Course data) {
            binding.tvTitleCourse.setText(data.getCourseTitle());
            binding.tvDetailsCourse.setText(data.getDescription());
            itemView.setOnClickListener(v -> mClickListener.onClick(data, getAdapterPosition()));
        }
    }
}