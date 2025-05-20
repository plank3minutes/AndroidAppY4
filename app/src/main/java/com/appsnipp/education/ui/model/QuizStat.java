package com.appsnipp.education.ui.model;

import java.util.List;
import java.util.Map;

public class QuizStat {
    private int passQuiz;
    private int failQuiz;
    private double averageScore;
    private int completedPercentage;
    private List<LessonStatus> lessonStatuses;
    private Map<String, Course> courseMap;
    private Map<String, Integer> progressByCourse;

    public Map<String, Integer> getProgressByCourse() {
        return progressByCourse;
    }

    public void setProgressByCourse(Map<String, Integer> progressByCourse) {
        this.progressByCourse = progressByCourse;
    }

    public int getPassQuiz() {
        return this.passQuiz;
    }

    public void setPassQuiz(int totalQuiz) {
        this.passQuiz = totalQuiz;
    }

    public int getFailQuiz() {
        return this.failQuiz;
    }

    public void setFailQuiz(int completedQuiz) {
        this.failQuiz = completedQuiz;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getCompletedPercentage() {
        return this.completedPercentage;
    }

    public void setCompletedPercentage(int accuracy) {
        this.completedPercentage = accuracy;
    }

    public List<LessonStatus> getLessonStatuses() {
        return lessonStatuses;
    }

    public void setLessonStatuses(List<LessonStatus> lessonStatuses) {
        this.lessonStatuses = lessonStatuses;
    }

    public Map<String, Course> getCourseMap() {
        return courseMap;
    }

    public void setCourseMap(Map<String, Course> courseMap) {
        this.courseMap = courseMap;
    }
}
