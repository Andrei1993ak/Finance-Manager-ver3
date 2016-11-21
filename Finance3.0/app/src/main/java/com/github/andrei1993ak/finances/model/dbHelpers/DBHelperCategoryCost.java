package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.util.ContextHolder;


import java.util.ArrayList;
import java.util.List;


public class DBHelperCategoryCost implements IDBHelperForModel<CostCategory> {

    public static final int usable = -1;
    public static final int hasChildrens = -2;

    private final DBHelper dbHelper;

    public DBHelperCategoryCost() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final CostCategory costCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CostCategory.NAME, costCategory.getName());
        values.put(CostCategory.PARENT_ID, costCategory.getParentId());
        long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(CostCategory.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public CostCategory get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final CostCategory costCategory = new CostCategory();
            costCategory.setId(cursor.getLong(cursor.getColumnIndex(CostCategory.ID)));
            costCategory.setName(cursor.getString(cursor.getColumnIndex(CostCategory.NAME)));
            costCategory.setParentId(cursor.getLong(cursor.getColumnIndex(CostCategory.PARENT_ID)));
            cursor.close();
            return costCategory;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final CostCategory costCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CostCategory.NAME, costCategory.getName());
        values.put(CostCategory.PARENT_ID, costCategory.getParentId());
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(CostCategory.class), values, CostCategory.ID + "=?", new String[]{String.valueOf(costCategory.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (get(id).getParentId() == -1) {
            final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class) + " WHERE " + CostCategory.PARENT_ID + " = " + id + " LIMIT 1";
            final Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                cursor.close();
                return hasChildrens;
            } else {
                cursor.close();
            }
        }
        final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE " + Cost.CATEGORY_ID + " = " + id + " LIMIT 1";
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
            count = db.delete(TableQueryGenerator.getTableName(CostCategory.class), CostCategory.ID + "=?", new String[]{String.valueOf(id)});
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
//            count = db.delete(TableQueryGenerator.getTableName(CostCategory.class), CostCategory.ID + "=?", new String[]{String.valueOf(id)});
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
            count = db.delete(TableQueryGenerator.getTableName(CostCategory.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public List<CostCategory> getAllToList() {
        final List<CostCategory> list = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final CostCategory costCategory = new CostCategory();
                costCategory.setId(cursor.getLong(cursor.getColumnIndex(CostCategory.ID)));
                costCategory.setName(cursor.getString(cursor.getColumnIndex(CostCategory.NAME)));
                costCategory.setParentId(cursor.getLong(cursor.getColumnIndex(CostCategory.PARENT_ID)));
                list.add(costCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getAllByParentId(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class) + " WHERE " + CostCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<CostCategory> getAllToListByParentId(final long id) {
        final List<CostCategory> list = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CostCategory.class) + " WHERE " + CostCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final CostCategory costCategory = new CostCategory();
                costCategory.setId(cursor.getLong(cursor.getColumnIndex(CostCategory.ID)));
                costCategory.setName(cursor.getString(cursor.getColumnIndex(CostCategory.NAME)));
                costCategory.setParentId(cursor.getLong(cursor.getColumnIndex(CostCategory.PARENT_ID)));
                list.add(costCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
