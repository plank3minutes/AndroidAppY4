package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appsnipp.education.data.repository.LessonStatusRepository;
import com.appsnipp.education.ui.model.LessonStatus;

import java.util.List;

public class LessonStatusViewModel extends AndroidViewModel {
    private final LessonStatusRepository repository;

    public LessonStatusViewModel(@NonNull Application application) {
        super(application);
        repository = LessonStatusRepository.getInstance(application);
    }

    public LiveData<List<LessonStatus>> getLessonStatusByCourseId(String courseId) {
        return repository.getLessonStatusByCourseId(courseId);
    }

    public int countCompletedLessonsSync(String courseId) {
        return repository.countCompletedLessonsSync(courseId);
    }

    public LiveData<LessonStatus> getLessonStatus(String courseId, String lessonId) {
        return repository.getLessonStatus(courseId, lessonId);
    }

    public LiveData<List<LessonStatus>> getCompletedLessons(String courseId) {
        return repository.getCompletedLessons(courseId);
    }

    public void insert(LessonStatus lessonStatus) {
        repository.insert(lessonStatus);
    }

    public void update(LessonStatus lessonStatus) {
        repository.update(lessonStatus);
    }

    public void completeLesson(String courseId, String lessonId, int quizScore) {
        repository.completeLesson(courseId, lessonId, quizScore);
    }

    public void completeLessonWithoutQuiz(String courseId, String lessonId) {
        repository.completeLessonWithoutQuiz(courseId, lessonId);
    }

    public void completeQuiz(String courseId, String lessonId, int quizScore) {
        repository.completeQuiz(courseId, lessonId, quizScore);
    }

    public void delete(LessonStatus lessonStatus) {
        repository.delete(lessonStatus);
    }

    public void deleteByCourseId(String courseId) {
        repository.deleteByCourseId(courseId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
} 