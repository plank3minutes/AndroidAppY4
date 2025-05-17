package com.appsnipp.education.ui.model;

public class Lesson {
    private String id;
    private String courseId;
    private String title;
    private String content;
    private String videoUrl;
    private Quiz quiz;

    public Lesson() {
    }

    public Lesson(String id, String courseId, String title, String content, String videoUrl) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
    }
    
    // Thêm constructor mới để phù hợp với dữ liệu từ JSON
    public Lesson(String id, String title, String content, String videoUrl, Quiz quiz) {
        this.id = id;
        this.courseId = null; // Sẽ được set sau khi biết courseId
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.quiz = quiz;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lesson) {
            Lesson other = (Lesson) obj;
            return this.id.equals(other.id);
        }
        return false;
    }
} 