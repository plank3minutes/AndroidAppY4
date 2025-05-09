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
import com.appsnipp.education.ui.model.CourseCard;

import java.util.ArrayList;
import java.util.List;

public class CourseRecyclerAdapter extends
        RecyclerView.Adapter<BaseViewHolder<CourseCard>> {

    final Context mContext;
    private List<CourseCard> mData;
    private final ItemClickListener<CourseCard> itemClickListener;

    public CourseRecyclerAdapter(Context mContext, List<CourseCard> mData, ItemClickListener<CourseCard> listener) {
        this.mContext = mContext;
        this.mData = mData != null ? mData : new ArrayList<>();
        this.itemClickListener = listener;
    }

    // Thêm phương thức để cập nhật danh sách khóa học
    public void setCourseCards(List<CourseCard> courseCards) {
        this.mData = courseCards != null ? courseCards : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<CourseCard> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ItemCardBinding itemCardBinding = ItemCardBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<CourseCard> holder, int position) {
        CourseCard item = mData.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            ViewHolder viewHolder = (ViewHolder) holder;
            itemClickListener.onItemClick(item, viewHolder.getItemCardBinding().cardViewImage);
        });
    }



    @Override
    public long getItemId(int position) {
        CourseCard courseCard = mData.get(position);
        // Chuyển đổi id từ String sang long cho getItemId
        try {
            return Long.parseLong(courseCard.getId());
        } catch (NumberFormatException e) {
            // Nếu không thể chuyển đổi, trả về vị trí
            return position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends BaseViewHolder<CourseCard> {

        ItemCardBinding itemCardBinding;

        public ViewHolder(@NonNull ItemCardBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;
        }

        public ItemCardBinding getItemCardBinding() {
            return itemCardBinding;
        }

        @Override
        public void bind(CourseCard item) {
            this.itemCardBinding.cardViewImage.setImageResource(item.getImageCourse());
            this.itemCardBinding.stagItemCourse.setText(item.getCourseTitle());
            this.itemCardBinding.stagItemQuantityCourse.setText(item.getQuantityCourses());
            this.itemCardBinding.cardViewImage.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color1));
        }
    }
}
