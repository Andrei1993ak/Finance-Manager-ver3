package com.github.andrei1993ak.finances.util;

import android.database.Cursor;


public class CursorUtils {

    public static double getDouble(final Cursor cursor, final String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }

    public static String getString(final Cursor cursor, final String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public static Long getLong(final Cursor cursor, final String key) {
        return cursor.getLong(cursor.getColumnIndex(key));
    }

    public static Integer getInteger(final Cursor cursor, final String key) {
        return cursor.getInt(cursor.getColumnIndex(key));
    }

}
