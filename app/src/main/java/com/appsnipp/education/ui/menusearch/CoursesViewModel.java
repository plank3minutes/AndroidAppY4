/*
 * Copyright (c) 2021. rogergcc
 */

package com.appsnipp.education.ui.menusearch;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.appsnipp.education.data.CoursesRepository;
import com.appsnipp.education.ui.model.CourseCard;
import com.appsnipp.education.ui.model.MatchCourse;

import java.util.List;

/**
 * Created on September.
 * year 2021 .
 */

public class CoursesViewModel extends ViewModel {
    private final MutableLiveData<List<MatchCourse>> mMatchedCourses;
    private final MutableLiveData<List<CourseCard>> mCourseCards;
    private final CoursesRepository repository;

    public CoursesViewModel(CoursesRepository repository) {
        this.repository = repository;
        this.mMatchedCourses = new MutableLiveData<>();
        this.mCourseCards = new MutableLiveData<>();
    }

    public LiveData<List<MatchCourse>> matchedCourses() {
        return mMatchedCourses;
    }
    
    public LiveData<List<CourseCard>> courseCards() {
        return mCourseCards;
    }

    public void fetchMatchedCourses() {
        List<MatchCourse> data = repository.matchedCourses();
        mMatchedCourses.postValue(data);
    }
    
    public void fetchCourseCards() {
        List<CourseCard> data = repository.getCourseCards();
        mCourseCards.postValue(data);
    }

    public static class MyCoursesViewModelFactory implements ViewModelProvider.Factory {
        private final CoursesRepository repository;

        public MyCoursesViewModelFactory(Context context) {
            this.repository = new CoursesRepository(context);
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CoursesViewModel.class)) {
                return modelClass.cast(new CoursesViewModel(repository));
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

