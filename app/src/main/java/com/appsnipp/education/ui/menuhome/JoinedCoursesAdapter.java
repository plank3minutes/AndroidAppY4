package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.CardJoinedCourseBinding;
import com.appsnipp.education.databinding.CardSeeAllBinding;
import com.appsnipp.education.ui.listeners.HomeCourseItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class JoinedCoursesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COURSE = 0;
    private static final int VIEW_TYPE_SEE_ALL = 1;
    private static final int MAX_ITEMS_WITH_SEE_ALL = 4;

    private final HomeCourseItemClickListener itemClickListener;
    private List<Pair<Course, UserProgress>> items;
    private final Context context;
    private final FontSizePrefManager fontSizePrefManager;

    public JoinedCoursesAdapter(Context context, List<Pair<Course, UserProgress>> items, HomeCourseItemClickListener listener) {
        this.items = items;
        this.itemClickListener = listener;
        this.context = context;
        this.fontSizePrefManager = new FontSizePrefManager(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Pair<Course, UserProgress>> listItems) {
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SEE_ALL) {
            CardSeeAllBinding binding = CardSeeAllBinding.inflate(inflater, parent, false);
            return new SeeAllViewHolder(binding);
        } else {
            CardJoinedCourseBinding binding = CardJoinedCourseBinding.inflate(inflater, parent, false);
            return new JoinedCourseViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == MAX_ITEMS_WITH_SEE_ALL - 1) {
            ((SeeAllViewHolder) holder).bind(itemClickListener);
            FontSizeUtils.applyFontSize(((SeeAllViewHolder) holder).binding.tvSeeAll, fontSizePrefManager.getFontSize());

        } else {
            Pair<Course, UserProgress> item = items.get(position);
            ((JoinedCourseViewHolder) holder).bind(item, itemClickListener);
            // Set font size for the course title
            FontSizeUtils.applyFontSize(((JoinedCourseViewHolder) holder).binding.tvCourseTitle, fontSizePrefManager.getFontSize());
            FontSizeUtils.applyFontSize(((JoinedCourseViewHolder) holder).binding.tvFeaturedProgressPercentage, fontSizePrefManager.getFontSize());
        }
    }

    public static class SeeAllViewHolder extends RecyclerView.ViewHolder {
        CardSeeAllBinding binding;

        public SeeAllViewHolder(CardSeeAllBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HomeCourseItemClickListener itemClickListener) {
            itemView.setOnClickListener(v -> itemClickListener.onSeeAllClick(SeeAllType.JOINED));
        }
    }

    public static class JoinedCourseViewHolder extends RecyclerView.ViewHolder {
        CardJoinedCourseBinding binding;

        public JoinedCourseViewHolder(CardJoinedCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pair<Course, UserProgress> item, HomeCourseItemClickListener itemClickListener) {
            Course course = item.first;
            UserProgress progress = item.second;

            int completionPercentage = (progress.getCompletedLessons() * 100) / course.getLessons().size();
            String progressText = completionPercentage + "% Completed";

            binding.tvCourseTitle.setText(course.getCourseTitle());
            binding.progressBar.setProgress(completionPercentage);
            binding.tvFeaturedProgressPercentage.setText(progressText);
            Glide.with(itemView.getContext())
                    .load(course.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);

            itemView.setOnClickListener(v -> {
                itemClickListener.onCourseItemClick(course);
            });
        }
    }
}
