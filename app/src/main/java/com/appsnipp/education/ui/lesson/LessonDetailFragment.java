/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.lesson;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentLessonDetailBinding;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.Date;
import java.util.List;

public class LessonDetailFragment extends Fragment {

    private FragmentLessonDetailBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private String lessonId;
    private String courseId;
    private Lesson currentLesson;
    private Course currentCourse;
    private int lessonIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLessonDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get arguments
        if (getArguments() != null) {
            lessonId = getArguments().getString("lessonId");
            courseId = getArguments().getString("courseId");
        }

        setupViewModels();
        observeData();
        setupButtonListeners();
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
    }

    private void observeData() {
        if (courseId != null) {
            courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
                if (course != null) {
                    currentCourse = course;
                    findCurrentLesson(course.getLessons());
                }
            });
        }
    }

    private void findCurrentLesson(List<Lesson> lessons) {
        if (lessonId != null && lessons != null) {
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                if (lesson.getId().equals(lessonId)) {
                    currentLesson = lesson;
                    lessonIndex = i;
                    setupLessonContent(lesson);
                    break;
                }
            }
        }
    }

    private void setupLessonContent(Lesson lesson) {
        binding.textLessonTitle.setText(lesson.getTitle());
        
        // Setup WebView to display content
        binding.webViewLessonContent.setWebViewClient(new WebViewClient());
        String htmlContent = "<html><body style='text-align:justify'>" + lesson.getContent() + "</body></html>";
        binding.webViewLessonContent.loadData(htmlContent, "text/html", "UTF-8");
        
        // Setup video if available
        if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
            binding.videoViewLesson.setVisibility(View.VISIBLE);
            setupVideoPlayer(lesson.getVideoUrl());
        } else {
            binding.videoViewLesson.setVisibility(View.GONE);
        }

    }

    private void setupVideoPlayer(String videoUrl) {
        try {
            MediaController mediaController = new MediaController(requireContext());
            mediaController.setAnchorView(binding.videoViewLesson);
            
            Uri videoUri = Uri.parse(videoUrl);
            binding.videoViewLesson.setMediaController(mediaController);
            binding.videoViewLesson.setVideoURI(videoUri);
            binding.videoViewLesson.requestFocus();
            
            // Auto-play disabled to avoid unexpected behaviors
            // binding.videoViewLesson.start();
        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.video_error), Toast.LENGTH_SHORT).show();
            binding.videoViewLesson.setVisibility(View.GONE);
        }
    }


    private void setupButtonListeners() {
        binding.buttonCompleteLesson.setOnClickListener(v -> {
            markLessonAsComplete();
        });
    }

    private void markLessonAsComplete() {
        if (currentCourse != null && currentLesson != null) {
            progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
                // Update only if new lesson is further in the course
                if (progress != null) {
                    if (lessonIndex >= progress.getLessonIndex()) {
                        progress.setLessonIndex(lessonIndex + 1);
                        progress.setLastAccess(new Date());
                        progressViewModel.update(progress);
                        Toast.makeText(requireContext(), getString(R.string.lesson_completed), Toast.LENGTH_SHORT).show();
                        
                        // Quay lại màn hình trước đó sau khi hoàn thành bài học
                        NavHostFragment.findNavController(this).popBackStack();
                    }
                } else {
                    // If no progress record exists, create one
                    UserProgress newProgress = new UserProgress(courseId, lessonIndex + 1, 0, new Date());
                    progressViewModel.insert(newProgress);
                    Toast.makeText(requireContext(), getString(R.string.lesson_completed), Toast.LENGTH_SHORT).show();
                    
                    // Quay lại màn hình trước đó sau khi hoàn thành bài học
                    NavHostFragment.findNavController(this).popBackStack();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 