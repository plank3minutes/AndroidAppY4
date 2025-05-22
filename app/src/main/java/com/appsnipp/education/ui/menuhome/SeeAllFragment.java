package com.appsnipp.education.ui.menuhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentSeeAllBinding;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class SeeAllFragment extends BaseFragment implements ItemClickListener<Course> {
    private SeeAllBookmarkedCoursesAdapter bookmarkedCoursesAdapter;
    private SeeAllJoinedCoursesAdapter joinedCoursesAdapter;
    private FragmentSeeAllBinding binding;
    private ProgressViewModel progressViewModel;
    private CourseViewModel courseViewModel;
    private SeeAllType viewType;

    private Handler handler = new Handler();
    private Runnable searchRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeeAllBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            viewType = (SeeAllType) getArguments().get("viewType");
        }

        setupToolbar();
        setupRecyclerViewAndSearchBar();
        setupModel();
    }

    @SuppressLint("DefaultLocale")
    private void setupModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);

        courseViewModel.getFilteredCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses == null) {
                return;
            }

            progressViewModel.getAllUserProgress().observe(getViewLifecycleOwner(), progresses -> {
                if (progresses == null) {
                    return;
                }

                boolean isQuerying = !binding.etSearch.getText().toString().trim().isEmpty();

                if (viewType == SeeAllType.JOINED) {
                    List<UserProgress> joinedCourses = progresses.stream()
                            .filter(o -> o.getCompletedLessons() > 0)
                            .collect(Collectors.toList());

                    binding.toolbar.setSubtitle(String.format("%d Joined Courses", joinedCourses.size()));

                    List<Pair<Course, UserProgress>> filteredJoinedCourses = joinedCourses.stream()
                            .filter(o -> courses.contains(courseViewModel.getCourseById(o.getCourseId()).getValue()))
                            .map(o -> Pair.create(courseViewModel.getCourseById(o.getCourseId()).getValue(), o))
                            .collect(Collectors.toList());

                    if (filteredJoinedCourses.isEmpty()) {
                        binding.rvCourses.setVisibility(View.GONE);
                        binding.vInfo.setVisibility(View.VISIBLE);

                        if (isQuerying) {
                            binding.tvInfo.setText("No courses found");
                            binding.tvSubInfo.setText("Try searching for something else, or check out our catalog");
                        } else {
                            binding.tvInfo.setText(R.string.no_courses_yet);
                            binding.tvSubInfo.setText(R.string.explore_our_catalog_and_enroll_in_your_first_course);
                        }
                    } else {
                        binding.rvCourses.setVisibility(View.VISIBLE);
                        binding.vInfo.setVisibility(View.GONE);
                        joinedCoursesAdapter.setListDataItems(filteredJoinedCourses);
                    }
                } else {
                    List<UserProgress> markedCourses = progresses.stream()
                            .filter(UserProgress::isMarked)
                            .collect(Collectors.toList());

                    binding.toolbar.setSubtitle(String.format("%d Bookmarked Courses", markedCourses.size()));

                    List<Course> filteredMarkedCourses = progresses.stream()
                            .filter(o -> o.isMarked() && courses.contains(courseViewModel.getCourseById(o.getCourseId()).getValue()))
                            .map(o -> courseViewModel.getCourseById(o.getCourseId()).getValue())
                            .collect(Collectors.toList());

                    if (filteredMarkedCourses.isEmpty()) {
                        binding.rvCourses.setVisibility(View.GONE);
                        binding.vInfo.setVisibility(View.VISIBLE);

                        if (isQuerying) {
                            binding.tvInfo.setText("No bookmarked courses found");
                            binding.tvSubInfo.setText("Try searching for something else, or check out our catalog");
                        } else {
                            binding.tvInfo.setText(R.string.no_bookmarked_courses_yet);
                            binding.tvSubInfo.setText(R.string.browse_our_catalog_and_bookmark_courses_you_re_interested_in);
                        }
                    } else {
                        binding.rvCourses.setVisibility(View.VISIBLE);
                        binding.vInfo.setVisibility(View.GONE);
                        bookmarkedCoursesAdapter.setListDataItems(filteredMarkedCourses);
                    }
                }
            });
        });
    }

    private void setupToolbar() {
        binding.toolbar.setTitle(getString(viewType == SeeAllType.JOINED ? R.string.all_joined_courses : R.string.all_bookmarked_courses));
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void setupRecyclerViewAndSearchBar() {
        if (viewType == SeeAllType.JOINED) {
            joinedCoursesAdapter = new SeeAllJoinedCoursesAdapter(
                    getContext(),
                    null,
                    this);

            binding.ivInfoIcon.setImageDrawable(
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_course, requireContext().getTheme())
            );
            binding.rvCourses.setAdapter(joinedCoursesAdapter);
        } else {
            bookmarkedCoursesAdapter = new SeeAllBookmarkedCoursesAdapter(
                    getContext(),
                    null,
                    this);

            binding.ivInfoIcon.setImageDrawable(
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark, requireContext().getTheme())
            );
            binding.rvCourses.setAdapter(bookmarkedCoursesAdapter);
        }

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    String query = s.toString().toLowerCase();
                    courseViewModel.search(query);
                };

                handler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });
    }

    @Override
    public void onItemClick(Course course, ImageView _imageView) {
        Bundle args = new Bundle();
        args.putString("courseId", course.getId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_seeAllFragment_to_courseDetailFragment, args);
    }

    @Override
    public void onResume() {
        super.onResume();

        courseViewModel.getFilteredCourses().setValue(courseViewModel.getAllCourses().getValue());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}