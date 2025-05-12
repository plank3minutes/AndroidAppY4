/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appsnipp.education.data.JsonDataRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private static final String TAG = "CourseRepository";
    private static CourseRepository instance;
    private final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();
    private final Context context;

    private CourseRepository(Context context) {
        this.context = context.getApplicationContext();
        loadCoursesFromJson();
    }

    public static CourseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseRepository(context);
        }
        return instance;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<Course> getCourseById(String courseId) {
        MutableLiveData<Course> course = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course c : currentCourses) {
                if (c.getId().equals(courseId)) {
                    course.setValue(c);
                    break;
                }
            }
        }
        return course;
    }

    public LiveData<List<Quiz>> getQuizzesByCourseId(String courseId) {
        MutableLiveData<List<Quiz>> quizzes = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course course : currentCourses) {
                if (course.getId().equals(courseId)) {
                    List<Quiz> courseQuizzes = new ArrayList<>();
                    for (Lesson lesson : course.getLessons()) {
                        if (lesson.getQuiz() != null) {
                            courseQuizzes.add(lesson.getQuiz());
                        }
                    }
                    quizzes.setValue(courseQuizzes);
                    break;
                }
            }
        }
        if (quizzes.getValue() == null) {
            quizzes.setValue(new ArrayList<>());
        }
        return quizzes;
    }

    public LiveData<Quiz> getQuizByLessonId(String courseId, String lessonId) {
        MutableLiveData<Quiz> quiz = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course course : currentCourses) {
                if (course.getId().equals(courseId)) {
                    for (Lesson lesson : course.getLessons()) {
                        if (lesson.getId().equals(lessonId) && lesson.getQuiz() != null) {
                            quiz.setValue(lesson.getQuiz());
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return quiz;
    }

    private void loadCoursesFromJson() {
        try {
            List<Course> courses = JsonDataRepository.getInstance(context).getAllCourses();
            allCourses.setValue(courses);
        } catch (Exception e) {
            Log.e(TAG, "Error loading courses: " + e.getMessage());
            allCourses.setValue(new ArrayList<>());
        }
    }
} 