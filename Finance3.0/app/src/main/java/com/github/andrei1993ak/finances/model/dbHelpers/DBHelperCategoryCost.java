package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;


public class DBHelperCategoryCost implements IDBHelperForModel<CostCategory> {

    private final DBHelper dbHelper;

    public DBHelperCategoryCost() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final CostCategory costCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = costCategory.convertToContentValues();
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new CostCategory().convertFromCursor(cursor);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
        final ContentValues values = costCategory.convertToContentValues();
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
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    return Constants.CATEGORY_HAS_CHILDS;
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE " + Cost.CATEGORY_ID + " = " + id + " LIMIT 1";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                return Constants.CATEGORY_USABLE;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final CostCategory costCategory = new CostCategory().convertFromCursor(cursor);
                    list.add(costCategory);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final CostCategory costCategory = new CostCategory().convertFromCursor(cursor);
                    list.add(costCategory);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}
