

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
import android.widget.LinearLayout;
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

public class CourseAnalysis extends BaseFragment{
    private ProgressBar courseProgressBar;
    private ProgressBar quizProgressBar;
    private RecyclerView courseCompletedRv;
    private RecyclerView courseInProgressRv;
    private RecyclerView courseNotJoinRv;
    private LinearLayout completedEmptyTv;
    private LinearLayout inProgressEmptyTv;
    private LinearLayout notJoinEmptyTv;
    private CourseStatViewModel viewModel;
    private CourseRepository courseRepository;
    private ProgressRepository progressRepository;
    private LessonStatusRepository lessonStatusRepository;

    private CourseStatAdapter completedAdapter, inProgressAdapter, notJoinAdapter;

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
        return inflater.inflate(R.layout.fragment_course_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
        initViewModel();
        loadData();
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

        completedAdapter   = new CourseStatAdapter(requireContext(), new ArrayList<>(), new ArrayList<>(), listener);
        inProgressAdapter  = new CourseStatAdapter(requireContext(), new ArrayList<>(), new ArrayList<>(), listener);
        notJoinAdapter     = new CourseStatAdapter(requireContext(), new ArrayList<>(), new ArrayList<>(), listener);

        courseCompletedRv.setAdapter(completedAdapter);
        courseInProgressRv.setAdapter(inProgressAdapter);
        courseNotJoinRv.setAdapter(notJoinAdapter);

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
                    courseCompletedRv.setVisibility(View.GONE);
                    completedEmptyTv.setVisibility(View.VISIBLE);
                } else {
                    courseCompletedRv.setVisibility(View.VISIBLE);
                    completedEmptyTv.setVisibility(View.GONE);
                    completedAdapter.updateData(courseStat.completedCourses, courseStat.completedProgress, listener);
                }

                if (courseStat.inProgressCourses.isEmpty()) {
                    courseInProgressRv.setVisibility(View.GONE);
                    inProgressEmptyTv.setVisibility(VISIBLE);
                } else {
                    courseInProgressRv.setVisibility(View.VISIBLE);
                    inProgressEmptyTv.setVisibility(View.GONE);
                    inProgressAdapter.updateData(courseStat.inProgressCourses, courseStat.inProgress, listener);
                }

                if (courseStat.notJoinCourses.isEmpty()) {
                    courseNotJoinRv.setVisibility(View.GONE);
                    notJoinEmptyTv.setVisibility(VISIBLE);
                } else {
                    courseNotJoinRv.setVisibility(View.VISIBLE);
                    notJoinEmptyTv.setVisibility(View.GONE);
                    notJoinAdapter.updateData(courseStat.notJoinCourses, courseStat.notJoinProgress, listener);
                }

                // Update progress bars with real data
                updateProgressBars(courseStat);
            }
        });
    }

    private static class QuizProgressHolder {
        int totalQuizzes = 0;
        int totalCompletedQuizzes = 0;
//        int totalInProgressQuizzes = 0;
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

            // Calculate quiz progress using CourseStat data
            QuizProgressHolder progressHolder = new QuizProgressHolder();

            // Count quizzes in completed courses
            for (Course course : courseStat.completedCourses) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getQuiz() != null) {
                        progressHolder.totalQuizzes++;
                        progressHolder.totalCompletedQuizzes++; // All quizzes in completed courses are completed
                    }
                }
            }

            // Count quizzes in not-joined courses (just for total count)
            for (Course course : courseStat.notJoinCourses) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getQuiz() != null) {
                        progressHolder.totalQuizzes++;
                    }
                }
            }

            // Initial progress bar update (will be updated again when in-progress courses are processed)
            updateQuizProgressBar(progressHolder);

            // Count quizzes in in-progress courses
            for (Course course : courseStat.inProgressCourses) {
                // Count quizzes in this course
                int courseQuizCount = 0;
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getQuiz() != null) {
                        courseQuizCount++;
                    }
                }

                progressHolder.totalQuizzes += courseQuizCount;
                
                lessonStatusRepository.getLessonStatusByCourseId(course.getId())
                    .observe(getViewLifecycleOwner(), lessonStatuses -> {
                        if (lessonStatuses != null) {
                            int completedQuizzes = 0;
//                            int inProgressQuizzes = 0;

                            for (LessonStatus status : lessonStatuses) {
                                if (status.getQuizScore() > 0) {
                                    if (status.isCompleted() || status.getQuizScore() == 100) {
                                        completedQuizzes++;
                                    }
//                                    else {
//                                        inProgressQuizzes++;
//                                    }
                                }
                            }

                            // Update progress holder
                            progressHolder.totalCompletedQuizzes += completedQuizzes;
//                            progressHolder.totalInProgressQuizzes += inProgressQuizzes;

                            // Update progress bar
                            updateQuizProgressBar(progressHolder);
                        }
                    });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateQuizProgressBar(QuizProgressHolder holder) {
        if (holder.totalQuizzes > 0) {
            int completedPercentage = (holder.totalCompletedQuizzes * 100) / holder.totalQuizzes;
//            int inProgressPercentage = (holder.totalInProgressQuizzes * 100) / holder.totalQuizzes;

            quizProgressBar.setMax(100);
            quizProgressBar.setProgress(completedPercentage);
//            quizProgressBar.setSecondaryProgress(completedPercentage + inProgressPercentage);
        }
    }
}