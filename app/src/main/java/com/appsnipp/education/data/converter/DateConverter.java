package com.appsnipp.education.data.converter;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String getDateFormated(Long value) {
        return SDF.format(fromTimestamp(value));
    }
} 