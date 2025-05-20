/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Quiz;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class QuizResultFragment extends Fragment {
    private Toolbar toolbar;
    private TextView overallPercentageTv;
    private TextView totalQuizTv;
    private TextView completedQuizTv;
    private TextView averageScoreTextView;

    public QuizResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_quiz_result, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        this.toolbar = view.findViewById(R.id.toolbar);
        this.overallPercentageTv = view.findViewById(R.id.overall_percentage_tv);
        this.totalQuizTv = view.findViewById(R.id.total_quiz_tv);
        this.completedQuizTv = view.findViewById(R.id.completed_quiz_tv);
        this.averageScoreTextView = view.findViewById(R.id.average_score_tv);

        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(QuizResultFragment.this).navigateUp();
            }
        });
    }

    private void loadData() {

    }
}