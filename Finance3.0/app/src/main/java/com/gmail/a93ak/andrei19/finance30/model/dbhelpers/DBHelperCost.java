package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperInterface;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;

import java.util.ArrayList;
import java.util.List;


public class DBHelperCost implements DBHelperInterface<Cost> {

    private DBHelper dbHelper;

    private static DBHelperCost instance;

    public static DBHelperCost getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperCost(dbHelper);
        return instance;
    }

    private DBHelperCost(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(Cost cost) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COST_KEY_NAME, cost.getName());
        values.put(DBHelper.COST_KEY_PURSE_ID, cost.getPurse_id());
        values.put(DBHelper.COST_KEY_AMOUNT, cost.getAmount());
        values.put(DBHelper.COST_KEY_CATEGORY_ID, cost.getCategory_id());
        values.put(DBHelper.COST_KEY_DATE, cost.getDate());
        values.put(DBHelper.COST_KEY_PHOTO, cost.getPhoto());
        long id;
        try {
            db.beginTransaction();
            DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
            helperPurse.takeAmount(cost.getPurse_id(), cost.getAmount());
            id = db.insert(DBHelper.TABLE_COSTS, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Cost get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COSTS + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Cost cost = new Cost();
            cost.setId(cursor.getLong(0));
            cost.setName(cursor.getString(1));
            cost.setPurse_id(cursor.getLong(2));
            cost.setAmount(cursor.getDouble(3));
            cost.setCategory_id(cursor.getLong(4));
            cost.setDate(cursor.getLong(5));
            cost.setPhoto(cursor.getInt(6));
            cursor.close();
            return cost;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COSTS;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(Cost cost) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cost oldCost = get(cost.getId());
        ContentValues values = new ContentValues();
        values.put(DBHelper.COST_KEY_NAME, cost.getName());
        values.put(DBHelper.COST_KEY_PURSE_ID, cost.getPurse_id());
        values.put(DBHelper.COST_KEY_AMOUNT, cost.getAmount());
        values.put(DBHelper.COST_KEY_CATEGORY_ID, cost.getCategory_id());
        values.put(DBHelper.COST_KEY_DATE, cost.getDate());
        values.put(DBHelper.COST_KEY_PHOTO, cost.getPhoto());
        int count;
        try {
            db.beginTransaction();
            count = db.update(DBHelper.TABLE_COSTS, values, DBHelper.COST_KEY_ID + "=?", new String[]{String.valueOf(cost.getId())});
            Cost newCost = get(cost.getId());
            DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
            if (oldCost.getPurse_id() != newCost.getPurse_id()) {
                helperPurse.addAmount(oldCost.getPurse_id(), oldCost.getAmount());
                helperPurse.takeAmount(newCost.getPurse_id(), newCost.getAmount());
            } else if (newCost.getAmount() != oldCost.getAmount()) {
                helperPurse.takeAmount(newCost.getPurse_id(), (newCost.getAmount() - oldCost.getAmount()));
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
        Cost cost = get(id);
        int count;
        try {
            db.beginTransaction();
            helperPurse.addAmount(cost.getPurse_id(), cost.getAmount());
            count = db.delete(DBHelper.TABLE_COSTS, DBHelper.COST_KEY_ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TABLE_COSTS, null, null);
    }

    public List<Cost> getAllToList() {
        List<Cost> costList = new ArrayList<Cost>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_COSTS;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Cost cost = new Cost();
                cost.setId(cursor.getLong(0));
                cost.setName(cursor.getString(1));
                cost.setPurse_id(cursor.getLong(2));
                cost.setAmount(cursor.getDouble(3));
                cost.setCategory_id(cursor.getLong(4));
                cost.setDate(cursor.getLong(5));
                cost.setPhoto(cursor.getInt(6));
                costList.add(cost);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return costList;
    }

    public List<Cost> getAllToListByCategoryId(long id) {
        return null;
    }

    public List<Cost> getAllToListByPurseId(long id) {
        return null;
    }

    public List<Cost> getAllToListByDates(long from, long to) {
        return null;
    }
}
