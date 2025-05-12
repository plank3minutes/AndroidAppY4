package com.appsnipp.education.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appsnipp.education.data.AppDatabase;
import com.appsnipp.education.data.dao.LessonStatusDao;
import com.appsnipp.education.ui.model.LessonStatus;

import java.util.List;

public class LessonStatusRepository {
    private static LessonStatusRepository instance;
    private final LessonStatusDao lessonStatusDao;

    private LessonStatusRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        lessonStatusDao = database.lessonStatusDao();
    }

    public static LessonStatusRepository getInstance(Context context) {
        if (instance == null) {
            instance = new LessonStatusRepository(context);
        }
        return instance;
    }

    public LiveData<List<LessonStatus>> getLessonStatusByCourseId(String courseId) {
        return lessonStatusDao.getLessonStatusByCourseId(courseId);
    }

    public LiveData<LessonStatus> getLessonStatus(String courseId, String lessonId) {
        return lessonStatusDao.getLessonStatus(courseId, lessonId);
    }

    public LiveData<List<LessonStatus>> getCompletedLessons(String courseId) {
        return lessonStatusDao.getCompletedLessons(courseId);
    }

    public void insert(LessonStatus lessonStatus) {
        new InsertLessonStatusAsyncTask(lessonStatusDao).execute(lessonStatus);
    }

    public void update(LessonStatus lessonStatus) {
        new UpdateLessonStatusAsyncTask(lessonStatusDao).execute(lessonStatus);
    }

    public void completeLesson(String courseId, String lessonId, int quizScore) {
        new CompleteLessonAsyncTask(lessonStatusDao).execute(
                new LessonCompletion(courseId, lessonId, quizScore));
    }

    public void delete(LessonStatus lessonStatus) {
        new DeleteLessonStatusAsyncTask(lessonStatusDao).execute(lessonStatus);
    }

    public void deleteByCourseId(String courseId) {
        new DeleteByCourseIdAsyncTask(lessonStatusDao).execute(courseId);
    }

    public void deleteAll() {
        new DeleteAllLessonStatusAsyncTask(lessonStatusDao).execute();
    }

    public void completeQuiz(String courseId, String lessonId, int quizScore) {
        new CompleteQuizAsyncTask(lessonStatusDao).execute(
                new LessonCompletion(courseId, lessonId, quizScore));
    }

    private static class CompleteQuizAsyncTask extends AsyncTask<LessonCompletion, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private CompleteQuizAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonCompletion... completions) {
            LessonCompletion completion = completions[0];
            LessonStatus status = lessonStatusDao.getLessonStatusSync(completion.courseId, completion.lessonId);
            if (status != null) {
                status.setQuizScore(completion.quizScore);
                lessonStatusDao.update(status);
            } else {
                status = new LessonStatus();
                status.setCourseId(completion.courseId);
                status.setLessonId(completion.lessonId);
                status.setCompleted(false);
                status.setQuizScore(completion.quizScore);
                lessonStatusDao.insert(status);
            }
            return null;
        }
    }

    public void completeLessonWithoutQuiz(String courseId, String lessonId) {
        new CompleteLessonWithoutQuizzAsyncTask(lessonStatusDao).execute(
                new LessonCompletion(courseId, lessonId, -1));
    }

    private static class CompleteLessonWithoutQuizzAsyncTask extends AsyncTask<LessonCompletion, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private CompleteLessonWithoutQuizzAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonCompletion... completions) {
            LessonCompletion completion = completions[0];
            LessonStatus status = lessonStatusDao.getLessonStatusSync(completion.courseId, completion.lessonId);
            if (status != null) {
                status.setCompleted(true);
                status.setCompletedAt(System.currentTimeMillis());
                lessonStatusDao.update(status);
            } else {
                status = new LessonStatus(
                        completion.courseId,
                        completion.lessonId,
                        -1, // No quiz score
                        true,
                        System.currentTimeMillis()
                );
                lessonStatusDao.insert(status);
            }
            return null;
        }
    }


    private static class InsertLessonStatusAsyncTask extends AsyncTask<LessonStatus, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private InsertLessonStatusAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonStatus... lessonStatuses) {
            lessonStatusDao.insert(lessonStatuses[0]);
            return null;
        }
    }

    private static class UpdateLessonStatusAsyncTask extends AsyncTask<LessonStatus, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private UpdateLessonStatusAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonStatus... lessonStatuses) {
            lessonStatusDao.update(lessonStatuses[0]);
            return null;
        }
    }

    private static class CompleteLessonAsyncTask extends AsyncTask<LessonCompletion, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private CompleteLessonAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonCompletion... completions) {
            LessonCompletion completion = completions[0];
            LessonStatus status = lessonStatusDao.getLessonStatusSync(completion.courseId, completion.lessonId);
            if (status != null) {
                status.setCompleted(true);
                status.setQuizScore(completion.quizScore);
                status.setCompletedAt(System.currentTimeMillis());
                lessonStatusDao.update(status);
            } else {
                status = new LessonStatus(
                        completion.courseId,
                        completion.lessonId,
                        completion.quizScore,
                        true,
                        System.currentTimeMillis()
                );
                lessonStatusDao.insert(status);
            }
            return null;
        }
    }

    private static class DeleteLessonStatusAsyncTask extends AsyncTask<LessonStatus, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private DeleteLessonStatusAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(LessonStatus... lessonStatuses) {
            lessonStatusDao.delete(lessonStatuses[0]);
            return null;
        }
    }

    private static class DeleteByCourseIdAsyncTask extends AsyncTask<String, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private DeleteByCourseIdAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(String... courseIds) {
            lessonStatusDao.deleteByCourseId(courseIds[0]);
            return null;
        }
    }

    private static class DeleteAllLessonStatusAsyncTask extends AsyncTask<Void, Void, Void> {
        private final LessonStatusDao lessonStatusDao;

        private DeleteAllLessonStatusAsyncTask(LessonStatusDao lessonStatusDao) {
            this.lessonStatusDao = lessonStatusDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            lessonStatusDao.deleteAll();
            return null;
        }
    }

    private static class LessonCompletion {
        final String courseId;
        final String lessonId;
        final int quizScore;

        LessonCompletion(String courseId, String lessonId, int quizScore) {
            this.courseId = courseId;
            this.lessonId = lessonId;
            this.quizScore = quizScore;
        }
    }
} 