/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.model;

import java.util.List;

public class Quiz {
    private String id;
    private String courseId;
    private List<Question> questions;
    private String title;

    public Quiz() {
    }

    public Quiz(String id, String courseId, List<Question> questions, String title) {
        this.id = id;
        this.courseId = courseId;
        this.questions = questions;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quiz) {
            Quiz other = (Quiz) obj;
            return this.id.equals(other.id);
        }
        return false;
    }
} 