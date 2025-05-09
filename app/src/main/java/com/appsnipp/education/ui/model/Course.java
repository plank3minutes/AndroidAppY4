/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.model;

import java.util.List;

public class Course {
    private String id;
    private String title;
    private String description;
    private List<Lesson> lessons;
    private int imageResource;
    private Quiz quiz;

    public Course() {
    }

    public Course(String id, String title, String description, List<Lesson> lessons, int imageResource) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
        this.imageResource = imageResource;
    }

    public Course(String id, String title, String description, int imageResource, List<Lesson> lessons, Quiz quiz) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
        this.imageResource = imageResource;
        this.quiz = quiz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            Course other = (Course) obj;
            return this.id.equals(other.id);
        }
        return false;
    }
} 