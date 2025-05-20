

package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.appsnipp.education.data.repository.CourseRepository;
import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.CourseStat;
import com.appsnipp.education.ui.model.UserProgress;

import java.util.ArrayList;
import java.util.List;

public class CourseStatViewModel extends ViewModel {
    private final CourseRepository courseRepo;
    private final ProgressRepository progressRepo;

    private final MediatorLiveData<CourseStat> courseStatLiveData = new MediatorLiveData<>();

    public CourseStatViewModel(Application application) {
        courseRepo = CourseRepository.getInstance(application);
        progressRepo = ProgressRepository.getInstance(application);

        LiveData<List<UserProgress>> progressLiveData = progressRepo.getAllUserProgress();
        LiveData<List<Course>> courseLiveData = courseRepo.getAllCourses();

        courseStatLiveData.addSource(progressLiveData, progress -> {
            combineData(courseLiveData.getValue(), progress);
        });

        courseStatLiveData.addSource(courseLiveData, courses -> {
            combineData(courses, progressLiveData.getValue());
        });
    }

    private void combineData(List<Course> courses, List<UserProgress> progresses) {
        if (courses == null || progresses == null) return;
        List<Course> completedCourses = new ArrayList<>();
        List<Integer> completedProgress = new ArrayList<>();

        List<Course> inProgressCourses = new ArrayList<>();
        List<Integer> inProgress = new ArrayList<>();

        List<Course> notJoinCourses = new ArrayList<>();
        List<Integer> notJoinProgress = new ArrayList<>();

        for (UserProgress up : progresses) {
            if (up.getTotalLessons() == 0) continue;

            for (Course course : courses) {
                if (course.getId().equals(up.getCourseId())) {
                    int percent = Math.round((float) up.getCompletedLessons() / up.getTotalLessons() * 100);
                    if (percent == 100) {
                        completedCourses.add(course);
                        completedProgress.add(percent);
                    } else if (percent > 0) {
                        inProgressCourses.add(course);
                        inProgress.add(percent);
                    } else {
                        notJoinCourses.add(course);
                        notJoinProgress.add(percent);
                    }
                }
            }
        }

        for (Course course : courses) {
            if (completedCourses.contains(course) || inProgressCourses.contains(course) || notJoinCourses.contains(course)) continue;
            notJoinCourses.add(course);
            notJoinProgress.add(0);
        }
        CourseStat data = new CourseStat(completedCourses, completedProgress, inProgressCourses, inProgress, notJoinCourses, notJoinProgress);
        courseStatLiveData.setValue(data);
    }

    public LiveData<CourseStat> getCourseStatLiveData() {
        return courseStatLiveData;
    }
}
