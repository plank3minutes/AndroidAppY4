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
                
                // Lấy quiz từ lesson nếu có
                Quiz quiz = null;
                if (lessonJson.has("quiz")) {
                    quiz = getQuizFromLessonJson(lessonJson.getJSONObject("quiz"), lessonId);
                }
                
                lessons.add(new Lesson(lessonId, title, content, videoUrl, quiz));
            }
        }
        return lessons;
    }

    private Quiz getQuizFromLessonJson(JSONObject quizJson, String lessonId) throws JSONException {
        String quizId = quizJson.getString("id");
        String title = quizJson.getString("title");
        List<Question> questions = new ArrayList<>();
        
        if (quizJson.has("questions")) {
            JSONArray questionsArray = quizJson.getJSONArray("questions");
            for (int j = 0; j < questionsArray.length(); j++) {
                JSONObject questionJson = questionsArray.getJSONObject(j);
                String questionId = questionJson.getString("id");
                String text = questionJson.getString("text");
                JSONArray optionsArray = questionJson.getJSONArray("options");
                List<String> options = new ArrayList<>();
                
                for (int k = 0; k < optionsArray.length(); k++) {
                    options.add(optionsArray.getString(k));
                }
                
                int correctIndex = questionJson.getInt("correctIndex");
                Question question = new Question(questionId, quizId, text, options, correctIndex);
                questions.add(question);
            }
        }
        
        return new Quiz(quizId, lessonId, questions, title);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            JSONArray coursesArray = JsonUtil.loadJSONArrayFromAsset(context, "courses.json");
            for (int i = 0; i < coursesArray.length(); i++) {
                JSONObject courseJson = coursesArray.getJSONObject(i);
                String id = courseJson.getString("id");
                String title = courseJson.getString("title");
                String description = courseJson.getString("description");
                String imageName = courseJson.getString("imageResource");
                int imageResource = context.getResources().getIdentifier(
                        imageName, "drawable", context.getPackageName());
                
                List<Lesson> lessons = getLessonsFromCourseJson(courseJson);
                courses.add(new Course(id, title, description, lessons, imageResource));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
    }
} 