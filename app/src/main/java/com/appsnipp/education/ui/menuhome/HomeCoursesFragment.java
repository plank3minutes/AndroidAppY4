package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentHomeCoursesBinding;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.listeners.HomeCourseItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.OnBottomNavTabSelected;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeCoursesFragment extends BaseFragment implements HomeCourseItemClickListener {
    private FragmentHomeCoursesBinding binding;
    private JoinedCoursesAdapter joinedCoursesAdapter;
    private BookmarkedCoursesAdapter bookmarkedCoursesAdapter;
    private ProgressViewModel progressViewModel;
    private CourseViewModel courseViewModel;

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
        joinedCoursesAdapter = new JoinedCoursesAdapter(
                getContext(),
                null,
                this
        );

        bookmarkedCoursesAdapter = new BookmarkedCoursesAdapter(
                getContext(),
                null,
                this
        );

        binding.rvJoinedCourses.setAdapter(joinedCoursesAdapter);
        binding.rvBookmarkedCourses.setAdapter(bookmarkedCoursesAdapter);
    }

    private void setupViewModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);

        progressViewModel.getAllUserProgress().observe(getViewLifecycleOwner(), progresses -> {
            if (progresses != null && !progresses.isEmpty()) {
                List<Pair<Course, UserProgress>> joinedCourses = progresses.stream()
                        .filter(o -> o.getCompletedLessons() > 0)
                        .map(o -> Pair.create(courseViewModel.getCourseById(o.getCourseId()).getValue(), o))
                        .collect(Collectors.toList());
                List<Course> markedCourses = progresses.stream()
                        .filter(UserProgress::isMarked)
                        .map(o -> courseViewModel.getCourseById(o.getCourseId()).getValue())
                        .collect(Collectors.toList());

                if (!markedCourses.isEmpty()) {
                    binding.rvBookmarkedCourses.setVisibility(View.VISIBLE);
                    binding.cvEmptyBookmarkedCourses.setVisibility(View.GONE);
                    bookmarkedCoursesAdapter.setListDataItems(markedCourses);
                } else {
                    binding.rvBookmarkedCourses.setVisibility(View.GONE);
                    binding.cvEmptyBookmarkedCourses.setVisibility(View.VISIBLE);
                }

                if (!joinedCourses.isEmpty()) {
                    binding.rvJoinedCourses.setVisibility(View.VISIBLE);
                    binding.cvEmptyJoinedCourses.setVisibility(View.GONE);
                    joinedCoursesAdapter.setListDataItems(joinedCourses);
                } else {
                    binding.rvJoinedCourses.setVisibility(View.GONE);
                    binding.cvEmptyJoinedCourses.setVisibility(View.VISIBLE);
                }

            } else {
                binding.rvJoinedCourses.setVisibility(View.GONE);
                binding.rvBookmarkedCourses.setVisibility(View.VISIBLE);
                binding.cvEmptyJoinedCourses.setVisibility(View.VISIBLE);
                binding.cvEmptyBookmarkedCourses.setVisibility(View.VISIBLE);
            }
        });

        progressViewModel.getLatestUserProgress().observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                courseViewModel.getCourseById(progress.getCourseId())
                        .observe(getViewLifecycleOwner(), course -> {
                            updateFeaturedCard(progress, course);
                        });
            } else {
                Course firstCourse = Objects.requireNonNull(courseViewModel.getAllCourses().getValue()).get(0);

                binding.tvFeaturedCourseTitle.setText(String.format("Start with %s", firstCourse.getCourseTitle()));
                binding.pbFeaturedCourse.setVisibility(View.GONE);
                binding.tvFeaturedProgressPercentage.setVisibility(View.GONE);
                binding.btnFeaturedCourse.setOnClickListener(v -> {
                    onCourseItemClick(firstCourse);
                });
            }
        });

        binding.seeAll1.setOnClickListener(v -> {
            onSeeAllClick(SeeAllType.JOINED);
        });

        binding.seeAll2.setOnClickListener(v -> {
            onSeeAllClick(SeeAllType.BOOKMARKED);
        });

        binding.btnExploreCourses1.setOnClickListener(v -> {
            ((OnBottomNavTabSelected) getActivity()).switchToTab(R.id.coursesStaggedFragment);
        });

        binding.btnExploreCourses2.setOnClickListener(v -> {
            ((OnBottomNavTabSelected) getActivity()).switchToTab(R.id.coursesStaggedFragment);
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateFeaturedCard(UserProgress progress, Course currentCourse) {
        if (currentCourse == null) return;

        int completionPercentage = 0;
        if (!currentCourse.getLessons().isEmpty()) {
            completionPercentage = (progress.getCompletedLessons() * 100) / currentCourse.getLessons().size();
        }

        binding.tvFeaturedCourseTitle.setText(String.format("Continue with %s", currentCourse.getCourseTitle()));
        binding.pbFeaturedCourse.setProgress(completionPercentage);
        binding.pbFeaturedCourse.setVisibility(View.VISIBLE);
        binding.tvFeaturedCourseSub.setVisibility(View.GONE);
        binding.tvFeaturedProgressPercentage.setText(String.format("%d%% %s", completionPercentage, getString(R.string.completed)));
        binding.tvFeaturedProgressPercentage.setVisibility(View.VISIBLE);
        binding.btnFeaturedCourse.setText(getText(R.string.continue_learning));
        binding.btnFeaturedCourse.setOnClickListener(a -> {
            onCourseItemClick(currentCourse);
        });
    }

    @Override
    public void onCourseItemClick(Course course) {
        NavDirections action = HomeCoursesFragmentDirections.actionHomeCoursesFragmentToCourseDetailFragment(course.getId());
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onSeeAllClick(SeeAllType type) {
        NavDirections action = HomeCoursesFragmentDirections.actionHomeCoursesFragmentToSeeAllFragment(type);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}