package com.appsnipp.education.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.model.UserProgress;

import java.util.List;

@Dao
public interface LessonStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LessonStatus lessonStatus);

    @Update
    void update(LessonStatus lessonStatus);

    @Delete
    void delete(LessonStatus lessonStatus);

    @Query("SELECT * FROM lesson_status WHERE courseId = :courseId AND lessonId = :lessonId LIMIT 1")
    LessonStatus getLessonStatusSync(String courseId, String lessonId);

    @Query("SELECT * FROM lesson_status WHERE courseId = :courseId")
    LiveData<List<LessonStatus>> getLessonStatusByCourseId(String courseId);

    @Query("SELECT * FROM lesson_status WHERE courseId = :courseId AND lessonId = :lessonId")
    LiveData<LessonStatus> getLessonStatus(String courseId, String lessonId);

    @Query("SELECT * FROM lesson_status WHERE courseId = :courseId AND isCompleted = 1")
    LiveData<List<LessonStatus>> getCompletedLessons(String courseId);

    @Query("DELETE FROM lesson_status WHERE courseId = :courseId")
    void deleteByCourseId(String courseId);

    @Query("DELETE FROM lesson_status")
    void deleteAll();
} 