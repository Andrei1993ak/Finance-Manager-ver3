package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Purse;
import com.github.andrei1993ak.finances.model.models.Transfer;

import java.util.ArrayList;
import java.util.List;

public class DBHelperPurse implements DBHelperForModel<Purse> {

    public static final String CURRENCY_NAME = "currency";

    private final DBHelper dbHelper;

    private static DBHelperPurse instance;

    public static DBHelperPurse getInstance() {
        if (instance == null)
            instance = new DBHelperPurse();
        return instance;
    }

    private DBHelperPurse() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Purse purse) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Purse.NAME, purse.getName());
        values.put(Purse.CURRENCY_ID, purse.getCurrencyId());
        values.put(Purse.AMOUNT, purse.getAmount());

        final long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(Purse.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Purse get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Purse.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final Purse purse = new Purse();
            purse.setId(cursor.getLong(cursor.getColumnIndex(Purse.ID)));
            purse.setName(cursor.getString(cursor.getColumnIndex(Purse.NAME)));
            purse.setCurrencyId(cursor.getLong(cursor.getColumnIndex(Purse.CURRENCY_ID)));
            purse.setAmount(cursor.getDouble(cursor.getColumnIndex(Purse.AMOUNT)));
            cursor.close();
            return purse;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT " +
                "PU." + Purse.ID + " as " + Purse.ID + ", " +
                "PU." + Purse.NAME + " as " + Purse.NAME + ", " +
                "PU." + Purse.AMOUNT + " as " + Purse.AMOUNT + ", " +
                "CU." + Currency.CODE + " as " + CURRENCY_NAME + " " +
                "from " + TableQueryGenerator.getTableName(Purse.class)
                + " as PU inner join " + TableQueryGenerator.getTableName(Currency.class) + " as CU "
                + "on PU." + Purse.CURRENCY_ID + " = CU." + Currency.ID;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final Purse purse) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Purse.NAME, purse.getName());
        values.put(Purse.CURRENCY_ID, purse.getCurrencyId());
        values.put(Purse.AMOUNT, purse.getAmount());
        final int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Purse.class), values,
                    Purse.ID + "=?", new String[]{String.valueOf(purse.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String[] querys = {
                "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE " + Cost.PURSE_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) + " WHERE " + Income.PURSE_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) + " WHERE " + Transfer.FROM_PURSE_ID + " = " + id +
                        " or " + Transfer.TO_PURSE_ID + " = " + id + " LIMIT 1"};
        for (final String query : querys) {
            final Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                cursor.close();
                return -1;
            } else {
                cursor.close();
            }
        }
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(Purse.class), Purse.ID + " = " + id, null);
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
            count = db.delete(TableQueryGenerator.getTableName(Purse.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    void addAmount(final long id, final double amount) {
        final Purse purse = get(id);
        purse.setAmount(purse.getAmount() + amount);
        update(purse);
    }

    void takeAmount(final long id, final double amount) {
        final Purse purse = get(id);
        purse.setAmount(purse.getAmount() - amount);
        update(purse);
    }

    public List<Purse> getAllToList() {
        final List<Purse> pursesList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Purse.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Purse purse = new Purse();
                purse.setId(cursor.getLong(cursor.getColumnIndex(Purse.ID)));
                purse.setName(cursor.getString(cursor.getColumnIndex(Purse.NAME)));
                purse.setCurrencyId(cursor.getLong(cursor.getColumnIndex(Purse.CURRENCY_ID)));
                purse.setAmount(cursor.getDouble(cursor.getColumnIndex(Purse.AMOUNT)));
                pursesList.add(purse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pursesList;
    }
}
