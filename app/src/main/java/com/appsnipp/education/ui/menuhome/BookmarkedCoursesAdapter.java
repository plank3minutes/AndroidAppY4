package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.CardBookmarkedCourseBinding;
import com.appsnipp.education.databinding.CardSeeAllSmallBinding;
import com.appsnipp.education.ui.listeners.HomeCourseItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BookmarkedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COURSE = 0;
    private static final int VIEW_TYPE_SEE_ALL = 1;
    private static final int MAX_ITEMS_WITH_SEE_ALL = 4;

    private final HomeCourseItemClickListener itemClickListener;
    private List<Course> items;
    private final Context context;
    private final FontSizePrefManager fontSizePrefManager;


    public BookmarkedCoursesAdapter(Context context, List<Course> items, HomeCourseItemClickListener listener) {
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
        return items == null ? 0 : items.size() > 3 ? MAX_ITEMS_WITH_SEE_ALL : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == MAX_ITEMS_WITH_SEE_ALL - 1) {
            return VIEW_TYPE_SEE_ALL; // Last item is "See All"
        } else {
            return VIEW_TYPE_COURSE;   // Normal course item
        }
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SEE_ALL) {
            CardSeeAllSmallBinding binding = CardSeeAllSmallBinding.inflate(inflater, parent, false);
            return new SeeAllViewHolder(binding);
        } else {
            CardBookmarkedCourseBinding binding = CardBookmarkedCourseBinding.inflate(inflater, parent, false);
            return new BookmarkedCourseViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == MAX_ITEMS_WITH_SEE_ALL - 1) {
            ((SeeAllViewHolder) holder).bind(itemClickListener);
            FontSizeUtils.applyFontSize(((SeeAllViewHolder) holder).binding.tvSeeAll, fontSizePrefManager.getFontSize());

        } else {
            Course item = items.get(position);
            ((BookmarkedCourseViewHolder) holder).bind(item, itemClickListener);
            FontSizeUtils.applyFontSize(((BookmarkedCourseViewHolder) holder).binding.tvCourseTitle, fontSizePrefManager.getFontSize());

        }

    }

    public static class SeeAllViewHolder extends RecyclerView.ViewHolder {
        CardSeeAllSmallBinding binding;

        public SeeAllViewHolder(CardSeeAllSmallBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HomeCourseItemClickListener itemClickListener) {
            itemView.setOnClickListener(v -> itemClickListener.onSeeAllClick(SeeAllType.BOOKMARKED));
        }
    }

    public static class BookmarkedCourseViewHolder extends RecyclerView.ViewHolder {
        private final CardBookmarkedCourseBinding binding;

        public BookmarkedCourseViewHolder(CardBookmarkedCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Course item, HomeCourseItemClickListener itemClickListener) {
            binding.tvCourseTitle.setText(item.getCourseTitle());
            Glide.with(itemView.getContext())
                    .load(item.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);

            itemView.setOnClickListener(v -> {
                itemClickListener.onCourseItemClick(item);
            });
        }
    }
}
