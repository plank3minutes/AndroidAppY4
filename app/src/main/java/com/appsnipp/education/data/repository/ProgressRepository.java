/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.data.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.appsnipp.education.data.AppDatabase;
import com.appsnipp.education.data.dao.UserProgressDao;
import com.appsnipp.education.ui.model.UserProgress;

import java.util.Date;
import java.util.List;

public class ProgressRepository {
    private static ProgressRepository instance;
    private final UserProgressDao userProgressDao;
    private final LiveData<List<UserProgress>> allUserProgress;

    private ProgressRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userProgressDao = database.userProgressDao();
        allUserProgress = userProgressDao.getAllUserProgress();
    }

    public static ProgressRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ProgressRepository(context);
        }
        return instance;
    }

    public LiveData<List<UserProgress>> getAllUserProgress() {
        return allUserProgress;
    }

    public LiveData<UserProgress> getUserProgressByCourseId(String courseId) {
        return userProgressDao.getUserProgressByCourseId(courseId);
    }

    public LiveData<UserProgress> getLatestUserProgress() {
        return userProgressDao.getLatestUserProgress();
    }

    public void insert(UserProgress userProgress) {
        userProgress.setLastAccess(new Date());
        new InsertUserProgressAsyncTask(userProgressDao).execute(userProgress);
    }

    public void update(UserProgress userProgress) {
        userProgress.setLastAccess(new Date());
        new UpdateUserProgressAsyncTask(userProgressDao).execute(userProgress);
    }

    public void updateProgress(String courseId, int totalLessons, int completedLessons) {
        new UpdateProgressAsyncTask(userProgressDao).execute(new ProgressUpdate(courseId, totalLessons, completedLessons));
    }

    public void toggleMark(String courseId, boolean isMarked) {
        new ToggleMarkAsyncTask(userProgressDao).execute(new MarkUpdate(courseId, isMarked));
    }

    public void delete(UserProgress userProgress) {
        new DeleteUserProgressAsyncTask(userProgressDao).execute(userProgress);
    }

    public void deleteAll() {
        new DeleteAllUserProgressAsyncTask(userProgressDao).execute();
    }

    public void updateLastAccess(String courseId) {
        new UpdateLastAccessAsyncTask(userProgressDao).execute(courseId);
    }

    private static class UpdateLastAccessAsyncTask extends AsyncTask<String, Void, Void> {
        private final UserProgressDao userProgressDao;

        private UpdateLastAccessAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(String... courseIds) {
            String courseId = courseIds[0];
            UserProgress progress = userProgressDao.getUserProgressByCourseIdSync(courseId);
            if (progress != null) {
                progress.setLastAccess(new Date());
                userProgressDao.update(progress);
            }
            return null;
        }
    }

    private static class InsertUserProgressAsyncTask extends AsyncTask<UserProgress, Void, Void> {
        private final UserProgressDao userProgressDao;

        private InsertUserProgressAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(UserProgress... userProgresses) {
            userProgressDao.insert(userProgresses[0]);
            return null;
        }
    }

    private static class UpdateUserProgressAsyncTask extends AsyncTask<UserProgress, Void, Void> {
        private final UserProgressDao userProgressDao;

        private UpdateUserProgressAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(UserProgress... userProgresses) {
            userProgressDao.update(userProgresses[0]);
            return null;
        }
    }

    private static class UpdateProgressAsyncTask extends AsyncTask<ProgressUpdate, Void, Void> {
        private final UserProgressDao userProgressDao;

        private UpdateProgressAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(ProgressUpdate... updates) {
            ProgressUpdate update = updates[0];
            UserProgress progress = userProgressDao.getUserProgressByCourseIdSync(update.courseId);
            if (progress != null) {
                progress.setTotalLessons(update.totalLessons);
                progress.setCompletedLessons(update.completedLessons);
                progress.setLastAccess(new Date());
                userProgressDao.update(progress);
            }
            return null;
        }
    }

    private static class ToggleMarkAsyncTask extends AsyncTask<MarkUpdate, Void, Void> {
        private final UserProgressDao userProgressDao;

        private ToggleMarkAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(MarkUpdate... updates) {
            MarkUpdate update = updates[0];
            UserProgress progress = userProgressDao.getUserProgressByCourseIdSync(update.courseId);
            if (progress != null) {
                progress.setMarked(update.isMarked);
                progress.setLastAccess(new Date());
                userProgressDao.update(progress);
            }
            return null;
        }
    }

    private static class DeleteUserProgressAsyncTask extends AsyncTask<UserProgress, Void, Void> {
        private final UserProgressDao userProgressDao;

        private DeleteUserProgressAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(UserProgress... userProgresses) {
            userProgressDao.delete(userProgresses[0]);
            return null;
        }
    }

    private static class DeleteAllUserProgressAsyncTask extends AsyncTask<Void, Void, Void> {
        private final UserProgressDao userProgressDao;

        private DeleteAllUserProgressAsyncTask(UserProgressDao userProgressDao) {
            this.userProgressDao = userProgressDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userProgressDao.deleteAll();
            return null;
        }
    }

    private static class ProgressUpdate {
        final String courseId;
        final int totalLessons;
        final int completedLessons;

        ProgressUpdate(String courseId, int totalLessons, int completedLessons) {
            this.courseId = courseId;
            this.totalLessons = totalLessons;
            this.completedLessons = completedLessons;
        }
    }

    private static class MarkUpdate {
        final String courseId;
        final boolean isMarked;

        MarkUpdate(String courseId, boolean isMarked) {
            this.courseId = courseId;
            this.isMarked = isMarked;
        }
    }
} 