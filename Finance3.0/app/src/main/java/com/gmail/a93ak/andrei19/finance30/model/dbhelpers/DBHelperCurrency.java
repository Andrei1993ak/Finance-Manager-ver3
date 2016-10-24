package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperPojo;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCurrency implements DBHelperPojo<Currency> {

    private DBHelper dbHelper;

    private static DBHelperCurrency instance;

    public static DBHelperCurrency getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperCurrency(dbHelper);
        return instance;
    }

    private DBHelperCurrency(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(Currency currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CURRENCY_KEY_CODE, currency.getCode());
        values.put(DBHelper.CURRENCY_KEY_NAME, currency.getName());
        return db.insert(DBHelper.TABLE_CURRENCIES, null, values);
    }

    @Override
    public Currency get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Currency currency = new Currency();
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
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<Currency> getAllToList() {
        List<Currency> currenciesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Currency currency = new Currency();
                currency.setId(cursor.getLong(0));
                currency.setCode(cursor.getString(1));
                currency.setName(cursor.getString(2));
                currenciesList.add(currency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return currenciesList;
    }

    public List<String> getAllCodes() {
        List<String> currenciesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CURRENCIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                currenciesList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return currenciesList;
    }

    @Override
    public int update(Currency currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CURRENCY_KEY_CODE, currency.getCode());
        values.put(DBHelper.CURRENCY_KEY_NAME, currency.getName());
        return db.update(DBHelper.TABLE_CURRENCIES, values, DBHelper.CURRENCY_KEY_ID + "=?", new String[]{String.valueOf(currency.getId())});
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_CURRENCIES, DBHelper.CURRENCY_KEY_ID + " = " + id, null);
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_CURRENCIES, null, null);
    }

}
