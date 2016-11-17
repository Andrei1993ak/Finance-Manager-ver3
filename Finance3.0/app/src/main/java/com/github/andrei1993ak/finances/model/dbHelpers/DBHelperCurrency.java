package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCurrency implements DBHelperForModel<Currency> {

    private final DBHelper dbHelper;

    private static DBHelperCurrency instance;

    public static DBHelperCurrency getInstance() {
        if (instance == null)
            instance = new DBHelperCurrency();
        return instance;
    }

    private DBHelperCurrency() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Currency currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Currency.CODE, currency.getCode());
        values.put(Currency.NAME, currency.getName());
        long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(Currency.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Currency get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Currency.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final Currency currency = new Currency();
            currency.setId(cursor.getLong(cursor.getColumnIndex(Currency.ID)));
            currency.setCode(cursor.getString(cursor.getColumnIndex(Currency.CODE)));
            currency.setName(cursor.getString(cursor.getColumnIndex(Currency.NAME)));
            cursor.close();
            return currency;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Currency.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<Currency> getAllToList() {
        final List<Currency> currenciesList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Currency.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Currency currency = new Currency();
                currency.setId(cursor.getLong(cursor.getColumnIndex(Currency.ID)));
                currency.setCode(cursor.getString(cursor.getColumnIndex(Currency.CODE)));
                currency.setName(cursor.getString(cursor.getColumnIndex(Currency.NAME)));
                currenciesList.add(currency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return currenciesList;
    }

    @Override
    public int update(final Currency currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Currency.CODE, currency.getCode());
        values.put(Currency.NAME, currency.getName());
        final int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Currency.class), values, Currency.ID + "=?", new String[]{String.valueOf(currency.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;

    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Wallet.class) + " WHERE " + Wallet.CURRENCY_ID + " = " + id + " LIMIT 1";
        final Cursor cursor = db.rawQuery(selectQuery, null);
        int count = -1;
        if (!cursor.moveToFirst()) {
            cursor.close();
            try {
                db.beginTransaction();
                count = db.delete(TableQueryGenerator.getTableName(Currency.class), Currency.ID + " = " + id, null);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } else {
            cursor.close();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(Currency.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

}
