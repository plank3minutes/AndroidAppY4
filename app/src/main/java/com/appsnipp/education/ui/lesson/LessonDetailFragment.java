/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.lesson;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentLessonDetailBinding;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.LessonStatusViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.Date;
import java.util.List;

public class LessonDetailFragment extends Fragment {

    private FragmentLessonDetailBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private LessonStatusViewModel lessonStatusViewModel;
    private String lessonId;
    private String courseId;
    private Lesson currentLesson;
    private Course currentCourse;
    private int lessonIndex = 0;
    private boolean isVideoWatched = false;
    private boolean isQuizCompleted = false;
    private WebView videoWebView;
    private View customView; // Lưu view toàn màn hình
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLessonDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            lessonId = getArguments().getString("lessonId");
            courseId = getArguments().getString("courseId");
        }

        setupToolbar();
        setupViewModels();
        observeData();
        setupButtonListeners();
        checkLessonStatus();
        updateUserProcessLastAccess();
    }

    private void setupToolbar() {
        binding.lessonToolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
        lessonStatusViewModel = new ViewModelProvider(requireActivity()).get(LessonStatusViewModel.class);
    }

    private void observeData() {
        if (courseId != null) {
            courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
                if (course != null) {
                    currentCourse = course;
                    findCurrentLesson(course.getLessons());
                }
            });
        }
    }

    private void findCurrentLesson(List<Lesson> lessons) {
        if (lessonId != null && lessons != null) {
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                if (lesson.getId().equals(lessonId)) {
                    currentLesson = lesson;
                    lessonIndex = i;
                    setupLessonContent(lesson);
                    break;
                }
            }
        }
    }

    private void setupLessonContent(Lesson lesson) {
        binding.lessonToolbar.setTitle(lesson.getTitle());
        binding.textLessonTitle.setText(lesson.getTitle());

        // Setup WebView to display content
        WebSettings webSettings = binding.webViewLessonContent.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu cần
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false); // Cho phép zoom nếu cần
        webSettings.setDisplayZoomControls(false); // Ẩn nút zoom

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body { background-color: #2A2E45; color: #FFFFFF; font-family: Arial, sans-serif; margin: 20px; text-align:justify;}" +
                "img { max-width: 100%; height: auto; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div>" + lesson.getContent() + "</div>" +
                "</body>" +
                "</html>";

        binding.webViewLessonContent.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
        
        // Setup video if available
        if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
            binding.videoViewLesson.setVisibility(View.VISIBLE);
            setupVideoPlayer(lesson.getVideoUrl());
        } else {
            binding.videoViewLesson.setVisibility(View.GONE);
            isVideoWatched = true; // No video means no need to watch
        }

//        // Disable complete button initially
//        binding.buttonCompleteLesson.setEnabled(false);
    }

    private void setupVideoPlayer(String videoUrl) {
        binding.videoViewLesson.setVisibility(View.VISIBLE);
        WebSettings webSettings = binding.videoViewLesson.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        videoWebView = binding.videoViewLesson;

        // Thêm interface để giao tiếp từ JavaScript sang Android
        videoWebView.addJavascriptInterface(new WebAppInterface(), "Android");

        videoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Gọi hàm JavaScript để thiết lập video ID
                setVideoId(videoUrl);
            }
        });

        // Tải file HTML từ assets
        videoWebView.loadUrl("file:///android_asset/youtube_player.html");

        // Cấu hình WebChromeClient để hỗ trợ toàn màn hình
        videoWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // Khi vào chế độ toàn màn hình
                if (customView != null) {
                    onHideCustomView();
                    return;
                }
                customView = view;
                customViewCallback = callback;

                // Ẩn WebView và hiển thị customView toàn màn hình
                videoWebView.setVisibility(View.GONE);
                ViewGroup rootView = binding.getRoot();
                rootView.addView(customView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onHideCustomView() {
                // Thoát chế độ toàn màn hình
                if (customView == null) return;

                ViewGroup rootView = binding.getRoot();
                rootView.removeView(customView);
                customView = null;
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;

                // Hiển thị lại WebView
                videoWebView.setVisibility(View.VISIBLE);
            }
        });

    }

    // Interface để nhận thông tin từ JavaScript
    public static class WebAppInterface {
        @JavascriptInterface
        public void onVideoStateChange(String state) {
            // Xử lý trạng thái video (nếu cần)
            System.out.println("Video state: " + state);
        }
    }

    public void setVideoId(String videoId) {
        // Gọi hàm JavaScript để thiết lập video ID
        videoWebView.evaluateJavascript("player.loadVideoById('" + videoId +"');", null);
    }

    // Gọi hàm JavaScript từ Android
    public void playVideo() {
        videoWebView.evaluateJavascript("playVideo();", null);
    }

    public void pauseVideo() {
        videoWebView.evaluateJavascript("pauseVideo();", null);
    }

    public void stopVideo() {
        videoWebView.evaluateJavascript("stopVideo();", null);
    }

//    private void setupVideoPlayer(String videoUrl) {
//        try {
//            MediaController mediaController = new MediaController(requireContext());
//            mediaController.setAnchorView(binding.videoViewLesson);
//
//            Uri videoUri = Uri.parse(videoUrl);
//            binding.videoViewLesson.setMediaController(mediaController);
//            binding.videoViewLesson.setVideoURI(videoUri);
//            binding.videoViewLesson.requestFocus();
//
//            // Add completion listener
//            binding.videoViewLesson.setOnCompletionListener(mp -> {
//                isVideoWatched = true;
//                checkCompletionStatus();
//            });
//        } catch (Exception e) {
//            Toast.makeText(requireContext(), getString(R.string.video_error), Toast.LENGTH_SHORT).show();
//            binding.videoViewLesson.setVisibility(View.GONE);
//            isVideoWatched = true; // If video fails, consider it watched
//        }
//    }

    private void setupButtonListeners() {
        binding.buttonTakeQuiz.setOnClickListener(v -> {
            // Navigate to quiz fragment
            Bundle args = new Bundle();
            args.putString("lessonId", lessonId);
            args.putString("courseId", courseId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_lessonDetailFragment_to_quizFragment, args);
        });

        binding.buttonCompleteLesson.setOnClickListener(v -> {
            markLessonAsComplete();
        });
    }

    private void checkLessonStatus() {
        if (courseId != null && lessonId != null) {
            lessonStatusViewModel.getLessonStatus(courseId, lessonId)
                    .observe(getViewLifecycleOwner(), status -> {
                        if (status != null && status.isCompleted()) {
                            isVideoWatched = true;
                            isQuizCompleted = true;
                            binding.buttonCompleteLesson.setEnabled(false);
                            binding.buttonTakeQuiz.setEnabled(false);
                        }
                        else if (status != null && status.getQuizScore() > 0 && !status.isCompleted()) {
                            onQuizCompleted();
                        }
                        else {
                            binding.buttonCompleteLesson.setEnabled(false);
                        }
                    });
        }
    }

    private void checkCompletionStatus() {
//        boolean canComplete = isVideoWatched && isQuizCompleted;
        boolean canComplete = isQuizCompleted; // Assuming video is not mandatory
        binding.buttonCompleteLesson.setEnabled(canComplete);
    }

    public void onQuizCompleted() {
        isQuizCompleted = true;
        checkCompletionStatus();
    }

    private void updateUserProcessLastAccess() {
        // Update UserProgress
        progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                progress.setLastAccess(new Date());
                progressViewModel.update(progress);

            } else {
                UserProgress newProgress = new UserProgress(courseId, currentCourse.getLessonCount(), 0, false, new Date());
                progressViewModel.insert(newProgress);
            }
        });
    }


    private void markLessonAsComplete() {
        if (currentCourse != null && currentLesson != null) {
            // Update LessonStatus
            lessonStatusViewModel.completeLessonWithoutQuiz(courseId, lessonId); // Score will be updated after quiz

            // Update UserProgress
            progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
                if (progress != null) {
                    progress.setCompletedLessons(progress.getCompletedLessons() + 1);
                    progress.setLastAccess(new Date());
                    progressViewModel.update(progress);
                    Toast.makeText(requireContext(), getString(R.string.lesson_completed), Toast.LENGTH_SHORT).show();

                    NavHostFragment.findNavController(this).popBackStack();

                } else {
                    UserProgress newProgress = new UserProgress(courseId, currentCourse.getLessonCount(), 1, false, new Date());
                    progressViewModel.insert(newProgress);
                    Toast.makeText(requireContext(), getString(R.string.lesson_completed), Toast.LENGTH_SHORT).show();
                    
                    NavHostFragment.findNavController(this).popBackStack();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        videoWebView.destroy();
    }
} 