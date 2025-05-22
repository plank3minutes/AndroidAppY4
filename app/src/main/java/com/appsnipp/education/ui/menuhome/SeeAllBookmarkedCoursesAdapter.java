package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.CardBookmarkedCourseSaBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SeeAllBookmarkedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ItemClickListener<Course> itemClickListener;
    private List<Course> items;
    private final Context context;
    private final FontSizePrefManager fontSizePrefManager;

    public SeeAllBookmarkedCoursesAdapter(Context context, List<Course> items, ItemClickListener<Course> listener) {
        this.items = items;
        this.itemClickListener = listener;
        this.context = context;
        this.fontSizePrefManager = new FontSizePrefManager(context);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardBookmarkedCourseSaBinding binding = CardBookmarkedCourseSaBinding.inflate(inflater, parent, false);
        return new SeeAllBookMarkedCoursesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Course item = items.get(position);
        ((SeeAllBookMarkedCoursesViewHolder) holder).bind(item, itemClickListener);

        // Set font size for the title and description
        FontSizeUtils.applyFontSize(((SeeAllBookMarkedCoursesViewHolder) holder).binding.tvCourseDescription, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(((SeeAllBookMarkedCoursesViewHolder) holder).binding.tvCourseTitle, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(((SeeAllBookMarkedCoursesViewHolder) holder).binding.tvCourseLesson, fontSizePrefManager.getFontSize());
    }

    public static class SeeAllBookMarkedCoursesViewHolder extends RecyclerView.ViewHolder {
        CardBookmarkedCourseSaBinding binding;

        public SeeAllBookMarkedCoursesViewHolder(CardBookmarkedCourseSaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("DefaultLocale")
        public void bind(Course item, ItemClickListener<Course> itemClickListener) {
            binding.tvCourseTitle.setText(item.getTitle());
            binding.tvCourseDescription.setText(item.getDescription());
            binding.tvCourseLesson.setText(String.format("%d lessons", item.getLessonCount()));
            Glide.with(itemView.getContext())
                    .load(item.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);


            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(item, null);
            });
        }
    }
}

