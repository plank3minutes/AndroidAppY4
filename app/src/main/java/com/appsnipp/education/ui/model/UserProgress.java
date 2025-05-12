package com.appsnipp.education.ui.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    private String uid;
    private String userId;
    private String lessonId;
    private boolean videoWatched;
    private boolean quizPassed;
    private int score;
    private int total;
    private String courseId;

    public UserProgress(){}

    public UserProgress(String userId, String lessonId, boolean videoWatched, boolean quizPassed, int score, int total, String courseId){
        this.userId = userId;
        this.lessonId = lessonId;
        this.videoWatched = videoWatched;
        this.quizPassed = quizPassed;
        this.score = score;
        this.total = total;
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUid(){
        return this.uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isVideoWatched() {
        return videoWatched;
    }

    public void setVideoWatched(boolean videoWatched) {
        this.videoWatched = videoWatched;
    }

    public boolean isQuizPassed() {
        return quizPassed;
    }

    public void setQuizPassed(boolean quizPassed) {
        this.quizPassed = quizPassed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
