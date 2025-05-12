/*
 * Copyright (c) 2021. rogergcc
 */

package com.appsnipp.education.ui.menuhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentHomeCoursesBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.MyUtilsApp;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.Date;

public class HomeCoursesFragment extends Fragment implements ItemClickListener<Course> {

    private FragmentHomeCoursesBinding binding;
    private PopularCoursesAdapter popularCoursesAdapter;
    private ProgressViewModel progressViewModel;
    private TutorialsAdapter tutorialsAdapter;
    private CourseViewModel viewModel;
    private static final String TAG = "HomeCoursesFragment";

    public HomeCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentHomeCoursesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews();
        setupViewModel();
    }

    private void setupRecyclerViews() {
        popularCoursesAdapter = new PopularCoursesAdapter(
            requireContext(),
            null,
            this
        );
//        tutorialsAdapter = new TutorialsAdapter(this);

        binding.rvPopularCourses.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvPopularCourses.setAdapter(popularCoursesAdapter);


// Dont use it rn
//        binding.rvTutorials.setLayoutManager(
//            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        );
//        binding.rvTutorials.setAdapter(tutorialsAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        viewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            popularCoursesAdapter.setListDataItems(courses);
//            tutorialsAdapter.setListDataItems(courses);
        });

        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);


        // Observe progress data
        progressViewModel.getLatestUserProgress().observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                viewModel.getCourseById(progress.getCourseId()).observe(getViewLifecycleOwner(), course -> {
                    // Cập nhật UI với dữ liệu progress
                    updateProgressUI(progress, course);
                });
            } else {
                MyUtilsApp.showToast(requireContext(), "No progress data available");
            }
        });
    }

    private void updateProgressUI(UserProgress progress, Course currentCourse) {
        int completionPercentage = 0;
        if (currentCourse != null && currentCourse.getLessons().size() > 0) {
            completionPercentage = (progress.getCompletedLessons() * 100) / currentCourse.getLessons().size();
        }

        binding.tvHomeCourse.setText("Continue to " + currentCourse.getCourseTitle());
        binding.pbHomeCourse.setProgress(completionPercentage);
        binding.tvPercentage.setText(completionPercentage + "% " + getString(R.string.completed));

    }

    @Override
    public void onItemClick(Course course, ImageView imageView) {
        MyUtilsApp.showToast(requireContext(), course.getCourseTitle());
        Bundle args = new Bundle();
        args.putString("courseId", course.getId());
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeCoursesFragment_to_courseDetailFragment, args);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}