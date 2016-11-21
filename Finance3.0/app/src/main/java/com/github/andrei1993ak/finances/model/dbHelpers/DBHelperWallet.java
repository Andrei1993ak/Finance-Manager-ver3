package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Transfer;

import java.util.ArrayList;
import java.util.List;

public class DBHelperWallet implements IDBHelperForModel<Wallet> {

    public static final String CURRENCY_NAME = "currency";

    private final DBHelper dbHelper;

    public DBHelperWallet() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Wallet wallet) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Wallet.NAME, wallet.getName());
        values.put(Wallet.CURRENCY_ID, wallet.getCurrencyId());
        values.put(Wallet.AMOUNT, wallet.getAmount());

        final long id;
        try {
            db.beginTransaction();
            id = db.insert(TableQueryGenerator.getTableName(Wallet.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Wallet get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Wallet.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final Wallet wallet = new Wallet();
            wallet.setId(cursor.getLong(cursor.getColumnIndex(Wallet.ID)));
            wallet.setName(cursor.getString(cursor.getColumnIndex(Wallet.NAME)));
            wallet.setCurrencyId(cursor.getLong(cursor.getColumnIndex(Wallet.CURRENCY_ID)));
            wallet.setAmount(cursor.getDouble(cursor.getColumnIndex(Wallet.AMOUNT)));
            cursor.close();
            return wallet;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT " +
                "PU." + Wallet.ID + " as " + Wallet.ID + ", " +
                "PU." + Wallet.NAME + " as " + Wallet.NAME + ", " +
                "PU." + Wallet.AMOUNT + " as " + Wallet.AMOUNT + ", " +
                "CU." + Currency.CODE + " as " + CURRENCY_NAME + " " +
                "from " + TableQueryGenerator.getTableName(Wallet.class)
                + " as PU inner join " + TableQueryGenerator.getTableName(Currency.class) + " as CU "
                + "on PU." + Wallet.CURRENCY_ID + " = CU." + Currency.ID;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final Wallet wallet) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Wallet.NAME, wallet.getName());
        values.put(Wallet.CURRENCY_ID, wallet.getCurrencyId());
        values.put(Wallet.AMOUNT, wallet.getAmount());
        final int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Wallet.class), values,
                    Wallet.ID + "=?", new String[]{String.valueOf(wallet.getId())});
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
                "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE " + Cost.WALLET_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) + " WHERE " + Income.WALLET_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) + " WHERE " + Transfer.FROM_WALLET_ID + " = " + id +
                        " or " + Transfer.TO_WALLET_ID + " = " + id + " LIMIT 1"};
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
            count = db.delete(TableQueryGenerator.getTableName(Wallet.class), Wallet.ID + " = " + id, null);
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
            count = db.delete(TableQueryGenerator.getTableName(Wallet.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    void addAmount(final long id, final double amount) {
        final Wallet wallet = get(id);
        wallet.setAmount(wallet.getAmount() + amount);
        update(wallet);
    }

    void takeAmount(final long id, final double amount) {
        final Wallet wallet = get(id);
        wallet.setAmount(wallet.getAmount() - amount);
        update(wallet);
    }

    public List<Wallet> getAllToList() {
        final List<Wallet> wallets = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Wallet.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Wallet wallet = new Wallet();
                wallet.setId(cursor.getLong(cursor.getColumnIndex(Wallet.ID)));
                wallet.setName(cursor.getString(cursor.getColumnIndex(Wallet.NAME)));
                wallet.setCurrencyId(cursor.getLong(cursor.getColumnIndex(Wallet.CURRENCY_ID)));
                wallet.setAmount(cursor.getDouble(cursor.getColumnIndex(Wallet.AMOUNT)));
                wallets.add(wallet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wallets;
    }
}
