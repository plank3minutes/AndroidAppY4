package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appsnipp.education.data.repository.CourseRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseViewModel extends AndroidViewModel {
    private final CourseRepository repository;
    private final LiveData<List<Course>> allCourses;
    private final MutableLiveData<List<Course>> filteredCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = CourseRepository.getInstance(application);
        allCourses = repository.getAllCourses();
        filteredCourses = new MutableLiveData<>(allCourses.getValue());
    }

    public MutableLiveData<List<Course>> getFilteredCourses() {
        return filteredCourses;
    }

    public void search(String query) {
        if (query.isEmpty()) {
            filteredCourses.setValue(allCourses.getValue());
        } else {
            List<Course> courses = allCourses.getValue();
            if (courses != null) {
                List<Course> filtered = courses.stream()
                        .filter(course -> course.getTitle().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
                filteredCourses.setValue(filtered);
            } else {
                filteredCourses.setValue(new ArrayList<>());
            }
        }
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

    public LiveData<Quiz> getQuizByCourseIdAndLessonId(String courseId, String lessonId) {
        return repository.getQuizByCourseIdAndLessonId(courseId, lessonId);
    }


    public LiveData<List<Course>> getCoursesByName(String name) {
        return repository.getCoursesByName(name);
    }

    public LiveData<List<Course>> getCoursesByNameAndType(String name, String type) {
        return repository.getCoursesByNameAndType(name, type);
    }

} 
