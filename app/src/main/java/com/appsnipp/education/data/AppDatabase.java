/*
 * Copyright (c) 2020. rogergcc
 */

package com.appsnipp.education.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.appsnipp.education.data.dao.LessonStatusDao;
import com.appsnipp.education.data.dao.UserProgressDao;
import com.appsnipp.education.data.converter.DateConverter;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.model.UserProgress;

@Database(entities = {UserProgress.class, LessonStatus.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "education_db";
    private static AppDatabase INSTANCE;

    public abstract UserProgressDao userProgressDao();
    public abstract LessonStatusDao lessonStatusDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 