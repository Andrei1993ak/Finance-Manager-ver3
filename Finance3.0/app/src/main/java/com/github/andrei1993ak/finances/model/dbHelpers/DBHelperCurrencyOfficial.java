package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCurrencyOfficial implements DBHelperForModel<CurrencyOfficial> {

    private final DBHelper dbHelper;

    private static DBHelperCurrencyOfficial instance;

    public static DBHelperCurrencyOfficial getInstance() {
        if (instance == null)
            instance = new DBHelperCurrencyOfficial();
        return instance;
    }

    private DBHelperCurrencyOfficial() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final CurrencyOfficial currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CurrencyOfficial.CODE, currency.getCode());
        values.put(CurrencyOfficial.NAME, currency.getName());
        final long id;
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
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final CurrencyOfficial currency = new CurrencyOfficial();
            currency.setId(cursor.getLong(cursor.getColumnIndex(CurrencyOfficial.ID)));
            currency.setCode(cursor.getString(cursor.getColumnIndex(CurrencyOfficial.CODE)));
            currency.setName(cursor.getString(cursor.getColumnIndex(CurrencyOfficial.NAME)));
            cursor.close();
            return currency;
        } else {
            cursor.close();
            return null;
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(CurrencyOfficial.class) + " ORDER BY " + CurrencyOfficial.NAME;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final CurrencyOfficial currency = new CurrencyOfficial();
                currency.setId(cursor.getLong(cursor.getColumnIndex(CurrencyOfficial.ID)));
                currency.setCode(cursor.getString(cursor.getColumnIndex(CurrencyOfficial.CODE)));
                currency.setName(cursor.getString(cursor.getColumnIndex(CurrencyOfficial.NAME)));
                currenciesList.add(currency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return currenciesList;
    }

    @Override
    public int update(final CurrencyOfficial currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CurrencyOfficial.CODE, currency.getCode());
        values.put(CurrencyOfficial.NAME, currency.getName());
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
