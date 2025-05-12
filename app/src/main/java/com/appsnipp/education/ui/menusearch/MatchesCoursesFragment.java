package com.appsnipp.education.ui.menusearch;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.education.databinding.FragmentMatchesCoursesBinding;

public class MatchesCoursesFragment extends Fragment {

    private static final String TAG = "MatchesCoursesFragment";
    FragmentMatchesCoursesBinding binding;
    Context mcontext;

    public MatchesCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatchesCoursesBinding.inflate(inflater, container, false);
        mcontext = requireContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Comment toàn bộ logic để chỉ hiển thị giao diện
        // setUpAdapters();

        // viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        // viewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
        //     courseTopicsViewPager.setListDataItems(courses);
        //     setupViewpager(1, courses);
        //     binding.rvPopularCourses.setAdapter(popularCoursesAdapter);
        //     popularCoursesAdapter.setListDataItems(courses);
        // });

        // binding.rvPopularCourses.setHasFixedSize(true);
        // binding.rvPopularCourses.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));

        // setupButtons();
    }

    // private void setupButtons() {
    //     binding.btnQuizResult.setOnClickListener(v -> {
    //         AppLogger.d("[" + TAG + "] Clicked Quiz Result");
    //         NavHostFragment.findNavController(this)
    //                 .navigate(R.id.action_matchesCoursesFragment_to_quizResultFragment);
    //     });

    //     binding.btnJoinedCourses.setOnClickListener(v -> {
    //         AppLogger.d("[" + TAG + "] Clicked Joined Courses");
    //         NavHostFragment.findNavController(this)
    //                 .navigate(R.id.action_matchesCoursesFragment_to_joinedCoursesFragment);
    //     });

    //     binding.btnSettings.setOnClickListener(v -> {
    //         AppLogger.d("[" + TAG + "] Clicked Settings");
    //         NavHostFragment.findNavController(this)
    //                 .navigate(R.id.action_matchesCoursesFragment_to_settingsFragment);
    //     });
    // }

    // private void setUpAdapters() {
    //     popularCoursesAdapter = new CoursesAdapter((course, position) -> {
    //         AppLogger.d("[" + TAG + "] lambda CLick CoursesAdapter " + course.getCourseTitle());
    //         String courseId = course.getId();
    //         Bundle args = new Bundle();
    //         args.putString("courseId", courseId);
    //         NavHostFragment.findNavController(this)
    //                 .navigate(R.id.action_matchesCoursesFragment_to_courseDetailFragment, args);
    //     });

    //     courseTopicsViewPager = new CourseTopicsViewPager((course, imageView) -> {
    //         AppLogger.d("[" + TAG + "] lambda CLick CourseTopicsViewPager () " + course);
    //         String courseId = course.getId();
    //         Bundle args = new Bundle();
    //         args.putString("courseId", courseId);
    //         NavHostFragment.findNavController(this)
    //                 .navigate(R.id.action_matchesCoursesFragment_to_courseDetailFragment, args);
    //     });
    // }

    // private void setupAdapter(int currentItem) {
    //     binding.viewPager.setAdapter(courseTopicsViewPager);
    //     binding.viewPager.setCurrentItem(currentItem);
    //     binding.viewPager.setOffscreenPageLimit(1);
    // }

    // private void setupPageTransformer() {
    //     binding.viewPager.setPageTransformer((page, position) -> {
    //         float absPosition = Math.abs(position);
    //         page.setScaleY(1 - (0.3f * absPosition));
    //         page.setAlpha(1 - (0.5f * absPosition));
    //     });
    // }

    // private void setupItemDecoration() {
    //     binding.viewPager.addItemDecoration(new HorizontalMarginItemDecoration(
    //             mcontext, R.dimen.viewpager_current_item_horizontal_margin,
    //             R.dimen.viewpager_next_item_visible
    //     ));
    // }

    // private void setupPageChangeCallback(List<Course> courseList) {
    //     binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
    //         @Override
    //         public void onPageSelected(int position) {
    //             super.onPageSelected(position);
    //             MyUtilsApp.showToast(mcontext, courseList.get(position).getName());
    //             String positionSelected = String.format(Locale.ENGLISH, "%d/%d", position + 1, courseList.size());
    //             AppLogger.d("[" + TAG + "] " + positionSelected);
    //             int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
    //             binding.containerConstraint.setBackgroundColor(color);
    //         }
    //     });
    // }

    // private void setupViewpager(int currentItem, List<Course> courseList) {
    //     setupAdapter(currentItem);
    //     setupPageTransformer();
    //     setupItemDecoration();
    //     setupPageChangeCallback(courseList);
    // }
}