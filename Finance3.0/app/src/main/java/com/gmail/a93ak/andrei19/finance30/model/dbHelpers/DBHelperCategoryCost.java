package com.gmail.a93ak.andrei19.finance30.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.CostCategory;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;


import java.util.ArrayList;
import java.util.List;


public class DBHelperCategoryCost implements DBHelperForModel<CostCategory> {


    private final DBHelper dbHelper;

    private static DBHelperCategoryCost instance;

    public static DBHelperCategoryCost getInstance() {
        if (instance == null)
            instance = new DBHelperCategoryCost();
        return instance;
    }

    private DBHelperCategoryCost() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final CostCategory costCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CostCategory.NAME, costCategory.getName());
        values.put(CostCategory.PARENT_ID, costCategory.getParent_id());
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
            costCategory.setId(cursor.getLong(0));
            costCategory.setName(cursor.getString(1));
            costCategory.setParent_id(cursor.getLong(2));
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
        values.put(CostCategory.PARENT_ID, costCategory.getParent_id());
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
        int count = 0;
        try {
            db.beginTransaction();
            if (get(id).getParent_id() == -1) {
                count += deleteAllByParentId(id);
            }
            count += db.delete(TableQueryGenerator.getTableName(CostCategory.class), CostCategory.ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    private int deleteAllByParentId(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(CostCategory.class), CostCategory.ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }


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
                costCategory.setId(cursor.getLong(0));
                costCategory.setName(cursor.getString(1));
                costCategory.setParent_id(cursor.getLong(2));
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
                costCategory.setId(cursor.getLong(0));
                costCategory.setName(cursor.getString(1));
                costCategory.setParent_id(cursor.getLong(2));
                list.add(costCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
