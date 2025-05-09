/*
 * Copyright (c) 2021. rogergcc
 */

package com.appsnipp.education.ui.menuhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appsnipp.education.MainActivity;
import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentHomeCoursesBinding;
import com.appsnipp.education.ui.model.CourseCard;
import com.appsnipp.education.ui.menusearch.CoursesViewModel;
import com.appsnipp.education.ui.utils.MyUtilsApp;

import java.util.List;

public class HomeCoursesFragment extends Fragment
        implements PopularCoursesAdapter.ClickListener {

    FragmentHomeCoursesBinding binding;
    private CoursesViewModel viewModel;
    private static final String TAG = "HomeCoursesFragment";

    public HomeCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setUpUI() {
        String percentage = getResources().getString(R.string.percentage_course, 75);
        binding.tvPercentage.setText(percentage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvPopularCourses.setLayoutManager(layoutManager);
        binding.rvPopularCourses.hasFixedSize();

        PopularCoursesAdapter popularCoursesAdapter = new PopularCoursesAdapter(this);
        binding.rvPopularCourses.setAdapter(popularCoursesAdapter);

        // Tạo và thiết lập ViewModel
        viewModel = new ViewModelProvider(this, new CoursesViewModel.MyCoursesViewModelFactory(requireContext())).get(CoursesViewModel.class);
        
        // Lấy dữ liệu từ ViewModel
        viewModel.fetchCourseCards();
        viewModel.courseCards().observe(getViewLifecycleOwner(), courseCards -> {
            popularCoursesAdapter.setListDataItems(courseCards);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeCoursesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setUpUI();
        return view;
    }

    @Override
    public void onClick(CourseCard courseCard, int position) {
        // Sử dụng courseCard.getId() đã là String
        String courseId = courseCard.getId();
        
        // Cách 1: Sử dụng NavHostFragment để điều hướng
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeCoursesFragment_to_courseDetailFragment, args);
        
        // Cách 2: Sử dụng MainActivity để điều hướng (bỏ comment nếu muốn dùng)
        // Cách này sẽ sử dụng phương thức hỗ trợ từ MainActivity
        // if (getActivity() instanceof MainActivity) {
        //     ((MainActivity) getActivity()).navigateToCourseDetail(courseId);
        // }
    }
}