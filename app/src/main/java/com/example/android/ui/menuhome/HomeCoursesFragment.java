package com.example.android.ui.menuhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.R;
import com.example.android.data.CourseCardsFake;
import com.example.android.databinding.FragmentHomeCoursesBinding;
import com.example.android.ui.model.CourseCard;
import com.example.android.ui.utils.MyUtilsApp;

import java.util.List;

public class HomeCoursesFragment extends Fragment
        implements PopularCoursesAdapter.ClickListener {

    FragmentHomeCoursesBinding binding;

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

        List<CourseCard> courseCardsList;
        courseCardsList = CourseCardsFake.getInstance().getCourseCards();

        popularCoursesAdapter.setListDataItems(courseCardsList);
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
    public void onClick(CourseCard view, int position) {
        MyUtilsApp.showToast(requireContext(), view.getCourseTitle());
    }
}