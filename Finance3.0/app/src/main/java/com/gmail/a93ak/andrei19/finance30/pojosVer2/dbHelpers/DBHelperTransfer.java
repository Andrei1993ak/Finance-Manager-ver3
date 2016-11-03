package com.gmail.a93ak.andrei19.finance30.pojosVer2.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.pojosVer2.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.pojosVer2.pojos.Transfer;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperTransfer implements DBHelperForModel<Transfer> {

    private DBHelper dbHelper;

    private static DBHelperTransfer instance;

    public static DBHelperTransfer getInstance() {
        if (instance == null)
            instance = new DBHelperTransfer();
        return instance;
    }

    private DBHelperTransfer() {
        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
    }

    @Override
    public long add(final Transfer transfer) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Transfer.NAME, transfer.getName());
        values.put(Transfer.DATE, transfer.getDate());
        values.put(Transfer.FROM_PURSE_ID, transfer.getFromPurseId());
        values.put(Transfer.TO_PURSE_ID, transfer.getToPurseId());
        values.put(Transfer.FROM_AMOUNT, transfer.getFromAmount());
        values.put(Transfer.TO_AMOUNT, transfer.getToAmount());
        long id = -1;
        try {
            db.beginTransaction();
            final DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
            helperPurse.takeAmount(transfer.getFromPurseId(), transfer.getFromAmount());
            helperPurse.addAmount(transfer.getToPurseId(), transfer.getToAmount());
            id = db.insert(TableQueryGenerator.getTableName(Transfer.class), null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public Transfer get(final long id) {

        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) + " WHERE _id = " + id;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Transfer transfer = new Transfer();
            transfer.setId((Integer.parseInt(cursor.getString(cursor.getColumnIndex(Transfer.ID)))));
            transfer.setName(cursor.getString(cursor.getColumnIndex(Transfer.NAME)));
            transfer.setDate(cursor.getLong(cursor.getColumnIndex(Transfer.DATE)));
            transfer.setFromPurseId(cursor.getInt(cursor.getColumnIndex(Transfer.FROM_PURSE_ID)));
            transfer.setToPurseId(cursor.getInt(cursor.getColumnIndex(Transfer.TO_PURSE_ID)));
            transfer.setFromAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT)));
            transfer.setToAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT)));
            cursor.close();
            return transfer;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public List<Transfer> getAllToList() {
        final List<Transfer> transfers = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Transfer transfer = new Transfer();
                transfer.setId((Integer.parseInt(cursor.getString(cursor.getColumnIndex(Transfer.ID)))));
                transfer.setName(cursor.getString(cursor.getColumnIndex(Transfer.NAME)));
                transfer.setDate(cursor.getLong(cursor.getColumnIndex(Transfer.DATE)));
                transfer.setFromPurseId(cursor.getInt(cursor.getColumnIndex(Transfer.FROM_PURSE_ID)));
                transfer.setToPurseId(cursor.getInt(cursor.getColumnIndex(Transfer.TO_PURSE_ID)));
                transfer.setFromAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT)));
                transfer.setToAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT)));
                cursor.close();
                transfers.add(transfer);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        return transfers;
    }

    @Override
    public int update(Transfer transfer) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Transfer oldTransfer = get(transfer.getId());
        final ContentValues values = new ContentValues();
        values.put(Transfer.NAME, transfer.getName());
        values.put(Transfer.DATE, transfer.getDate());
        values.put(Transfer.FROM_PURSE_ID, transfer.getFromPurseId());
        values.put(Transfer.TO_PURSE_ID, transfer.getToPurseId());
        values.put(Transfer.FROM_AMOUNT, transfer.getFromAmount());
        values.put(Transfer.TO_AMOUNT, transfer.getToAmount());
        final int id = db.update(TableQueryGenerator.getTableName(Transfer.class), values, Transfer.ID + "=?", new String[]{String.valueOf(transfer.getId())});
        final Transfer newTransfer = get(transfer.getId());
        final DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
        if (oldTransfer.getFromPurseId() != newTransfer.getFromPurseId()) {
            helperPurse.addAmount(oldTransfer.getFromPurseId(), oldTransfer.getFromAmount());
            helperPurse.takeAmount(newTransfer.getFromPurseId(), newTransfer.getFromAmount());
        } else if (oldTransfer.getFromAmount() != newTransfer.getFromAmount()) {
            helperPurse.takeAmount(newTransfer.getFromPurseId(), (newTransfer.getFromAmount() - oldTransfer.getFromAmount()));
        }
        if (oldTransfer.getToPurseId() != newTransfer.getToPurseId()) {
            helperPurse.takeAmount(oldTransfer.getToPurseId(), oldTransfer.getToAmount());
            helperPurse.addAmount(newTransfer.getToPurseId(), newTransfer.getToAmount());
        } else if (oldTransfer.getToAmount() != newTransfer.getToAmount()) {
            helperPurse.addAmount(newTransfer.getToPurseId(), (newTransfer.getToAmount() - oldTransfer.getToAmount()));
        }
        return id;
    }

    @Override
    public int delete(long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final DBHelperPurse helperPurse = DBHelperPurse.getInstance(dbHelper);
        final Transfer transfer = get(id);
        helperPurse.addAmount(transfer.getFromPurseId(), transfer.getFromAmount());
        helperPurse.takeAmount(transfer.getToPurseId(), transfer.getToAmount());
        return db.delete(TableQueryGenerator.getTableName(Transfer.class), Transfer.ID + " = " + id, null);
    }

    @Override
    public int deleteAll() {
        return 0;
    }

}
