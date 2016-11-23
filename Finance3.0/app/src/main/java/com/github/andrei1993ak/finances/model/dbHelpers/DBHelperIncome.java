package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;


public class DBHelperIncome implements IDBHelperForModel<Income> {

    private final DBHelper dbHelper;
    private final DBHelperWallet dbHelperWallet;

    public DBHelperIncome() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
    }

    @Override
    public long add(final Income income) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = income.convertToContentValues();
        long id;
        try {
            db.beginTransaction();
            dbHelperWallet.addAmount(income.getWalletId(), income.getAmount());
            id = db.insert(TableQueryGenerator.getTableName(Income.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Income get(final long id) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new Income().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(final Income income) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Income oldIncome = get(income.getId());
        final ContentValues values = income.convertToContentValues();
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Income.class), values, Income.ID + "=?", new String[]{String.valueOf(income.getId())});
            final Income newIncome = get(income.getId());
            if (oldIncome.getWalletId() != newIncome.getWalletId()) {
                dbHelperWallet.takeAmount(oldIncome.getWalletId(), oldIncome.getAmount());
                dbHelperWallet.addAmount(newIncome.getWalletId(), newIncome.getAmount());
            } else if (newIncome.getAmount() != oldIncome.getAmount()) {
                dbHelperWallet.addAmount(newIncome.getWalletId(), (newIncome.getAmount() - oldIncome.getAmount()));
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
        final Income income = get(id);
        int count;
        try {
            db.beginTransaction();
            dbHelperWallet.takeAmount(income.getWalletId(), income.getAmount());
            count = db.delete(TableQueryGenerator.getTableName(Income.class), Income.ID + " = " + id, null);
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
            count = db.delete(TableQueryGenerator.getTableName(Income.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public List<Income> getAllToList() {
        final List<Income> incomeList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Income income = new Income().convertFromCursor(cursor);
                    incomeList.add(income);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return incomeList;
    }

    public List<Income> getAllToListByCategoryId(final long id) {
        return null;
    }

    public List<Income> getAllToListByWalletId(final long id) {
        final List<Income> incomeList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) +
                " WHERE " + Income.WALLET_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Income income = new Income().convertFromCursor(cursor);
                    incomeList.add(income);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return incomeList;
    }

    public List<Income> getAllToListByDates(final long from, long to) {
        return null;
    }
}
