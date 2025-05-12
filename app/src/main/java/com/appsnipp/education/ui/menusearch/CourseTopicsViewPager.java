/*
 * Copyright (c) 2021. rogergcc
 */

package com.appsnipp.education.ui.menusearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.ItemCardBinding;
import com.appsnipp.education.databinding.ItemPagerCardBinding;
import com.appsnipp.education.ui.base.BaseViewHolder;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.menucourses.CourseRecyclerAdapter;
import com.appsnipp.education.ui.model.Course;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.List;

public class CourseTopicsViewPager extends RecyclerView.Adapter<BaseViewHolder<Course>> {
    private List<Course> mCoursesList;
    private final ItemClickListener<Course> courseClickListener;

    public CourseTopicsViewPager(ItemClickListener<Course> listener) {
        this.courseClickListener = listener;
    }

    public void setListDataItems(List<Course> listItems) {
        this.mCoursesList = listItems != null ? listItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<Course> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ItemPagerCardBinding itemPagerCardBinding = ItemPagerCardBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(itemPagerCardBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Course> holder, int position) {
        Course item = mCoursesList.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            ViewHolder viewHolder = (ViewHolder) holder;
            courseClickListener.onItemClick(item, viewHolder.getItemPagerCardBinding().image);
        });
    }

    @Override
    public int getItemCount() {
        return mCoursesList != null ? mCoursesList.size() : 0;
    }

    public static class ViewHolder extends BaseViewHolder<Course> {

        ItemPagerCardBinding itemPagerCardBinding;

        public ViewHolder(@NonNull ItemPagerCardBinding itemPagerCardBinding) {
            super(itemPagerCardBinding.getRoot());
            this.itemPagerCardBinding = itemPagerCardBinding;
        }

        public ItemPagerCardBinding getItemPagerCardBinding() {
            return itemPagerCardBinding;
        }

        @Override
        public void bind(Course item) {
            this.itemPagerCardBinding.tvTitulo.setText(item.getName());
            this.itemPagerCardBinding.tvCantidadCursos.setText(item.getLessonCount() + " bài học");
            Glide.with(itemView.getContext())
                    .load(item.getImageResource())
                    .transform(new CenterCrop())
                    .into(itemPagerCardBinding.image);
        }
    }
}
