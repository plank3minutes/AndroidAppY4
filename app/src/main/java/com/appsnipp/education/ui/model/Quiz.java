/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.model;

import java.util.List;

public class Quiz {
    private String id;
    private String lessonId;
    private List<Question> questions;
    private String title;

    public Quiz() {
    }

    public Quiz(String id, String lessonId, List<Question> questions, String title) {
        this.id = id;
        this.lessonId = lessonId;
        this.questions = questions;
        this.title = title;
    }
    
    // Thêm constructor để hỗ trợ dữ liệu từ JSON
    public Quiz(String lessonId, String title, List<Question> questions) {
        this.id = String.valueOf(System.currentTimeMillis()); // Tạo id tạm thời
        this.lessonId = lessonId;
        this.questions = questions;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLesson(String lessonId) {
        this.lessonId = lessonId;
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
