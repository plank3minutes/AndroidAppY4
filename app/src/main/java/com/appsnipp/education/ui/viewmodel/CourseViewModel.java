/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appsnipp.education.data.repository.CourseRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Quiz;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private final CourseRepository repository;
    private final LiveData<List<Course>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = CourseRepository.getInstance(application);
        allCourses = repository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<Course> getCourseById(String courseId) {
        return repository.getCourseById(courseId);
    }

    public LiveData<List<Quiz>> getQuizzesByCourseId(String courseId) {
        return repository.getQuizzesByCourseId(courseId);
    }

    public LiveData<List<Course>> getCoursesByName(String name) {
        return repository.getCoursesByName(name);
    }

    public LiveData<List<Course>> getFiveCourses() {
        return repository.getFiveCourses();
    }

} 