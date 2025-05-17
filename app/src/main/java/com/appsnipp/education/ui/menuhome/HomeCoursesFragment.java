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

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentHomeCoursesBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.utils.MyUtilsApp;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class HomeCoursesFragment extends Fragment implements ItemClickListener<Course> {

    private static final String TAG = "HomeCoursesFragment";
    private FragmentHomeCoursesBinding binding;
    private PopularCoursesAdapter popularCoursesAdapter;
    private JoinedCoursesAdapter joinedCoursesAdapter;
    private BookmarkedCoursesAdapter bookmarkedCoursesAdapter;
    private ProgressViewModel progressViewModel;
    private CourseViewModel viewModel;

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

        joinedCoursesAdapter = new JoinedCoursesAdapter(
                requireContext(),
                null,
                this
        );

        bookmarkedCoursesAdapter = new BookmarkedCoursesAdapter(
                requireContext(),
                null,
                this
        );

        binding.rvPopularCourses.setAdapter(popularCoursesAdapter);
        binding.rvJoinedCourses.setAdapter(joinedCoursesAdapter);
        binding.rvBookmarkedCourses.setAdapter(bookmarkedCoursesAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        viewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            popularCoursesAdapter.setListDataItems(courses);
        });

        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);

        progressViewModel.getAllUserProgress().observe(getViewLifecycleOwner(), progresses -> {
            if (progresses != null && !progresses.isEmpty()) {
                List<Course> joinedCourses = progresses.stream().filter(o -> o.getCompletedLessons() > 0).map(o -> viewModel.getCourseById(o.getCourseId()).getValue()).collect(Collectors.toList());
                List<Course> markedCourses = progresses.stream().filter(UserProgress::isMarked).map(o -> viewModel.getCourseById(o.getCourseId()).getValue()).collect(Collectors.toList());

                if (!markedCourses.isEmpty()) {
                    binding.vBookmarkedCourses.setVisibility(View.VISIBLE);
                    bookmarkedCoursesAdapter.setListDataItems(markedCourses);
                }

                if (!joinedCourses.isEmpty()) {
                    joinedCoursesAdapter.setListDataItems(joinedCourses);
                    binding.vJoinedCourses.setVisibility(View.VISIBLE);
                }

                binding.vJoined.setVisibility(View.VISIBLE);
                binding.vHaventJoined.setVisibility(View.GONE);
                binding.vPopularCourses.setVisibility(View.GONE);
                binding.vExploreCourses.setVisibility(View.GONE);
            } else {
                binding.vBookmarkedCourses.setVisibility(View.GONE);
                binding.vJoinedCourses.setVisibility(View.GONE);
                binding.vJoined.setVisibility(View.GONE);
                binding.vHaventJoined.setVisibility(View.VISIBLE);
                binding.vPopularCourses.setVisibility(View.VISIBLE);
                binding.vExploreCourses.setVisibility(View.VISIBLE);
            }
        });

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

        binding.tvHomeCourse.setText("Continue with\n" + currentCourse.getCourseTitle());
        binding.pbHomeCourse.setProgress(completionPercentage);
        binding.tvPercentage.setText(completionPercentage + "% " + getString(R.string.completed));
        binding.latestLearning.setOnClickListener(a -> {
            onItemClick(currentCourse, null);
        });
    }

    @Override
    public void onItemClick(Course course, ImageView imageView) {
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