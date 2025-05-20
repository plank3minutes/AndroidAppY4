

package com.appsnipp.education.ui.model;

import java.util.List;

public class CourseStat {
    public List<Course> completedCourses;
    public List<Integer> completedProgress;
    public List<Course> inProgressCourses;
    public List<Integer> inProgress;
    public List<Course> notJoinCourses;
    public List<Integer> notJoinProgress;

    public CourseStat(
            List<Course> completedCourses,
            List<Integer> completedProgress,
            List<Course> inProgressCourses,
            List<Integer> inProgress,
            List<Course> notJoinCourses,
            List<Integer> notJoinProgress
    ) {
        this.completedCourses = completedCourses;
        this.completedProgress = completedProgress;
        this.inProgressCourses = inProgressCourses;
        this.inProgress = inProgress;
        this.notJoinCourses = notJoinCourses;
        this.notJoinProgress = notJoinProgress;
    }
}
