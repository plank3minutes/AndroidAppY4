package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.BookmarkedCourseCardBinding;
import com.appsnipp.education.ui.base.BaseViewHolder;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BookmarkedCoursesAdapter
        extends RecyclerView.Adapter<BaseViewHolder<Course>> {

    final Context mContext;
    private final ItemClickListener<Course> itemClickListener;
    private List<Course> mCoursesList;

    public BookmarkedCoursesAdapter(Context mContext, List<Course> mData, ItemClickListener<Course> listener) {
        this.mCoursesList = mData;
        this.mContext = mContext;
        this.itemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Course> listItems) {
        this.mCoursesList = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCoursesList == null ? 0 : mCoursesList.size();
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
        BookmarkedCourseCardBinding binding = BookmarkedCourseCardBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder<Course> holder, int i) {
        Course item = mCoursesList.get(i);
        if (item != null) {
            holder.bind(item);
            holder.itemView.setOnClickListener(v -> {
                ViewHolder viewHolder = (ViewHolder) holder;
                itemClickListener.onItemClick(item, viewHolder.getItemCardBinding().imvCoursePhoto);
            });
        }
    }

    public static class ViewHolder extends BaseViewHolder<Course> {
        private final BookmarkedCourseCardBinding binding;

        public ViewHolder(BookmarkedCourseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public BookmarkedCourseCardBinding getItemCardBinding() {
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
