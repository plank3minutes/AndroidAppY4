/*
 * Copyright (c) 2021. rogergcc
 */

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentCoursesStaggedBinding;
import com.appsnipp.education.ui.listeners.ItemClickListener;
import com.appsnipp.education.ui.menusearch.CoursesViewModel;
import com.appsnipp.education.ui.model.CourseCard;
import com.appsnipp.education.ui.utils.MyUtilsApp;
import com.appsnipp.education.ui.utils.helpers.GridSpacingItemDecoration;

import java.util.List;


public class CoursesStaggedFragment extends Fragment
        implements ItemClickListener<CourseCard> {

    FragmentCoursesStaggedBinding binding;
    private Context mcontext;
    private CoursesViewModel viewModel;

    public CoursesStaggedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCoursesStaggedBinding.inflate(getLayoutInflater());
        mcontext = this.getContext();
        View view = binding.getRoot();

        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                //For this example only use seach option
                //U can use a other view with activityresult
                performSearch();
                Toast.makeText(mcontext,
                        "Edt Searching Click: " + binding.edtSearch.getText().toString().trim(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL);

        binding.rvCourses.setLayoutManager(
                layoutManager
        );
        binding.rvCourses.setClipToPadding(false);
        binding.rvCourses.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        binding.rvCourses.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        CourseRecyclerAdapter adapter = new CourseRecyclerAdapter(mcontext, null, this);
        binding.rvCourses.setAdapter(adapter);

        // Tạo và thiết lập ViewModel
        viewModel = new ViewModelProvider(this, new CoursesViewModel.MyCoursesViewModelFactory(requireContext())).get(CoursesViewModel.class);
        
        // Lấy dữ liệu từ ViewModel
        viewModel.fetchCourseCards();
        viewModel.courseCards().observe(getViewLifecycleOwner(), courseCards -> {
            adapter.setCourseCards(courseCards);
        });

        return view;
    }

    private void performSearch() {
        binding.edtSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
        //...perform search
    }

    @Override
    public void onItemClick(CourseCard item, ImageView imageView) {
        // Sử dụng item.getId() đã là String
        String courseId = item.getId();
        
        // Cách 1: Sử dụng NavHostFragment để điều hướng
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_coursesStaggedFragment_to_courseDetailFragment, args);
            
        // Cách 2: Sử dụng MainActivity để điều hướng (bỏ comment nếu muốn dùng)
        // if (getActivity() instanceof MainActivity) {
        //     ((MainActivity) getActivity()).navigateToCourseDetail(courseId);
        // }
    }
}