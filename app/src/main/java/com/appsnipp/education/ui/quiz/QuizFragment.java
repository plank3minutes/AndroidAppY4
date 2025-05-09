/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.quiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentQuizBinding;
import com.appsnipp.education.ui.model.Question;
import com.appsnipp.education.ui.model.Quiz;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.Date;
import java.util.List;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private String courseId;
    private List<Quiz> quizzes;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int[] userAnswers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get courseId from arguments
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
        }

        setupViewModels();
        observeQuizData();
        setupButtonListeners();
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
    }

    private void observeQuizData() {
        if (courseId != null) {
            courseViewModel.getQuizzesByCourseId(courseId).observe(getViewLifecycleOwner(), quizList -> {
                if (quizList != null && !quizList.isEmpty()) {
                    quizzes = quizList;
                    // Get questions from the first quiz
                    questions = quizList.get(0).getQuestions();
                    userAnswers = new int[questions.size()];
                    for (int i = 0; i < userAnswers.length; i++) {
                        userAnswers[i] = -1; // -1 means no answer selected
                    }
                    displayCurrentQuestion();
                }
            });
        }
    }

    private void displayCurrentQuestion() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            binding.textQuestion.setText(question.getText());

            // Set up radio buttons for options
            binding.radioGroupOptions.clearCheck();
            List<String> options = question.getOptions();
            int optionCount = Math.min(options.size(), 4); // Maximum 4 options

            // Set options to radio buttons
            for (int i = 0; i < optionCount; i++) {
                RadioButton radioButton = getRadioButton(i);
                if (radioButton != null) {
                    radioButton.setText(options.get(i));
                    radioButton.setVisibility(View.VISIBLE);
                }
            }

            // Hide unused radio buttons
            for (int i = optionCount; i < 4; i++) {
                RadioButton radioButton = getRadioButton(i);
                if (radioButton != null) {
                    radioButton.setVisibility(View.GONE);
                }
            }

            // Check the previously selected answer if any
            if (userAnswers[currentQuestionIndex] >= 0) {
                RadioButton radioButton = getRadioButton(userAnswers[currentQuestionIndex]);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                }
            }

            // Update progress text
            updateProgressText();
        }
    }

    private RadioButton getRadioButton(int index) {
        switch (index) {
            case 0:
                return binding.radioOption1;
            case 1:
                return binding.radioOption2;
            case 2:
                return binding.radioOption3;
            case 3:
                return binding.radioOption4;
            default:
                return null;
        }
    }

    private void updateProgressText() {
        binding.textProgress.setText(getString(R.string.question_progress, 
            currentQuestionIndex + 1, questions.size()));
    }

    private void setupButtonListeners() {
        binding.buttonPrevious.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayCurrentQuestion();
            } else {
                // Nếu đang ở câu hỏi đầu tiên, hiển thị thông báo
                Toast.makeText(requireContext(), "This is the first question", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonNext.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayCurrentQuestion();
            } else {
                // Nếu đang ở câu hỏi cuối cùng, hiển thị hộp thoại xác nhận hoàn thành
                new AlertDialog.Builder(requireContext())
                    .setTitle("Complete Quiz")
                    .setMessage("Are you sure you want to submit your answers?")
                    .setPositiveButton("Submit", (dialog, which) -> {
                        calculateScore();
                    })
                    .setNegativeButton("Review", null)
                    .show();
            }
        });

        binding.radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            // Update the userAnswers array when a radio button is checked
            if (checkedId == R.id.radioOption1) {
                userAnswers[currentQuestionIndex] = 0;
            } else if (checkedId == R.id.radioOption2) {
                userAnswers[currentQuestionIndex] = 1;
            } else if (checkedId == R.id.radioOption3) {
                userAnswers[currentQuestionIndex] = 2;
            } else if (checkedId == R.id.radioOption4) {
                userAnswers[currentQuestionIndex] = 3;
            }
        });
    }

    private void saveCurrentAnswer() {
        int selectedRadioButtonId = binding.radioGroupOptions.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            if (selectedRadioButtonId == R.id.radioOption1) {
                userAnswers[currentQuestionIndex] = 0;
            } else if (selectedRadioButtonId == R.id.radioOption2) {
                userAnswers[currentQuestionIndex] = 1;
            } else if (selectedRadioButtonId == R.id.radioOption3) {
                userAnswers[currentQuestionIndex] = 2;
            } else if (selectedRadioButtonId == R.id.radioOption4) {
                userAnswers[currentQuestionIndex] = 3;
            }
        }
    }

    private void calculateScore() {
        score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectIndex()) {
                score++;
            }
        }

        // Calculate percentage
        int percentage = (score * 100) / questions.size();

        // Update user progress
        progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                progress.setQuizScore(percentage);
                progress.setLastAccess(new Date());
                progressViewModel.update(progress);
            } else {
                UserProgress newProgress = new UserProgress(courseId, 0, percentage, new Date());
                progressViewModel.insert(newProgress);
            }
        });

        // Show result in a toast and navigate back
        Toast.makeText(requireContext(), getString(R.string.quiz_score, score, questions.size(), percentage), Toast.LENGTH_LONG).show();
        
        // Quay lại màn hình trước đó sau khi hoàn thành quiz
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 