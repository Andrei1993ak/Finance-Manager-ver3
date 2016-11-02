package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.OperationToFtp;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperPojo;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;

import java.util.ArrayList;
import java.util.List;

public class DBHelperQueueToFTP implements DBHelperPojo<OperationToFtp> {

    private DBHelper dbHelper;

    private static DBHelperQueueToFTP instance;

    public static DBHelperQueueToFTP getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperQueueToFTP(dbHelper);
        return instance;
    }

    private DBHelperQueueToFTP(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    @Override
    public long add(OperationToFtp operationToFtp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.QUEUE_KEY_COST_ID, operationToFtp.getCost_id());
        values.put(DBHelper.QUEUE_KEY_OPERATION, operationToFtp.getOperation());
        long id;
        try {
            db.beginTransaction();
            id = db.insert(DBHelper.TABLE_QUEUE, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public OperationToFtp get(long id) {
        return null;
    }

    @Override
    public Cursor getAll() {
        return null;
    }

    @Override
    public int update(OperationToFtp operationToFtp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.QUEUE_KEY_COST_ID, operationToFtp.getCost_id());
        values.put(DBHelper.QUEUE_KEY_OPERATION, operationToFtp.getOperation());
        int count;
        try {
            db.beginTransaction();
            count = db.update(DBHelper.TABLE_QUEUE, values, DBHelper.QUEUE_KEY_ID + "=?", new String[]{String.valueOf(operationToFtp.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(DBHelper.TABLE_QUEUE, DBHelper.QUEUE_KEY_COST_ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    public List<OperationToFtp> getAllToList() {
        List<OperationToFtp> operations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_QUEUE;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OperationToFtp operation = new OperationToFtp();
                operation.setId(cursor.getLong(0));
                operation.setCost_id(cursor.getLong(1));
                operation.setOperation(cursor.getInt(2));
                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operations;
    }
}
