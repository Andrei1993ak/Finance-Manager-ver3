package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCurrencyOfficial implements IDBHelperForModel<CurrencyOfficial> {

    private final DBHelper dbHelper;

    public DBHelperCurrencyOfficial() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final CurrencyOfficial currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = currency.convertToContentValues();
        long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(CurrencyOfficial.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public CurrencyOfficial get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CurrencyOfficial.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new CurrencyOfficial().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CurrencyOfficial.class) + " ORDER BY " + CurrencyOfficial.NAME;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public List<CurrencyOfficial> getAllToList() {
        final List<CurrencyOfficial> currenciesList = new ArrayList<>();
        Cursor cursor = null;
        try{
            final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CurrencyOfficial.class) + " ORDER BY " + CurrencyOfficial.NAME;
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final CurrencyOfficial currency = new CurrencyOfficial().convertFromCursor(cursor);
                    currenciesList.add(currency);
                } while (cursor.moveToNext());
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return currenciesList;
    }

    @Override
    public int update(final CurrencyOfficial currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = currency.convertToContentValues();
        final int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(CurrencyOfficial.class), values, CurrencyOfficial.ID + "=?", new String[]{String.valueOf(currency.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(CurrencyOfficial.class), CurrencyOfficial.ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(CurrencyOfficial.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

}
