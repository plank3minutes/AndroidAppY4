package com.appsnipp.education.data;

import android.content.Context;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.Quiz;
import com.appsnipp.education.ui.model.Question;
import com.appsnipp.education.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonDataRepository {
    private static JsonDataRepository instance;
    private final Context context;

    private JsonDataRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized JsonDataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new JsonDataRepository(context);
        }
        return instance;
    }

    private List<Lesson> getLessonsFromCourseJson(JSONObject courseJson) throws JSONException {
        List<Lesson> lessons = new ArrayList<>();
        if (courseJson.has("lessons")) {
            JSONArray lessonsArray = courseJson.getJSONArray("lessons");
            for (int j = 0; j < lessonsArray.length(); j++) {
                JSONObject lessonJson = lessonsArray.getJSONObject(j);
                String lessonId = lessonJson.getString("id");
                String title = lessonJson.getString("title");
                String content = lessonJson.getString("content");
                String videoUrl = lessonJson.getString("videoUrl");
                boolean isBookmarked = lessonJson.getBoolean("isBookmarked");
                
                lessons.add(new Lesson(lessonId, title, content, videoUrl, isBookmarked));
            }
        }
        return lessons;
    }

    private Quiz getQuizFromCourseJson(JSONObject courseJson, String courseId) throws JSONException {
        if (courseJson.has("quiz")) {
            JSONObject quizJson = courseJson.getJSONObject("quiz");
            List<Question> questions = new ArrayList<>();
            
            if (quizJson.has("questions")) {
                JSONArray questionsArray = quizJson.getJSONArray("questions");
                for (int j = 0; j < questionsArray.length(); j++) {
                    JSONObject questionJson = questionsArray.getJSONObject(j);
                    String text = questionJson.getString("text");
                    JSONArray optionsArray = questionJson.getJSONArray("options");
                    List<String> options = new ArrayList<>();
                    
                    for (int k = 0; k < optionsArray.length(); k++) {
                        options.add(optionsArray.getString(k));
                    }
                    
                    int correctIndex = questionJson.getInt("correctIndex");
                    questions.add(new Question(text, options, correctIndex));
                }
            }
            
            return new Quiz(courseId, "Quiz for " + courseJson.getString("title"), questions);
        }
        return null;
    }

    public List<String> getCourseIds() {
        List<String> courseIds = new ArrayList<>();
        try {
            JSONArray jsonArray = JsonUtil.loadJSONArrayFromAsset(context, "courses.json");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject courseJson = jsonArray.getJSONObject(i);
                courseIds.add(courseJson.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courseIds;
    }

    public Course getCourseById(String courseId) {
        try {
            JSONArray coursesArray = JsonUtil.loadJSONArrayFromAsset(context, "courses.json");
            for (int i = 0; i < coursesArray.length(); i++) {
                JSONObject courseJson = coursesArray.getJSONObject(i);
                String id = courseJson.getString("id");
                if (id.equals(courseId)) {
                    String title = courseJson.getString("title");
                    String description = courseJson.getString("description");
                    String imageName = courseJson.getString("imageResource");
                    int imageResource = context.getResources().getIdentifier(
                            imageName, "drawable", context.getPackageName());
                    
                    List<Lesson> lessons = getLessonsFromCourseJson(courseJson);
                    Quiz quiz = getQuizFromCourseJson(courseJson, id);
                    
                    Course course = new Course(id, title, description, lessons, imageResource);
                    course.setQuiz(quiz);
                    return course;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
} 