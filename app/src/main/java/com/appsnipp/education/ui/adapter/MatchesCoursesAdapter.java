package com.appsnipp.education.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.databinding.ItemShopCardBinding;
import com.appsnipp.education.ui.listeners.MatchCourseClickListener;
import com.appsnipp.education.ui.model.Course;
import com.bumptech.glide.Glide;

import java.util.List;

public class MatchesCoursesAdapter extends RecyclerView.Adapter<MatchesCoursesAdapter.ViewHolder> {

    private final List<Course> mData;
    private final MatchCourseClickListener matchCourseClickListener;

    public MatchesCoursesAdapter(List<Course> mData, MatchCourseClickListener listener) {
        this.mData = mData;
        this.matchCourseClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShopCardBinding itemCardBinding = ItemShopCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setBind(mData.get(position));
        holder.itemView.setOnClickListener(v -> matchCourseClickListener.onScrollPagerItemClick(
            mData.get(holder.getAdapterPosition()), 
            holder.itemCardBinding.image
        ));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemShopCardBinding itemCardBinding;

        public ViewHolder(@NonNull ItemShopCardBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;
        }

        void setBind(Course course) {
            itemCardBinding.tvTitulo.setText(course.getName());
            itemCardBinding.tvCantidadCursos.setText(course.getLessonCount() + " bài học");

            Glide.with(itemView.getContext())
                .load(course.getImageResource())
                .into(itemCardBinding.image);
        }
    }
}
