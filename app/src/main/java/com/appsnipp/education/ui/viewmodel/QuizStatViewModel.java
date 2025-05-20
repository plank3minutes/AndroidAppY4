package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.appsnipp.education.data.repository.CourseRepository;
import com.appsnipp.education.data.repository.LessonStatusRepository;
import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.model.Quiz;
import com.appsnipp.education.ui.model.QuizStat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizStatViewModel extends ViewModel {
    private final CourseRepository courseRepository;
    private final LessonStatusRepository lessonStatusRepository;
    private final MediatorLiveData<QuizStat> quizStatLiveData = new MediatorLiveData<>();

    public QuizStatViewModel(Application application) {
        this.courseRepository = CourseRepository.getInstance(application);
        this.lessonStatusRepository = LessonStatusRepository.getInstance(application);

        LiveData<List<Course>> courseLiveData = this.courseRepository.getAllCourses();
        LiveData<List<LessonStatus>> lessonStatusLiveData = this.lessonStatusRepository.getAllLessonStatus();

        quizStatLiveData.addSource(courseLiveData, courses -> {
            combineData(courses, lessonStatusLiveData.getValue());
        });

        quizStatLiveData.addSource(lessonStatusLiveData, lessonStatuses -> {
            combineData(courseLiveData.getValue(), lessonStatuses);
        });
    }

    private void combineData(List<Course> courses, List<LessonStatus> lessonStatuses) {
        if (courses == null || lessonStatuses == null) return;
        QuizStat quizStat = new QuizStat();
        quizStat.setLessonStatuses(lessonStatuses);
        Map<String, Course> courseMap = new HashMap<>();
        Map<String, Integer> progressByCourse = new HashMap<>();
        for(Course course: courses) {
            courseMap.put(course.getId(), course);
        }
        quizStat.setCourseMap(courseMap);

        int passQuiz = 0;
        int failQuiz = 0;
        int totalScore = 0;
        int totalQuiz = 0;
        for(LessonStatus lessonStatus: lessonStatuses) {
            if(lessonStatus.getQuizScore() >= 0) {
                totalScore += lessonStatus.getQuizScore();
                totalQuiz++;
                Course course = courseMap.get(lessonStatus.getCourseId());
                for(Lesson lesson : course.getLessons()) {
                    if(lesson.getId().equals(lessonStatus.getLessonId())) {
                        if (lessonStatus.getQuizScore() > Math.floor((double) lesson.getQuiz().getQuestions().size()/2)) {
                            passQuiz++;
                        } else {
                            failQuiz++;
                        }
                    }
                }
            }
        }

        for(Course course: courses) {
            int quizScoreCourse = 0;
            int totalQuizCourse = 0;
            for(Lesson ls: course.getLessons()) {
                for(LessonStatus lessonStatus: lessonStatuses) {
                    if(lessonStatus.getQuizScore() >= 0 && lessonStatus.getLessonId().equals(ls.getId()) && lessonStatus.getCourseId().equals(course.getId())) {
                        quizScoreCourse += lessonStatus.getQuizScore();
                        totalQuizCourse++;
                    }
                }
            }
            if (totalQuizCourse != 0) {
                progressByCourse.put(course.getId(), (int)Math.ceil((double) quizScoreCourse / totalQuizCourse));
            }
        }

        quizStat.setFailQuiz(failQuiz);
        quizStat.setPassQuiz(passQuiz);
        quizStat.setAverageScore(totalQuiz == 0 ? 0.0 : (double)totalScore / totalQuiz / 10);
        int totalAttempted = passQuiz + failQuiz;
        quizStat.setCompletedPercentage(totalAttempted == 0 ? 0 : (int) Math.round((double) passQuiz / totalAttempted * 100));
        quizStat.setProgressByCourse(progressByCourse);

        quizStatLiveData.setValue(quizStat);
    }

    public LiveData<QuizStat> getLiveDataQuizStat() {
        return this.quizStatLiveData;
    }
}
