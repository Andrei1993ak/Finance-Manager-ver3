package com.gmail.a93ak.andrei19.finance30.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.models.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCategoryIncome implements DBHelperForModel<IncomeCategory> {

    public static final int usable = -1;
    public static final int hasChildrens = -2;

    private final DBHelper dbHelper;

    private static DBHelperCategoryIncome instance;

    public static DBHelperCategoryIncome getInstance() {
        if (instance == null)
            instance = new DBHelperCategoryIncome();
        return instance;
    }

    private DBHelperCategoryIncome() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final IncomeCategory incomeCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(IncomeCategory.NAME, incomeCategory.getName());
        values.put(IncomeCategory.PARENT_ID, incomeCategory.getParent_id());
        final long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(IncomeCategory.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public IncomeCategory get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setId(cursor.getLong(cursor.getColumnIndex(IncomeCategory.ID)));
            incomeCategory.setName(cursor.getString(cursor.getColumnIndex(IncomeCategory.NAME)));
            incomeCategory.setParent_id(cursor.getLong(cursor.getColumnIndex(IncomeCategory.PARENT_ID)));
            cursor.close();
            return incomeCategory;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final IncomeCategory incomeCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(IncomeCategory.NAME, incomeCategory.getName());
        values.put(IncomeCategory.PARENT_ID, incomeCategory.getParent_id());
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(IncomeCategory.class), values, IncomeCategory.ID + "=?", new String[]{String.valueOf(incomeCategory.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (get(id).getParent_id() == -1) {
            final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class) + " WHERE " + IncomeCategory.PARENT_ID + " = " + id + " LIMIT 1";
            final Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                cursor.close();
                return hasChildrens;
            } else {
                cursor.close();
            }
        }
        final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) + " WHERE " + Income.CATEGORY_ID + " = " + id + " LIMIT 1";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return usable;
        } else {
            cursor.close();
        }
        int count = 0;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(IncomeCategory.class), IncomeCategory.ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

//    private int deleteAllByParentId(final long id) {
//        final SQLiteDatabase db = dbHelper.getWritableDatabase();
//        int count;
//        try {
//            db.beginTransaction();
//            count = db.delete(TableQueryGenerator.getTableName(IncomeCategory.class), IncomeCategory.PARENT_ID + "=?", new String[]{String.valueOf(id)});
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//        return count;
//    }


    @Override
    public int deleteAll() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(IncomeCategory.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public List<IncomeCategory> getAllToList() {
        final List<IncomeCategory> list = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final IncomeCategory incomeCategory = new IncomeCategory();
                incomeCategory.setId(cursor.getLong(cursor.getColumnIndex(IncomeCategory.ID)));
                incomeCategory.setName(cursor.getString(cursor.getColumnIndex(IncomeCategory.NAME)));
                incomeCategory.setParent_id(cursor.getLong(cursor.getColumnIndex(IncomeCategory.PARENT_ID)));
                list.add(incomeCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getAllByParentId(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class) + " WHERE " + IncomeCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<IncomeCategory> getAllToListByParentId(final long id) {
        final List<IncomeCategory> list = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class) + " WHERE " + IncomeCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final IncomeCategory incomeCategory = new IncomeCategory();
                incomeCategory.setId(cursor.getLong(cursor.getColumnIndex(IncomeCategory.ID)));
                incomeCategory.setName(cursor.getString(cursor.getColumnIndex(IncomeCategory.NAME)));
                incomeCategory.setParent_id(cursor.getLong(cursor.getColumnIndex(IncomeCategory.PARENT_ID)));
                list.add(incomeCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
