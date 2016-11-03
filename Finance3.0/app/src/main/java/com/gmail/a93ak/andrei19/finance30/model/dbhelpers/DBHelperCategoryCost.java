package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperInterface;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CostCategory;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCategoryCost implements DBHelperInterface<CostCategory> {


    private DBHelper dbHelper;

    private static DBHelperCategoryCost instance;

    public static DBHelperCategoryCost getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperCategoryCost(dbHelper);
        return instance;
    }

    private DBHelperCategoryCost(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(CostCategory costCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COST_CATEGORY_KEY_NAME, costCategory.getName());
        values.put(DBHelper.COST_CATEGORY_KEY_PARENT_ID, costCategory.getParent_id());
        long id;
        try {
            db.beginTransaction();
            id = db.insert(DBHelper.TABLE_COST_CATEGORIES, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public CostCategory get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COST_CATEGORIES + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            CostCategory costCategory = new CostCategory();
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
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COST_CATEGORIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(CostCategory costCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COST_CATEGORY_KEY_NAME, costCategory.getName());
        values.put(DBHelper.COST_CATEGORY_KEY_PARENT_ID, costCategory.getParent_id());
        int count;
        try {
            db.beginTransaction();
            count = db.update(DBHelper.TABLE_COST_CATEGORIES, values, DBHelper.COST_CATEGORY_KEY_ID + "=?", new String[]{String.valueOf(costCategory.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        try {
            db.beginTransaction();
            if (get(id).getParent_id() == -1) {
                count += deleteAllByParentId(id);
            }
            count += db.delete(DBHelper.TABLE_COST_CATEGORIES, DBHelper.COST_CATEGORY_KEY_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public int deleteAllByParentId(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(DBHelper.TABLE_COST_CATEGORIES, DBHelper.COST_CATEGORY_KEY_PARENT_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }


    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(DBHelper.TABLE_COST_CATEGORIES, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public List<CostCategory> getAllToList() {
        List<CostCategory> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COST_CATEGORIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CostCategory costCategory = new CostCategory();
                costCategory.setId(cursor.getLong(0));
                costCategory.setName(cursor.getString(1));
                costCategory.setParent_id(cursor.getLong(2));
                list.add(costCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getAllByParentId(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COST_CATEGORIES + " WHERE " + DBHelper.COST_CATEGORY_KEY_PARENT_ID + " = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<CostCategory> getAllToListByParentId(long id) {
        List<CostCategory> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COST_CATEGORIES + " WHERE " + DBHelper.COST_CATEGORY_KEY_PARENT_ID + " = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CostCategory costCategory = new CostCategory();
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
