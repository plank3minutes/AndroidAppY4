/*
 * Copyright (c) 2024. rogergcc
 */

package com.appsnipp.education.data;

import android.content.Context;

import com.appsnipp.education.ui.model.CourseCard;
import com.appsnipp.education.ui.model.MatchCourse;

import java.util.Collections;
import java.util.List;

/**
 * Created on mayo.
 * year 2024 .
 */
public class CoursesRepository {
    private final Context context;
    
    public CoursesRepository(Context context) {
        this.context = context;
    }
    
    public List<MatchCourse> matchedCourses() {
        try {
            return JsonDataRepository.getInstance(context).getMatchedCourses();
        } catch (Exception ex) {
            // Xử lý ngoại lệ nếu có
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public List<CourseCard> getCourseCards() {
        try {
            return JsonDataRepository.getInstance(context).getCourseCards();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
}
