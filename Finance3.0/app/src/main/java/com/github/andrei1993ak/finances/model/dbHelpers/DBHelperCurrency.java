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

public class DBHelperCurrency implements IDBHelperForModel<Currency> {

    private final DBHelper dbHelper;

    public DBHelperCurrency() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Currency currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = currency.convertToContentValues();
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new Currency().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Currency.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public List<Currency> getAllToList() {
        final List<Currency> currenciesList = new ArrayList<>();
        Cursor cursor = null;
        try {
            final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Currency.class);
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Currency currency = new Currency().convertFromCursor(cursor);
                    currenciesList.add(currency);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return currenciesList;
    }

    @Override
    public int update(final Currency currency) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = currency.convertToContentValues();
        final int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Currency.class), values,
                    Currency.ID + "=?", new String[]{String.valueOf(currency.getId())});
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return -1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        int count = -1;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(Currency.class), Currency.ID + " = " + id, null);
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
            count = db.delete(TableQueryGenerator.getTableName(Currency.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }
}
