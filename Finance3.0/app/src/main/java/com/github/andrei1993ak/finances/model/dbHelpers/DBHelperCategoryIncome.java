package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCategoryIncome implements IDBHelperForModel<IncomeCategory> {

    private final DBHelper dbHelper;

    public DBHelperCategoryIncome() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final IncomeCategory incomeCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = incomeCategory.convertToContentValues();
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new IncomeCategory().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final IncomeCategory incomeCategory) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = incomeCategory.convertToContentValues();
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
        if (get(id).getParentId() == -1) {
            final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class)
                    + " WHERE " + IncomeCategory.PARENT_ID + " = " + id + " LIMIT 1";
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
        final String query = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) +
                " WHERE " + Income.CATEGORY_ID + " = " + id + " LIMIT 1";
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
            count = db.delete(TableQueryGenerator.getTableName(IncomeCategory.class), IncomeCategory.ID + "=?", new String[]{String.valueOf(id)});
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final IncomeCategory costCategory = new IncomeCategory().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class) + " WHERE " + IncomeCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<IncomeCategory> getAllToListByParentId(final long id) {
        final List<IncomeCategory> list = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(IncomeCategory.class)
                + " WHERE " + IncomeCategory.PARENT_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final IncomeCategory costCategory = new IncomeCategory().convertFromCursor(cursor);
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
