/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.data.repository.CourseRepository;
import com.appsnipp.education.data.repository.LessonStatusRepository;
import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.ui.adapter.CourseStatAdapter;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.course.CourseDetailFragment;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.CourseStat;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.viewmodel.CourseStatViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link CourseAnalysis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseAnalysis extends BaseFragment{
    private ProgressBar courseProgressBar;
    private ProgressBar quizProgressBar;
    private RecyclerView courseCompletedRv;
    private RecyclerView courseInProgressRv;
    private RecyclerView courseNotJoinRv;
    private TextView completedEmptyTv;
    private TextView inProgressEmptyTv;
    private TextView notJoinEmptyTv;
    private CourseStatViewModel viewModel;
    private CourseRepository courseRepository;
    private ProgressRepository progressRepository;
    private LessonStatusRepository lessonStatusRepository;

    private final CourseStatAdapter.CourseStatListener listener = new CourseStatAdapter.CourseStatListener () {
        @Override
        public void onCourseStatClicked(String courseId) {
            Bundle bundle = new Bundle();
            bundle.putString("courseId", courseId);
            NavHostFragment.findNavController(CourseAnalysis.this)
                    .navigate(R.id.courseDetailFragment, bundle);
        }
    };

    public CourseAnalysis() {
        // Required empty public constructor
    }

    public static CourseAnalysis newInstance() {
        CourseAnalysis fragment = new CourseAnalysis();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_analysis, container, false);
        initializeViews(view);
        initViewModel();
        loadData();
        return view;
    }

    private void initializeViews(View view) {
        // Initialize progress bars
        courseProgressBar = view.findViewById(R.id.courseProgressBar);
        quizProgressBar = view.findViewById(R.id.quizProgressBar);

        // Set up back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        // Set the tint color to contentTextColor
        btnBack.setImageTintList(ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.contentTextColor)));
            
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        courseCompletedRv = view.findViewById(R.id.completed_course_rv);
        courseCompletedRv.setLayoutManager(linearLayoutManager1);
        courseInProgressRv = view.findViewById(R.id.in_progress_course_rv);
        courseInProgressRv.setLayoutManager(linearLayoutManager2);
        courseNotJoinRv = view.findViewById(R.id.not_join_course_rv);
        courseNotJoinRv.setLayoutManager(linearLayoutManager3);

        completedEmptyTv = view.findViewById(R.id.completed_course_empty_tv);
        inProgressEmptyTv = view.findViewById(R.id.in_progress_course_empty_tv);
        notJoinEmptyTv = view.findViewById(R.id.not_join_course_empty_tv);
    }

    private void initViewModel() {
        this.viewModel = new CourseStatViewModel(requireActivity().getApplication());
        this.courseRepository = CourseRepository.getInstance(requireContext());
        this.progressRepository = ProgressRepository.getInstance(requireContext());
        this.lessonStatusRepository = LessonStatusRepository.getInstance(requireContext());
    }

    private void loadData() {
        viewModel.getCourseStatLiveData().observe(getViewLifecycleOwner(), new Observer<CourseStat>() {
            @Override
            public void onChanged(CourseStat courseStat) {
                if (courseStat.completedCourses.isEmpty()) {
                    courseCompletedRv.setVisibility(INVISIBLE);
                    completedEmptyTv.setVisibility(VISIBLE);
                } else {
                    courseCompletedRv.setAdapter(new CourseStatAdapter(courseStat.completedCourses, courseStat.completedProgress, listener));
                }

                if (courseStat.inProgressCourses.isEmpty()) {
                    courseInProgressRv.setVisibility(INVISIBLE);
                    inProgressEmptyTv.setVisibility(VISIBLE);
                } else {
                    courseInProgressRv.setAdapter(new CourseStatAdapter(courseStat.inProgressCourses, courseStat.inProgress, listener));
                }

                if (courseStat.notJoinCourses.isEmpty()) {
                    courseNotJoinRv.setVisibility(INVISIBLE);
                    notJoinEmptyTv.setVisibility(VISIBLE);
                } else {
                    courseNotJoinRv.setAdapter(new CourseStatAdapter(courseStat.notJoinCourses, courseStat.notJoinProgress, listener));
                }

                // Update progress bars with real data
                updateProgressBars(courseStat);
            }
        });
    }

    private void updateProgressBars(CourseStat courseStat) {
        try {
            // Calculate course progress
            int totalCourses = courseStat.completedCourses.size() + 
                             courseStat.inProgressCourses.size() + 
                             courseStat.notJoinCourses.size();
            
            if (totalCourses > 0) {
                int completedPercentage = (courseStat.completedCourses.size() * 100) / totalCourses;
                int inProgressPercentage = (courseStat.inProgressCourses.size() * 100) / totalCourses;

                // Set course progress values
                courseProgressBar.setMax(100);
                courseProgressBar.setProgress(completedPercentage);
                courseProgressBar.setSecondaryProgress(completedPercentage + inProgressPercentage);
            }

            // Calculate quiz progress
            lessonStatusRepository.getQuizTaken().observe(getViewLifecycleOwner(), quizzesTaken -> {
                courseRepository.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
                    int totalQuizzes = 0;
                    for (Course course : courses) {
                        for (Lesson lesson : course.getLessons()) {
                            if (lesson.getQuiz() != null) {
                                totalQuizzes++;
                            }
                        }
                    }

                    if (totalQuizzes > 0 && quizzesTaken != null) {
                        int quizProgress = (quizzesTaken * 100) / totalQuizzes;
                        
                        // Set quiz progress values
                        quizProgressBar.setMax(100);
                        quizProgressBar.setProgress(quizProgress);
                        // We don't have a way to track in-progress quizzes, so we'll only show completed ones
                        quizProgressBar.setSecondaryProgress(quizProgress);
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}