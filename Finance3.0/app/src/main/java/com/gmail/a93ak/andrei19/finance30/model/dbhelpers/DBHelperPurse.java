package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperPojo;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

import java.util.ArrayList;
import java.util.List;

public class DBHelperPurse implements DBHelperPojo<Purse> {

    public static final String CURRENCY_NAME = "currency";

    private DBHelper dbHelper;

    private static DBHelperPurse instance;

    public static DBHelperPurse getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperPurse(dbHelper);
        return instance;
    }

    private DBHelperPurse(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(Purse purse) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.PURSES_KEY_NAME, purse.getName());
        values.put(DBHelper.PURSES_KEY_CURRENCY_ID, purse.getCurrency_id());
        values.put(DBHelper.PURSES_KEY_AMOUNT, purse.getAmount());
        return db.insert(DBHelper.TABLE_PURSES, null, values);
    }

    @Override
    public Purse get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_PURSES + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Purse purse = new Purse();
            purse.setId(cursor.getLong(0));
            purse.setName(cursor.getString(1));
            purse.setCurrency_id(cursor.getLong(2));
            purse.setAmount(cursor.getDouble(3));
            cursor.close();
            return purse;
        } else {
            cursor.close();
            return null;
        }
    }

    //TODO
    @Override
    public Cursor getAll() {
        String selectQuery = "SELECT " +
                "PU." + DBHelper.PURSES_KEY_ID + " as " + DBHelper.PURSES_KEY_ID + ", " +
                "PU." + DBHelper.PURSES_KEY_NAME + " as " + DBHelper.PURSES_KEY_NAME + ", " +
                "PU." + DBHelper.PURSES_KEY_AMOUNT + " as " + DBHelper.PURSES_KEY_AMOUNT + ", " +
                "CU." + DBHelper.CURRENCY_KEY_CODE + " as " + CURRENCY_NAME + " " +
                "from " + DBHelper.TABLE_PURSES + " as PU inner join " + DBHelper.TABLE_CURRENCIES + " as CU " +
                "on PU." + DBHelper.PURSES_KEY_CURRENCY_ID + " = CU." + DBHelper.CURRENCY_KEY_ID;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(Purse purse) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.PURSES_KEY_NAME, purse.getName());
        values.put(DBHelper.PURSES_KEY_CURRENCY_ID, purse.getCurrency_id());
        values.put(DBHelper.PURSES_KEY_AMOUNT, purse.getAmount());
        return db.update(DBHelper.TABLE_PURSES, values, DBHelper.PURSES_KEY_ID + "=?", new String[]{String.valueOf(purse.getId())});
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_PURSES, DBHelper.PURSES_KEY_ID + " = " + id, null);
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_PURSES, null, null);
    }

    public void addAmount(long id, double amount) {
        Purse purse = get(id);
        purse.setAmount(purse.getAmount() + amount);
        update(purse);
    }

    public void takeAmount(long id, double amount) {
        Purse purse = get(id);
        purse.setAmount(purse.getAmount() - amount);
        update(purse);
    }

    public List<Purse> getAllToList() {
        List<Purse> pursesList = new ArrayList<Purse>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_PURSES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Purse purse = new Purse();
                purse.setId(cursor.getLong(0));
                purse.setName(cursor.getString(1));
                purse.setCurrency_id(cursor.getLong(2));
                purse.setAmount(cursor.getDouble(3));
                pursesList.add(purse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pursesList;
    }
}
