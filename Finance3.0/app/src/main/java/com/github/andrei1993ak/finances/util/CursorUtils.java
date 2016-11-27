package com.github.andrei1993ak.finances.util;

import android.database.Cursor;

// TODO: rewrite it to static class
public class CursorUtils {

    public double getDouble(final Cursor cursor, final String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }

    public String getString(final Cursor cursor, final String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public Long getLong(final Cursor cursor, final String key) {
        return cursor.getLong(cursor.getColumnIndex(key));
    }

}
