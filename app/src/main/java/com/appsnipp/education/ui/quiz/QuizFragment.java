package com.appsnipp.education.ui.quiz;

import android.app.AlertDialog;
import android.graphics.Color;
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
import com.appsnipp.education.ui.viewmodel.LessonStatusViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private LessonStatusViewModel lessonStatusViewModel;
    private String courseId;
    private String lessonId;
    private List<Quiz> quizzes;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int[] userAnswers;

    // Danh sách RadioButton và CardView để quản lý dễ dàng
    private List<RadioButton> radioButtons;
    private List<MaterialCardView> cardViews;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get courseId and lessonId from arguments
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            lessonId = getArguments().getString("lessonId");
        }

        // Khôi phục trạng thái nếu có
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0);
            userAnswers = savedInstanceState.getIntArray("userAnswers");
            score = savedInstanceState.getInt("score", 0);
        }

        // Khởi tạo danh sách RadioButton và CardView
        initRadioButtonsAndCards();
        setupToolbar();
        setupViewModels();
        observeQuizData();
        setupButtonListeners();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
        outState.putIntArray("userAnswers", userAnswers);
        outState.putInt("score", score);
    }

    private void initRadioButtonsAndCards() {
        radioButtons = new ArrayList<>();
        cardViews = new ArrayList<>();

        // Thêm RadioButton vào danh sách
        radioButtons.add(binding.radioOption1);
        radioButtons.add(binding.radioOption2);
        radioButtons.add(binding.radioOption3);
        radioButtons.add(binding.radioOption4);

        // Lấy các MaterialCardView từ RadioGroup
        for (int i = 0; i < binding.radioGroupOptions.getChildCount(); i++) {
            View child = binding.radioGroupOptions.getChildAt(i);
            if (child instanceof MaterialCardView) {
                cardViews.add((MaterialCardView) child);
            }
        }
    }

    private void setupToolbar() {
        binding.quizToolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
        lessonStatusViewModel = new ViewModelProvider(requireActivity()).get(LessonStatusViewModel.class);
    }

    private void observeQuizData() {
        if (courseId != null) {
            courseViewModel.getQuizzesByCourseId(courseId).observe(getViewLifecycleOwner(), quizList -> {
                if (quizList != null && !quizList.isEmpty()) {
                    quizzes = quizList;
                    // Get questions from the first quiz
                    questions = quizList.get(0).getQuestions();
                    // Chỉ khởi tạo userAnswers nếu chưa được khôi phục
                    if (userAnswers == null) {
                        userAnswers = new int[questions.size()];
                        for (int i = 0; i < userAnswers.length; i++) {
                            userAnswers[i] = -1; // -1 means no answer selected
                        }
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

            // Reset all radio buttons
            for (RadioButton radioButton : radioButtons) {
                radioButton.setChecked(false);
            }

            // Reset all card backgrounds
            resetCardBackgrounds();

            List<String> options = question.getOptions();
            int optionCount = Math.min(options.size(), 4); // Maximum 4 options

            // Set options to radio buttons
            for (int i = 0; i < optionCount; i++) {
                RadioButton radioButton = radioButtons.get(i);
                radioButton.setText(options.get(i));
                radioButton.setVisibility(View.VISIBLE);
                cardViews.get(i).setVisibility(View.VISIBLE);
            }

            // Hide unused radio buttons and cards
            for (int i = optionCount; i < 4; i++) {
                radioButtons.get(i).setVisibility(View.GONE);
                cardViews.get(i).setVisibility(View.GONE);
            }

            // Check the previously selected answer if any
            if (userAnswers[currentQuestionIndex] >= 0 && userAnswers[currentQuestionIndex] < radioButtons.size()) {
                radioButtons.get(userAnswers[currentQuestionIndex]).setChecked(true);
                highlightSelectedCard(userAnswers[currentQuestionIndex]);
            }

            // Update progress text and progress indicator
            updateProgressText();
            updateProgressIndicator();
        }
    }

    private void resetCardBackgrounds() {
        for (MaterialCardView cardView : cardViews) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.card_quiz_background));
            cardView.setStrokeColor(getResources().getColor(R.color.card_quiz_stroke));
        }
    }

    private void highlightSelectedCard(int index) {
        if (index >= 0 && index < cardViews.size()) {
            cardViews.get(index).setCardBackgroundColor(getResources().getColor(R.color.card_quiz_selected_background));
            cardViews.get(index).setStrokeColor(getResources().getColor(R.color.card_quiz_selected_stroke));
        }
    }

    private void updateProgressText() {
        binding.textProgress.setText(getString(R.string.question_progress,
                currentQuestionIndex + 1, questions.size()));
    }

    private void updateProgressIndicator() {
        int progress = ((currentQuestionIndex + 1) * 100) / questions.size();
        binding.progressIndicator.setProgress(progress);
    }

    private void setupButtonListeners() {
        binding.buttonPrevious.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayCurrentQuestion();
            } else {
                Toast.makeText(requireContext(), "This is the first question", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonNext.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayCurrentQuestion();
            } else {
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

        // Thiết lập sự kiện click cho từng RadioButton
        for (int i = 0; i < radioButtons.size(); i++) {
            final int index = i;
            radioButtons.get(i).setOnClickListener(v -> {
                // Bỏ chọn tất cả các RadioButton khác
                for (int j = 0; j < radioButtons.size(); j++) {
                    if (j != index) {
                        radioButtons.get(j).setChecked(false);
                    }
                }

                // Reset tất cả card backgrounds
                resetCardBackgrounds();

                // Highlight card được chọn
                highlightSelectedCard(index);

                // Lưu lựa chọn của người dùng
                userAnswers[currentQuestionIndex] = index;
            });
        }
    }

    private void saveCurrentAnswer() {
        // Kiểm tra RadioButton nào được chọn
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked()) {
                userAnswers[currentQuestionIndex] = i;
                return;
            }
        }
        // Nếu không có lựa chọn nào, đặt giá trị -1
        userAnswers[currentQuestionIndex] = -1;
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

        // Update LessonStatus with quiz score
        if (courseId != null && lessonId != null) {
            lessonStatusViewModel.completeQuiz(courseId, lessonId, percentage);
        }

        // Show result in a toast and navigate back
        Toast.makeText(requireContext(), getString(R.string.quiz_score, score, questions.size(), percentage), Toast.LENGTH_LONG).show();

        NavHostFragment.findNavController(this).popBackStack(R.id.lessonDetailFragment, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}