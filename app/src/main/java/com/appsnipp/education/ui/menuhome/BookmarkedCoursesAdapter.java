package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.BookmarkedCourseCardBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BookmarkedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ItemClickListener<Course> itemClickListener;
    private List<Course> items;

    public BookmarkedCoursesAdapter(List<Course> items, ItemClickListener<Course> listener) {
        this.items = items;
        this.itemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Course> listItems) {
        this.items = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        BookmarkedCourseCardBinding binding = BookmarkedCourseCardBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Course item = items.get(position);
        ((ViewHolder) holder).bind(item, itemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final BookmarkedCourseCardBinding binding;

        public ViewHolder(BookmarkedCourseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Course item, ItemClickListener<Course> itemClickListener) {
            binding.tvCourseTitle.setText(item.getCourseTitle());
            Glide.with(itemView.getContext())
                    .load(item.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);

            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(item, binding.imvCoursePhoto);
            });
        }
    }
}
