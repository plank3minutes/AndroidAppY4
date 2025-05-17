package com.appsnipp.education.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appsnipp.education.ui.model.UserProgress;

import java.util.List;

@Dao
public interface UserProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProgress userProgress);

    @Update
    void update(UserProgress userProgress);

    @Delete
    void delete(UserProgress userProgress);

    // Trong UserProgressDao.java
    @Query("SELECT * FROM user_progress WHERE courseId = :courseId LIMIT 1")
    UserProgress getUserProgressByCourseIdSync(String courseId);

    @Query("SELECT * FROM user_progress WHERE courseId = :courseId")
    LiveData<UserProgress> getUserProgressByCourseId(String courseId);

    @Query("SELECT * FROM user_progress")
    LiveData<List<UserProgress>> getAllUserProgress();

    @Query("DELETE FROM user_progress")
    void deleteAll();

    @Query("SELECT * FROM user_progress ORDER BY lastAccess DESC LIMIT 1")
    LiveData<UserProgress> getLatestUserProgress();

    @Query("SELECT COUNT(*) FROM user_progress u WHERE u.completedLessons > 0")
    LiveData<Integer> getCourseTaken();
}