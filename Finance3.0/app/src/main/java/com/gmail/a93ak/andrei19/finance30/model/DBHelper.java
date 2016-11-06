package com.gmail.a93ak.andrei19.finance30.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.a93ak.andrei19.finance30.model.models.Cost;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financePm";

    private static final int DATABASE_VERSION = 11;

    private static DBHelper instance;


    public static DBHelper getInstance(final Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    private DBHelper(final Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(Transfer.class));
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(Transfer.class));
        onCreate(sqLiteDatabase);
    }

    public int getNextId() {
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT SEQ FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TableQueryGenerator.getTableName(Cost.class) + "'", null);
        if (cursor.moveToFirst()) {
            final int nextId = cursor.getInt(0) + 1;
            cursor.close();
            return nextId;
        } else {
            cursor.close();
            return -1;
        }
    }

}
