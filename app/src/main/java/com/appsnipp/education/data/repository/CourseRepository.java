package com.appsnipp.education.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appsnipp.education.data.JsonDataRepository;
import com.appsnipp.education.ui.model.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.appsnipp.education.util.JsonUtil;
import com.google.gson.Gson;

public class CourseRepository {
    private static final String TAG = "CourseRepository";
    private static CourseRepository instance;
    private final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();
    private final Context context;
    private final Gson gson = new Gson();
    private boolean isDataLoaded = false;
    private final Map<String, Course> courseMap = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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

    private void loadCoursesFromJson() {
        if (isDataLoaded) return;
        executor.execute(() -> {
            try {
                String jsonString = JsonUtil.loadJSONStringFromAsset(context, "courses.json");
                if (jsonString == null) {
                    throw new Exception("Failed to load courses.json");
                }
                Course[] courses = gson.fromJson(jsonString, Course[].class);
                synchronized (courseMap) {
                    courseMap.clear();
                    for (Course course : courses) {
                        if (course.getId() == null || course.getId().isEmpty()) {
                            Log.w("JsonDataRepository", "Invalid course ID, skipping");
                            continue;
                        }
                        courseMap.put(course.getId(), course);
                    }
                    isDataLoaded = true;
                }
                allCourses.postValue(new ArrayList<>(courseMap.values()));
            } catch (Exception e) {
                Log.e("JsonDataRepository", "Error loading courses", e);
                synchronized (courseMap) {
                    courseMap.clear();
                }
                allCourses.postValue(null);
            }
        });
    }
}