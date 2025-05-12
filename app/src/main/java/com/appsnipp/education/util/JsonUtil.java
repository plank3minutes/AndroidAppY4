package com.appsnipp.education.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonUtil {

    /**
     * Đọc file JSON từ thư mục assets
     * @param context Context của ứng dụng
     * @param fileName Tên file JSON cần đọc
     * @return JSONArray đọc được từ file
     */
    public static JSONArray loadJSONArrayFromAsset(Context context, String fileName) {
        String jsonStr = loadJSONStringFromAsset(context, fileName);
        try {
            if (jsonStr != null) {
                return new JSONArray(jsonStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * Đọc file JSON từ thư mục assets dưới dạng String
     * @param context Context của ứng dụng
     * @param fileName Tên file JSON cần đọc
     * @return Chuỗi JSON
     */
    public static String loadJSONStringFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
} 