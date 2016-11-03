package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperInterface;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Income;

import java.util.ArrayList;
import java.util.List;


public class DBHelperIncome implements DBHelperInterface<Income> {

    private DBHelper dbHelper;

    private static DBHelperIncome instance;

    public static DBHelperIncome getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperIncome(dbHelper);
        return instance;
    }

    private DBHelperIncome(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(Income income) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.INCOME_KEY_NAME, income.getName());
        values.put(DBHelper.INCOME_KEY_PURSE_ID, income.getPurse_id());
        values.put(DBHelper.INCOME_KEY_AMOUNT, income.getAmount());
        values.put(DBHelper.INCOME_KEY_CATEGORY_ID, income.getCategory_id());
        values.put(DBHelper.INCOME_KEY_DATE, income.getDate());
        long id;
        try {
            db.beginTransaction();
            DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
            helperPurse.addAmount(income.getPurse_id(), income.getAmount());
            id = db.insert(DBHelper.TABLE_INCOMES, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Income get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOMES + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Income income = new Income();
            income.setId(cursor.getLong(0));
            income.setName(cursor.getString(1));
            income.setPurse_id(cursor.getLong(2));
            income.setAmount(cursor.getDouble(3));
            income.setCategory_id(cursor.getLong(4));
            income.setDate(cursor.getLong(5));
            cursor.close();
            return income;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOMES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(Income income) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Income oldIncome = get(income.getId());
        ContentValues values = new ContentValues();
        values.put(DBHelper.INCOME_KEY_NAME, income.getName());
        values.put(DBHelper.INCOME_KEY_PURSE_ID, income.getPurse_id());
        values.put(DBHelper.INCOME_KEY_AMOUNT, income.getAmount());
        values.put(DBHelper.INCOME_KEY_CATEGORY_ID, income.getCategory_id());
        values.put(DBHelper.INCOME_KEY_DATE, income.getDate());
        int count;
        try {
            db.beginTransaction();
            count = db.update(DBHelper.TABLE_INCOMES, values, DBHelper.INCOME_KEY_ID + "=?", new String[]{String.valueOf(income.getId())});
            Income newIncome = get(income.getId());
            DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
            if (oldIncome.getPurse_id() != newIncome.getPurse_id()) {
                helperPurse.takeAmount(oldIncome.getPurse_id(), oldIncome.getAmount());
                helperPurse.addAmount(newIncome.getPurse_id(), newIncome.getAmount());
            } else if (newIncome.getAmount() != oldIncome.getAmount()) {
                helperPurse.addAmount(newIncome.getPurse_id(), (newIncome.getAmount() - oldIncome.getAmount()));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
        Income income = get(id);
        int count;
        try {
            db.beginTransaction();
            helperPurse.takeAmount(income.getPurse_id(), income.getAmount());
            count = db.delete(DBHelper.TABLE_INCOMES, DBHelper.INCOME_KEY_ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_INCOMES, null, null);
    }

    public List<Income> getAllToList() {
        List<Income> incomeList = new ArrayList<Income>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOMES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                income.setId(cursor.getLong(0));
                income.setName(cursor.getString(1));
                income.setPurse_id(cursor.getLong(2));
                income.setAmount(cursor.getDouble(3));
                income.setCategory_id(cursor.getLong(4));
                income.setDate(cursor.getLong(5));
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return incomeList;
    }

    public List<Income> getAllToListByCategoryId(long id) {
        return null;
    }

    public List<Income> getAllToListByPurseId(long id) {
        return null;
    }

    public List<Income> getAllToListByDates(long from, long to) {
        return null;
    }
}
