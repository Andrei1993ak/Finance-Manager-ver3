package com.gmail.a93ak.andrei19.finance30.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;


public class DBHelperIncome implements DBHelperForModel<Income> {

    private final DBHelper dbHelper;

    private static DBHelperIncome instance;

    public static DBHelperIncome getInstance() {
        if (instance == null)
            instance = new DBHelperIncome();
        return instance;
    }

    private DBHelperIncome() {

        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Income income) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Income.NAME, income.getName());
        values.put(Income.PURSE_ID, income.getpurseId());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.CATEGORY_ID, income.getCategoryId());
        values.put(Income.DATE, income.getDate());
        long id;
        try {
            db.beginTransaction();
            final DBHelperPurse helperPurse = DBHelperPurse.getInstance();
            helperPurse.addAmount(income.getpurseId(), income.getAmount());
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
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            final Income income = new Income();
            income.setId(cursor.getLong(cursor.getColumnIndex(Income.ID)));
            income.setName(cursor.getString(cursor.getColumnIndex(Income.NAME)));
            income.setPurseId(cursor.getLong(cursor.getColumnIndex(Income.PURSE_ID)));
            income.setAmount(cursor.getDouble(cursor.getColumnIndex(Income.AMOUNT)));
            income.setCategoryId(cursor.getLong(cursor.getColumnIndex(Income.CATEGORY_ID)));
            income.setDate(cursor.getLong(cursor.getColumnIndex(Income.DATE)));
            cursor.close();
            return income;
        } else {
            cursor.close();
            return null;
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
        final ContentValues values = new ContentValues();
        values.put(Income.NAME, income.getName());
        values.put(Income.PURSE_ID, income.getpurseId());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.CATEGORY_ID, income.getCategoryId());
        values.put(Income.DATE, income.getDate());
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Income.class), values, Income.ID + "=?", new String[]{String.valueOf(income.getId())});
            final Income newIncome = get(income.getId());
            final DBHelperPurse helperPurse = DBHelperPurse.getInstance();
            if (oldIncome.getpurseId() != newIncome.getpurseId()) {
                helperPurse.takeAmount(oldIncome.getpurseId(), oldIncome.getAmount());
                helperPurse.addAmount(newIncome.getpurseId(), newIncome.getAmount());
            } else if (newIncome.getAmount() != oldIncome.getAmount()) {
                helperPurse.addAmount(newIncome.getpurseId(), (newIncome.getAmount() - oldIncome.getAmount()));
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
        final DBHelperPurse helperPurse = DBHelperPurse.getInstance();
        final Income income = get(id);
        int count;
        try {
            db.beginTransaction();
            helperPurse.takeAmount(income.getpurseId(), income.getAmount());
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
        return db.delete(TableQueryGenerator.getTableName(Income.class), null, null);
    }

    public List<Income> getAllToList() {
        final List<Income> incomeList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Income income = new Income();
                income.setId(cursor.getLong(cursor.getColumnIndex(Income.ID)));
                income.setName(cursor.getString(cursor.getColumnIndex(Income.NAME)));
                income.setPurseId(cursor.getLong(cursor.getColumnIndex(Income.PURSE_ID)));
                income.setAmount(cursor.getDouble(cursor.getColumnIndex(Income.AMOUNT)));
                income.setCategoryId(cursor.getLong(cursor.getColumnIndex(Income.CATEGORY_ID)));
                income.setDate(cursor.getLong(cursor.getColumnIndex(Income.DATE)));
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return incomeList;
    }

    public List<Income> getAllToListByCategoryId(final long id) {
        return null;
    }

    public List<Income> getAllToListByPurseId(final long id) {
        return null;
    }

    public List<Income> getAllToListByDates(final long from, long to) {
        return null;
    }
}
