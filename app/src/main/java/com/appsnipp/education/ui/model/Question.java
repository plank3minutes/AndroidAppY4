package com.appsnipp.education.ui.model;

import java.util.List;

public class Question {
    private String id;
    private String quizId;
    private String text;
    private List<String> options;
    private int correctIndex;

    public Question() {
    }

    public Question(String id, String quizId, String text, List<String> options, int correctIndex) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }
    
    // Thêm constructor để hỗ trợ dữ liệu từ JSON
    public Question(String text, List<String> options, int correctIndex) {
        this.id = String.valueOf(System.currentTimeMillis()); // Tạo id tạm thời
        this.quizId = null; // Sẽ được set sau khi biết quizId
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Question) {
            Question other = (Question) obj;
            return this.id.equals(other.id);
        }
        return false;
    }
} 