/*
 * Copyright (c) 2021. rogergcc
 */

package com.appsnipp.education.ui.menucourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.ItemCardBinding;
import com.appsnipp.education.ui.base.BaseViewHolder;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CourseRecyclerAdapter extends
        RecyclerView.Adapter<BaseViewHolder<Course>> {

    final Context mContext;
    private List<Course> mData;
    private final ItemClickListener<Course> itemClickListener;

    public CourseRecyclerAdapter(Context mContext, List<Course> mData, ItemClickListener<Course> listener) {
        this.mData = mData;
        this.mContext = mContext;
        this.itemClickListener = listener;
    }

    public void setCourseCards(List<Course> courses) {
        this.mData = courses != null ? courses : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<Course> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ItemCardBinding itemCardBinding = ItemCardBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Course> holder, int position) {
        Course item = mData.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            ViewHolder viewHolder = (ViewHolder) holder;
            itemClickListener.onItemClick(item, viewHolder.getItemCardBinding().cardViewImage);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        Course course = mData.get(position);
        return Long.parseLong(course.getId());
    }

    public static class ViewHolder extends BaseViewHolder<Course> {
        private final ItemCardBinding itemCardBinding;

        public ViewHolder(@NonNull ItemCardBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;
        }

        public ItemCardBinding getItemCardBinding() {
            return itemCardBinding;
        }

        @Override
        public void bind(Course item) {
            // Tải hình ảnh bằng Glide thay vì setImageResource
            Glide.with(itemView.getContext())
                    .load(item.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(itemCardBinding.cardViewImage);

            // Đặt tiêu đề khóa học
            itemCardBinding.stagItemCourse.setText(item.getCourseTitle());

            // Sửa lỗi: Chuyển int thành String cho số lượng bài học
            itemCardBinding.stagItemQuantityLesson.setText(item.getLessonCount() + " bài học");

            // Đặt màu nền
            itemCardBinding.cardViewImage.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color1));
        }
    }

}
