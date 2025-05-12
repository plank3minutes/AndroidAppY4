package com.appsnipp.education.ui.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lesson_status")
public class LessonStatus {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String courseId;
    private String lessonId;
    private int quizScore;
    private boolean isCompleted;
    private long completedAt;

    public LessonStatus() {
    }

    public LessonStatus(String courseId, String lessonId, int quizScore, boolean isCompleted, long completedAt) {
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.quizScore = quizScore;
        this.isCompleted = isCompleted;
        this.completedAt = completedAt;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }
} 