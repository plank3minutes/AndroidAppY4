/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.Question;
import com.appsnipp.education.ui.model.Quiz;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
        List<Quiz> courseQuizzes = new ArrayList<>();
        
        // In a real app, this would load from the JSON
        // For demo, we'll create some sample quizzes
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");
        
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("q1", "quiz1", "What is the capital of France?", options, 1));
        questions.add(new Question("q2", "quiz1", "What is 2+2?", options, 3));
        
        Quiz quiz = new Quiz("quiz1", courseId, questions, "Course Quiz");
        courseQuizzes.add(quiz);
        
        quizzes.setValue(courseQuizzes);
        return quizzes;
    }

    private void loadCoursesFromJson() {
        try {
            // In a real app, this would load from assets/courses.json
            // For now, create sample data
            List<Course> courses = createSampleCourses();
            allCourses.setValue(courses);
        } catch (Exception e) {
            Log.e(TAG, "Error loading courses: " + e.getMessage());
            allCourses.setValue(new ArrayList<>());
        }
    }

    private List<Course> createSampleCourses() {
        List<Course> courses = new ArrayList<>();
        
        // Create sample Lessons
        List<Lesson> androidLessons = new ArrayList<>();
        androidLessons.add(new Lesson("l1", "c1", "Introduction to Android", "Android is an open source mobile operating system developed by Google...", null));
        androidLessons.add(new Lesson("l2", "c1", "Activity Lifecycle", "The activity lifecycle is a set of states through which an activity transitions...", "https://example.com/video1"));
        
        List<Lesson> javaLessons = new ArrayList<>();
        javaLessons.add(new Lesson("l3", "c2", "Java Basics", "Java is a popular programming language...", null));
        javaLessons.add(new Lesson("l4", "c2", "Object-Oriented Programming", "OOP is a programming paradigm based on the concept of objects...", "https://example.com/video2"));
        
        // Create sample Courses
        courses.add(new Course("c1", "Android Development", "Learn Android development from scratch", androidLessons, R.drawable.course_android));
        courses.add(new Course("c2", "Java Programming", "Master Java programming language", javaLessons, R.drawable.course_java));
        
        return courses;
    }
} 