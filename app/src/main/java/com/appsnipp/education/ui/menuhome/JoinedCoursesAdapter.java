package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.databinding.JoinedCourseCardBinding;
import com.appsnipp.education.ui.base.BaseViewHolder;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;


public class JoinedCoursesAdapter
        extends RecyclerView.Adapter<BaseViewHolder<Course>> {

    final Context mContext;
    private final ItemClickListener<Course> itemClickListener;
    private List<Course> mCoursesList;

    public JoinedCoursesAdapter(Context mContext, List<Course> mData, ItemClickListener<Course> listener) {
        this.mCoursesList = mData;
        this.mContext = mContext;
        this.itemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListDataItems(List<Course> listItems) {
        this.mCoursesList = listItems;
        notifyDataSetChanged();
        Logger.getLogger(JoinedCoursesAdapter.class.getName()).info("setListDataItems: " + listItems);
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
        JoinedCourseCardBinding binding = JoinedCourseCardBinding.inflate(inflater, viewGroup, false);

        return new ViewHolder(binding, ProgressRepository.getInstance(mContext));
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
        private final JoinedCourseCardBinding binding;
        private final ProgressRepository progressRepository;

        public ViewHolder(JoinedCourseCardBinding binding, ProgressRepository progressRepository) {
            super(binding.getRoot());
            this.binding = binding;
            this.progressRepository = progressRepository;
        }

        public JoinedCourseCardBinding getItemCardBinding() {
            return binding;
        }

        @Override
        public void bind(Course course) {
            List<UserProgress> progresses = progressRepository.getAllUserProgress().getValue();

            // TODO: Figure out why it not work with getUserProgressByCourseId
            UserProgress progress = progresses.stream()
                    .filter(p -> p.getCourseId().equals(course.getId()))
                    .findFirst()
                    .orElse(null);

            if (progress == null) {
                return;
            }

            int completionPercentage = (progress.getCompletedLessons() * 100) / course.getLessons().size();

            binding.tvCourseTitle.setText(course.getCourseTitle());
            binding.progressBar.setProgress(completionPercentage);
            binding.tvPercentage.setText(completionPercentage + "% Completed");
            Glide.with(itemView.getContext())
                    .load(course.getImageResource())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.imvCoursePhoto);
        }
    }
}
