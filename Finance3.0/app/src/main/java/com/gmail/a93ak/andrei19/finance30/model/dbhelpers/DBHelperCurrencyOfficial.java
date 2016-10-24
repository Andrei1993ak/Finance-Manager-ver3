package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperPojo;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CurrencyOfficial;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCurrencyOfficial implements DBHelperPojo<CurrencyOfficial> {

    private DBHelper dbHelper;

    private static DBHelperCurrencyOfficial instance;

    public static DBHelperCurrencyOfficial getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperCurrencyOfficial(dbHelper);
        return instance;
    }

    private DBHelperCurrencyOfficial(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(CurrencyOfficial currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CURRENCY_FROM_WEB_KEY_CODE, currency.getCode());
        values.put(DBHelper.CURRENCY_FROM_WEB_KEY_NAME, currency.getName());
        return db.insert(DBHelper.TABLE_CURRENCIES_FROM_WEB, null, values);
    }

    @Override
    public CurrencyOfficial get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES_FROM_WEB + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            CurrencyOfficial currency = new CurrencyOfficial();
            currency.setId(cursor.getLong(0));
            currency.setCode(cursor.getString(1));
            currency.setName(cursor.getString(2));
            cursor.close();
            return currency;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES_FROM_WEB + " ORDER BY " + DBHelper.CURRENCY_FROM_WEB_KEY_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<CurrencyOfficial> getAllToList() {
        List<CurrencyOfficial> currenciesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES_FROM_WEB + " ORDER BY " + DBHelper.CURRENCY_FROM_WEB_KEY_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CurrencyOfficial currency = new CurrencyOfficial();
                currency.setId(cursor.getLong(0));
                currency.setCode(cursor.getString(1));
                currency.setName(cursor.getString(2));
                currenciesList.add(currency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return currenciesList;
    }

    @Override
    public int update(CurrencyOfficial currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CURRENCY_FROM_WEB_KEY_CODE, currency.getCode());
        values.put(DBHelper.CURRENCY_FROM_WEB_KEY_NAME, currency.getName());
        return db.update(DBHelper.TABLE_CURRENCIES_FROM_WEB, values, DBHelper.CURRENCY_FROM_WEB_KEY_ID + "=?", new String[]{String.valueOf(currency.getId())});
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_CURRENCIES_FROM_WEB, DBHelper.CURRENCY_FROM_WEB_KEY_ID + " = " + id, null);
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_CURRENCIES_FROM_WEB, null, null);
    }

}
