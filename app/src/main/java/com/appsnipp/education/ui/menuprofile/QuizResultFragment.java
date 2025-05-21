package com.appsnipp.education.ui.menuprofile;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.adapter.QuizStatAdapter;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Quiz;
import com.appsnipp.education.ui.model.QuizStat;
import com.appsnipp.education.ui.viewmodel.QuizStatViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Locale;
import java.util.Map;

public class QuizResultFragment extends BaseFragment {
    private Toolbar toolbar;
    private TextView overallPercentageTv;
    private CircularProgressIndicator circularProgressIndicator;
    private TextView passQuizTv;
    private TextView failQuizTv;
    private TextView averageScoreTextView;
    private LinearLayout performanceQuizResultList;
    private RecyclerView quizResultRecyclerView;
    private LinearLayout emptyQuizStat;
    private LinearLayout emptyQuizResult;

    public QuizResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_quiz_result, container, false);
        initComponent(view);
        loadData(inflater);
        return view;
    }

    private void initComponent(View view) {
        this.toolbar = view.findViewById(R.id.toolbar);
        this.overallPercentageTv = view.findViewById(R.id.overall_percentage_tv);
        this.passQuizTv = view.findViewById(R.id.pass_quiz_tv);
        this.failQuizTv = view.findViewById(R.id.fail_quiz_tv);
        this.averageScoreTextView = view.findViewById(R.id.average_score_tv);
        this.performanceQuizResultList = view.findViewById(R.id.performance_quiz_stat_by_course_list);
        this.quizResultRecyclerView = view.findViewById(R.id.recycler_view_quiz_result);
        this.emptyQuizResult = view.findViewById(R.id.empty_quiz_result);
        this.emptyQuizStat = view.findViewById(R.id.empty_performance_quiz_stat_by_course);
        this.circularProgressIndicator = view.findViewById(R.id.overall_progress);

        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(QuizResultFragment.this).navigateUp();
            }
        });
    }

    private void loadData(LayoutInflater inflater) {
        QuizStatViewModel quizStatViewModel = new QuizStatViewModel(requireActivity().getApplication());
        LiveData<QuizStat> quizStatLiveData = quizStatViewModel.getLiveDataQuizStat();
        quizStatLiveData.observe(getViewLifecycleOwner(), new Observer<QuizStat>() {
            @Override
            public void onChanged(QuizStat quizStat) {
                QuizResultFragment.this.overallPercentageTv.setText(String.format(Locale.getDefault(), "%d%%", quizStat.getCompletedPercentage()));
                QuizResultFragment.this.passQuizTv.setText(String.format(Locale.getDefault(), "%d", quizStat.getPassQuiz()));
                QuizResultFragment.this.failQuizTv.setText(String.format(Locale.getDefault(), "%d", quizStat.getFailQuiz()));
                QuizResultFragment.this.averageScoreTextView.setText(String.format(Locale.getDefault(), "%.1f/10", quizStat.getAverageScore()));
                QuizResultFragment.this.circularProgressIndicator.setProgress(quizStat.getCompletedPercentage());

                if (!quizStat.getProgressByCourse().keySet().isEmpty()) {
                    for(Map.Entry<String, Integer> entry : quizStat.getProgressByCourse().entrySet()) {
                        Course course = quizStat.getCourseMap().get(entry.getKey());
                        int progress = entry.getValue();
                        View itemView = inflater.inflate(R.layout.item_quiz_stat_by_course, QuizResultFragment.this.performanceQuizResultList, false);
                        TextView courseName = itemView.findViewById(R.id.title_course_quiz_stat_by_course_tv);
                        LinearProgressIndicator progressIndicator = itemView.findViewById(R.id.progress_quiz_stat_by_course);
                        TextView progressTv = itemView.findViewById(R.id.progress_quiz_stat_by_course_tv);

                        courseName.setText(course.getTitle());
                        progressIndicator.setProgress(progress);
                        progressTv.setText(String.format(Locale.getDefault(), "%d%%", progress));

                        QuizResultFragment.this.performanceQuizResultList.addView(itemView);
                    }
                } else {
                    emptyQuizStat.setVisibility(View.VISIBLE);
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuizResultFragment.this.getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                QuizResultFragment.this.quizResultRecyclerView.setLayoutManager(linearLayoutManager);
                QuizStatAdapter quizStatAdapter = new QuizStatAdapter(quizStat.getLessonStatuses(), quizStat.getCourseMap());

                if (quizStat.getLessonStatuses().isEmpty()) {
                    QuizResultFragment.this.emptyQuizResult.setVisibility(View.VISIBLE);
                } else {
                    QuizResultFragment.this.quizResultRecyclerView.setAdapter(quizStatAdapter);
                }
            }
        });
    }
}