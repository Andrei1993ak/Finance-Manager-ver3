package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;

import java.util.ArrayList;
import java.util.List;


public class DBHelperCost implements DBHelperForModel<Cost> {

    private final DBHelper dbHelper;

    private static DBHelperCost instance;

    public static DBHelperCost getInstance() {
        if (instance == null)
            instance = new DBHelperCost();
        return instance;
    }

    private DBHelperCost() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Cost cost) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Cost.NAME, cost.getName());
        values.put(Cost.WALLET_ID, cost.getWalletId());
        values.put(Cost.AMOUNT, cost.getAmount());
        values.put(Cost.CATEGORY_ID, cost.getCategoryId());
        values.put(Cost.DATE, cost.getDate());
        values.put(Cost.PHOTO, cost.getPhoto());
        long id;
        try {
            db.beginTransaction();
            final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
            dbHelperWallet.takeAmount(cost.getWalletId(), cost.getAmount());
            id = db.insert(TableQueryGenerator.getTableName(Cost.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    //TODO public Cost get(final long id, Class<?> clazz) {
    public Cost get(final long id) {
        //TODO getTable from clazz
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final Cost cost = new Cost();
            //TODO generate fields from annotated members
            cost.setId(cursor.getLong(cursor.getColumnIndex(Cost.ID)));
            cost.setName(cursor.getString(cursor.getColumnIndex(Cost.NAME)));
            cost.setWalletId(cursor.getLong(cursor.getColumnIndex(Cost.WALLET_ID)));
            cost.setAmount(cursor.getDouble(cursor.getColumnIndex(Cost.AMOUNT)));
            cost.setCategoryId(cursor.getLong(cursor.getColumnIndex(Cost.CATEGORY_ID)));
            cost.setDate(cursor.getLong(cursor.getColumnIndex(Cost.DATE)));
            cost.setPhoto(cursor.getInt(cursor.getColumnIndex(Cost.PHOTO)));
            cursor.close();
            return cost;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final Cost cost) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cost oldCost = get(cost.getId());
        final ContentValues values = new ContentValues();
        values.put(Cost.NAME, cost.getName());
        values.put(Cost.WALLET_ID, cost.getWalletId());
        values.put(Cost.AMOUNT, cost.getAmount());
        values.put(Cost.CATEGORY_ID, cost.getCategoryId());
        values.put(Cost.DATE, cost.getDate());
        values.put(Cost.PHOTO, cost.getPhoto());
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Cost.class), values, Cost.ID + "=?", new String[]{String.valueOf(cost.getId())});
            final Cost newCost = get(cost.getId());
            final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
            if (oldCost.getWalletId() != newCost.getWalletId()) {
                dbHelperWallet.addAmount(oldCost.getWalletId(), oldCost.getAmount());
                dbHelperWallet.takeAmount(newCost.getWalletId(), newCost.getAmount());
            } else if (newCost.getAmount() != oldCost.getAmount()) {
                dbHelperWallet.takeAmount(newCost.getWalletId(), (newCost.getAmount() - oldCost.getAmount()));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
        final Cost cost = get(id);
        int count;
        try {
            db.beginTransaction();
            dbHelperWallet.addAmount(cost.getWalletId(), cost.getAmount());
            count = db.delete(TableQueryGenerator.getTableName(Cost.class), Cost.ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TableQueryGenerator.getTableName(Cost.class), null, null);
    }

    public List<Cost> getAllToList() {
        final List<Cost> costList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Cost cost = new Cost();
                cost.setId(cursor.getLong(cursor.getColumnIndex(Cost.ID)));
                cost.setName(cursor.getString(cursor.getColumnIndex(Cost.NAME)));
                cost.setWalletId(cursor.getLong(cursor.getColumnIndex(Cost.WALLET_ID)));
                cost.setAmount(cursor.getDouble(cursor.getColumnIndex(Cost.AMOUNT)));
                cost.setCategoryId(cursor.getLong(cursor.getColumnIndex(Cost.CATEGORY_ID)));
                cost.setDate(cursor.getLong(cursor.getColumnIndex(Cost.DATE)));
                cost.setPhoto(cursor.getInt(cursor.getColumnIndex(Cost.PHOTO)));
                costList.add(cost);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return costList;
    }

    public List<Cost> getAllToListByCategoryId(final long id) {
        return null;
    }

    public List<Cost> getAllToListByWalletId(final long id) {
        final List<Cost> incomeList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) +
                " WHERE " + Cost.WALLET_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Cost income = new Cost();
                income.setId(cursor.getLong(cursor.getColumnIndex(Cost.ID)));
                income.setName(cursor.getString(cursor.getColumnIndex(Cost.NAME)));
                income.setWalletId(cursor.getLong(cursor.getColumnIndex(Cost.WALLET_ID)));
                income.setAmount(cursor.getDouble(cursor.getColumnIndex(Cost.AMOUNT)));
                income.setCategoryId(cursor.getLong(cursor.getColumnIndex(Cost.CATEGORY_ID)));
                income.setDate(cursor.getLong(cursor.getColumnIndex(Cost.DATE)));
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return incomeList;
    }

    public List<Cost> getAllToListByDates(final long from, final long to) {
        return null;
    }
}
