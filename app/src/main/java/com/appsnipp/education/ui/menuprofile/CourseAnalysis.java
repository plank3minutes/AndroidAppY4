/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.base.BaseFragment;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link CourseAnalysis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseAnalysis extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar courseProgressBar;
    private ProgressBar quizProgressBar;

    public CourseAnalysis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseAnalysis.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseAnalysis newInstance(String param1, String param2) {
        CourseAnalysis fragment = new CourseAnalysis();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_analysis, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        // Initialize progress bars
        courseProgressBar = view.findViewById(R.id.courseProgressBar);
        quizProgressBar = view.findViewById(R.id.quizProgressBar);

        // Set up back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        // Set up initial progress
        setupProgressBars();
    }

    private void setupProgressBars() {
        try {
            // Example values - replace these with actual progress values from your data
            int courseProgress = 30; // Completed percentage
            int courseInProgress = 40; // In progress percentage
            
            int quizProgress = 25; // Completed percentage
            int quizInProgress = 35; // In progress percentage

            // Set progress values for course progress
            if (courseProgressBar != null) {
                courseProgressBar.setMax(100);
                courseProgressBar.setProgress(courseProgress);
                courseProgressBar.setSecondaryProgress(courseProgress + courseInProgress);
            }

            // Set progress values for quiz progress
            if (quizProgressBar != null) {
                quizProgressBar.setMax(100);
                quizProgressBar.setProgress(quizProgress);
                quizProgressBar.setSecondaryProgress(quizProgress + quizInProgress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}