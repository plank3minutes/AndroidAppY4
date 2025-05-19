package com.appsnipp.education.ui.listeners;

import com.appsnipp.education.ui.menuhome.SeeAllType;
import com.appsnipp.education.ui.model.Course;

public interface HomeCourseItemClickListener {
    void onCourseItemClick(Course course);

    void onSeeAllClick(SeeAllType type);
}
