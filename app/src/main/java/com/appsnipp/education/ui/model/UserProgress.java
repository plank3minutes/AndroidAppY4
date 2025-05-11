/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String courseId;
    private int lessonIndex;
    private int quizScore;
    private Date lastAccess;

    public UserProgress() {
    }

    public UserProgress(String courseId, int lessonIndex, int quizScore, Date lastAccess) {
        this.courseId = courseId;
        this.lessonIndex = lessonIndex;
        this.quizScore = quizScore;
        this.lastAccess = lastAccess;
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

    public int getLessonIndex() {
        return lessonIndex;
    }

    public void setLessonIndex(int lessonIndex) {
        this.lessonIndex = lessonIndex;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
} 