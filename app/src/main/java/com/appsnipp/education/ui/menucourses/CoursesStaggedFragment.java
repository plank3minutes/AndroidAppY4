package com.appsnipp.education.ui.menucourses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentCoursesStaggedBinding;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.utils.MyUtilsApp;
import com.appsnipp.education.ui.utils.helpers.GridSpacingItemDecoration;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CoursesStaggedFragment extends BaseFragment implements ItemClickListener<Course> {

    private FragmentCoursesStaggedBinding binding;
    private CourseRecyclerAdapter adapter;
    private CourseViewModel viewModel;
    private Handler debounceHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentCoursesStaggedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupViewModel();
        setupSearchView();

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Android"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("AI"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Other"));

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String type = tab.getText().toString();
                binding.edtSearch.setText("");
                viewModel.getCoursesByNameAndType("", type).observe(getViewLifecycleOwner(), courses -> {
                    adapter.setCourseCards(courses);
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvCourses.setLayoutManager(layoutManager);
        binding.rvCourses.addItemDecoration(new GridSpacingItemDecoration(2, 30, true, 0));

        adapter = new CourseRecyclerAdapter(requireContext(), null, this);
        binding.rvCourses.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        viewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            adapter.setCourseCards(courses);
        });
    }

    private void setupSearchView() {
        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = v.getText().toString();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                hideKeyboard();
                return true;
            }
            return false;
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không dùng
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Huỷ các callback cũ
                debounceHandler.removeCallbacks(searchRunnable);

                // Tạo callback mới
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    if (!query.isEmpty()) {
                        performSearch(query);
                    }
                };

                debounceHandler.postDelayed(searchRunnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void performSearch(String query) {
        String type = binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition()).getText().toString();
        if(!query.isEmpty()){
            viewModel.getCoursesByNameAndType(query,type).observe(getViewLifecycleOwner(), courses -> {
                adapter.setCourseCards(courses);
            });
        } else {
            viewModel.getCoursesByNameAndType("", type).observe(getViewLifecycleOwner(), courses -> {
                adapter.setCourseCards(courses);
            });
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && binding.edtSearch != null) {
            imm.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(Course course, ImageView imageView) {
        MyUtilsApp.showToast(requireContext(), course.getCourseTitle());
        Bundle args = new Bundle();
        args.putString("courseId", course.getId());
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_coursesStaggedFragment_to_courseDetailFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
