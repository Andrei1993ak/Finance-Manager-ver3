package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;

import java.util.ArrayList;
import java.util.List;


public class DBHelperCost implements IDBHelperForModel<Cost> {

    private final DBHelper dbHelper;
    private final DBHelperWallet dbHelperWallet;

    public DBHelperCost() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
    }

    @Override
    public long add(final Cost cost) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = cost.convertToContentValues();
        long id;
        try {
            db.beginTransaction();
            dbHelperWallet.takeAmount(cost.getWalletId(), cost.getAmount());
            id = db.insert(TableQueryGenerator.getTableName(Cost.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Cost get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new Cost().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final Cost cost) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cost oldCost = get(cost.getId());
        final ContentValues values = cost.convertToContentValues();
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Cost.class), values, Cost.ID + "=?", new String[]{String.valueOf(cost.getId())});
            final Cost newCost = get(cost.getId());
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
        final Cost oldCost = get(id);
        int count;
        try {
            db.beginTransaction();
            dbHelperWallet.addAmount(oldCost.getWalletId(), oldCost.getAmount());
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
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(Cost.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public List<Cost> getAllToList() {
        final List<Cost> costList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Cost cost = new Cost().convertFromCursor(cursor);
                    costList.add(cost);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return costList;
    }

    public List<Cost> getAllToListByCategoryId(final long id) {
        return null;
    }

    public List<Cost> getAllToListByWalletId(final long id) {
        final List<Cost> costs = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) +
                " WHERE " + Cost.WALLET_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Cost cost = new Cost().convertFromCursor(cursor);
                    costs.add(cost);
                } while (cursor.moveToNext());
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return costs;
    }

    public List<Cost> getAllToListByDates(final long from, final long to) {
        return null;
    }
}
