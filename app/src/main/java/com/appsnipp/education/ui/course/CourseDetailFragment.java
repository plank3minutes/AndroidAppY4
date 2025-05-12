/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentCourseDetailBinding;
import com.appsnipp.education.ui.adapter.LessonAdapter;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseDetailFragment extends Fragment {

    private FragmentCourseDetailBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private String courseId;
    private Course currentCourse;
    private LessonAdapter lessonAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCourseDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
        }

        setupToolbar();
        setupViewModels();
        setupRecyclerView();
        setupButtonListeners();
        observeCourseData();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
    }

    private void setupRecyclerView() {
        lessonAdapter = new LessonAdapter(new ArrayList<>(), lessonListener);
        binding.recyclerViewLessons.setAdapter(lessonAdapter);
        binding.recyclerViewLessons.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupButtonListeners() {
        binding.buttonContinue.setOnClickListener(v -> {
            // Xử lý sự kiện khi nhấn nút Continue
            if (currentCourse != null && !currentCourse.getLessons().isEmpty()) {
                Lesson nextLesson = findNextIncompleteLesson();
                if (nextLesson != null) {
                    navigateToLesson(nextLesson);
                }
            }
        });

        binding.fabBookmarkCourse.setOnClickListener(v -> {
            // Xử lý sự kiện khi nhấn nút Bookmark
            toggleBookmark();
        });
    }

    private Lesson findNextIncompleteLesson() {
        if (currentCourse == null) return null;
        
        for (Lesson lesson : currentCourse.getLessons()) {
            // Kiểm tra trạng thái hoàn thành của bài học
            if (!isLessonCompleted(lesson.getId())) {
                return lesson;
            }
        }
        return null;
    }

    private boolean isLessonCompleted(String lessonId) {
        // TODO: Implement kiểm tra trạng thái hoàn thành của bài học
        return false;
    }

    private void navigateToLesson(Lesson lesson) {
        Bundle args = new Bundle();
        args.putString("lessonId", lesson.getId());
        args.putString("courseId", courseId);
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_courseDetailFragment_to_lessonDetailFragment, args);
    }

    private void toggleBookmark() {
        // TODO: Implement chức năng bookmark
    }

    private void observeCourseData() {
        if (courseId != null) {
            courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
                if (course != null) {
                    currentCourse = course;
                    updateUI(course);
                }
            });

            progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
                if (progress != null) {
                    updateProgressUI(progress);
                } else {
                    UserProgress newProgress = new UserProgress(courseId, currentCourse.getLessonCount(), 0, false, new Date());
                    progressViewModel.insert(newProgress);
                }
            });
        }
    }

    private void updateUI(Course course) {
        binding.textCourseTitle.setText(course.getTitle());
        binding.textCourseDescription.setText(course.getDescription());
        binding.imageCourse.setImageResource(course.getImageResource());
        binding.textInstructor.setText(course.getLessonCount() + " lessons");
        binding.textLessonCount.setText(course.getLessonCount() + " lessons");
        
        lessonAdapter.updateLessons(course.getLessons());
        binding.collapsingToolbar.setTitle(course.getTitle());
    }

    private void updateProgressUI(UserProgress progress) {
        int completionPercentage = 0;
        if (currentCourse != null && currentCourse.getLessonCount() > 0) {
            completionPercentage = (progress.getCompletedLessons() * 100) / currentCourse.getLessonCount();
        }
        
        binding.progressBarCourse.setProgress(completionPercentage);
        binding.textProgress.setText(completionPercentage + "% completed");
    }

    private final LessonAdapter.LessonListener lessonListener = new LessonAdapter.LessonListener() {
        @Override
        public void onLessonClicked(Lesson lesson, int position) {
            navigateToLesson(lesson);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 