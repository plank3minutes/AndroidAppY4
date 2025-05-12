/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appsnipp.education.data.repository.ProgressRepository;
import com.appsnipp.education.ui.model.UserProgress;

import java.util.List;

public class ProgressViewModel extends AndroidViewModel {
    private final ProgressRepository repository;
    private final LiveData<List<UserProgress>> allUserProgress;

    public ProgressViewModel(@NonNull Application application) {
        super(application);
        repository = ProgressRepository.getInstance(application);
        allUserProgress = repository.getAllUserProgress();
    }

    public LiveData<List<UserProgress>> getAllUserProgress() {
        return allUserProgress;
    }

    public LiveData<UserProgress> getUserProgressByCourseId(String courseId) {
        return repository.getUserProgressByCourseId(courseId);
    }

    public LiveData<UserProgress> getLatestUserProgress() {
        return repository.getLatestUserProgress();
    }

    public void toggleMark(UserProgress progress) {
        progress.setMarked(!progress.isMarked());
        update(progress);
    }


    public void insert(UserProgress userProgress) {
        repository.insert(userProgress);
    }

    public void update(UserProgress userProgress) {
        repository.update(userProgress);
    }

    public void delete(UserProgress userProgress) {
        repository.delete(userProgress);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
} 