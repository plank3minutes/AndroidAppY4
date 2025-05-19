/*
 * Copyright (c) 2023. rogergcc
 */

package com.appsnipp.education.ui.menuhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.CardPopularCoursesBinding;
import com.appsnipp.education.ui.base.BaseViewHolder;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PopularCoursesAdapter
        extends RecyclerView.Adapter<BaseViewHolder<Course>> {

    final Context mContext;
    private final ItemClickListener<Course> itemClickListener;
    private List<Course> mCoursesList;
    private final FontSizePrefManager fontSizePrefManager;

    public PopularCoursesAdapter(Context mContext, List<Course> mData, ItemClickListener<Course> listener) {
        this.mCoursesList = mData;
        this.mContext = mContext;
        this.itemClickListener = listener;
        this.fontSizePrefManager = new FontSizePrefManager(mContext);
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

    @Override
    public long getItemId(int position) {
        Course course = mCoursesList.get(position);
        return Long.parseLong(course.getId());
    }

    @NotNull
    @Override
    public BaseViewHolder<Course> onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        CardPopularCoursesBinding binding = CardPopularCoursesBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Course> holder, int position) {
        Course item = mCoursesList.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            ViewHolder viewHolder = (ViewHolder) holder;
            itemClickListener.onItemClick(item, viewHolder.getItemCardBinding().imvCoursePhoto);
        });

        // Áp dụng font size cho text trong ViewHolder
        ViewHolder viewHolder = (ViewHolder) holder;
        TextView courseTitle = viewHolder.getItemCardBinding().tvCourseTitle;
        FontSizeUtils.applyFontSize(courseTitle, fontSizePrefManager.getFontSize());
    }

    public static class ViewHolder extends BaseViewHolder<Course> {
        private final CardPopularCoursesBinding binding;

        public ViewHolder(CardPopularCoursesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public CardPopularCoursesBinding getItemCardBinding() {
            return binding;
        }

        @Override
        public void bind(Course data) {
            binding.tvCourseTitle.setText(data.getCourseTitle());
            Glide.with(itemView.getContext())
                .load(data.getImageResource())
                .apply(new RequestOptions().centerCrop())
                .into(binding.imvCoursePhoto);
        }
    }
}