package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;

import java.util.ArrayList;
import java.util.List;

public class DBHelperTransfer implements DBHelperForModel<Transfer> {

    private final DBHelper dbHelper;

    //TODO bbeeee static
    //TODO move to App
    private static DBHelperTransfer instance;

    public static DBHelperTransfer getInstance() {
        //TODO {}
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
        values.put(Transfer.FROM_WALLET_ID, transfer.getFromWalletId());
        values.put(Transfer.TO_WALLET_ID, transfer.getToWalletId());
        values.put(Transfer.FROM_AMOUNT, transfer.getFromAmount());
        values.put(Transfer.TO_AMOUNT, transfer.getToAmount());
        long id = -1;
        try {
            db.beginTransaction();
            final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
            dbHelperWallet.takeAmount(transfer.getFromWalletId(), transfer.getFromAmount());
            dbHelperWallet.addAmount(transfer.getToWalletId(), transfer.getToAmount());
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
            final Transfer transfer = new Transfer();
            transfer.setId(cursor.getLong(cursor.getColumnIndex(Transfer.ID)));
            transfer.setName(cursor.getString(cursor.getColumnIndex(Transfer.NAME)));
            transfer.setDate(cursor.getLong(cursor.getColumnIndex(Transfer.DATE)));
            transfer.setFromWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.FROM_WALLET_ID)));
            transfer.setToWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.TO_WALLET_ID)));
            transfer.setFromAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT)));
            transfer.setToAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT)));
            //TODO final block
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
                transfer.setId(cursor.getLong(cursor.getColumnIndex(Transfer.ID)));
                transfer.setName(cursor.getString(cursor.getColumnIndex(Transfer.NAME)));
                transfer.setDate(cursor.getLong(cursor.getColumnIndex(Transfer.DATE)));
                transfer.setFromWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.FROM_WALLET_ID)));
                transfer.setToWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.TO_WALLET_ID)));
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
    public int update(final Transfer transfer) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Transfer oldTransfer = get(transfer.getId());
        final ContentValues values = new ContentValues();
        values.put(Transfer.NAME, transfer.getName());
        values.put(Transfer.DATE, transfer.getDate());
        values.put(Transfer.FROM_WALLET_ID, transfer.getFromWalletId());
        values.put(Transfer.TO_WALLET_ID, transfer.getToWalletId());
        values.put(Transfer.FROM_AMOUNT, transfer.getFromAmount());
        values.put(Transfer.TO_AMOUNT, transfer.getToAmount());
        final int id = db.update(TableQueryGenerator.getTableName(Transfer.class), values, Transfer.ID + "=?", new String[]{String.valueOf(transfer.getId())});
        final Transfer newTransfer = get(transfer.getId());
        final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
        if (oldTransfer.getFromWalletId() != newTransfer.getFromWalletId()) {
            dbHelperWallet.addAmount(oldTransfer.getFromWalletId(), oldTransfer.getFromAmount());
            dbHelperWallet.takeAmount(newTransfer.getFromWalletId(), newTransfer.getFromAmount());
        } else if (oldTransfer.getFromAmount() != newTransfer.getFromAmount()) {
            dbHelperWallet.takeAmount(newTransfer.getFromWalletId(), (newTransfer.getFromAmount() - oldTransfer.getFromAmount()));
        }
        if (oldTransfer.getToWalletId() != newTransfer.getToWalletId()) {
            dbHelperWallet.takeAmount(oldTransfer.getToWalletId(), oldTransfer.getToAmount());
            dbHelperWallet.addAmount(newTransfer.getToWalletId(), newTransfer.getToAmount());
        } else if (oldTransfer.getToAmount() != newTransfer.getToAmount()) {
            dbHelperWallet.addAmount(newTransfer.getToWalletId(), (newTransfer.getToAmount() - oldTransfer.getToAmount()));
        }
        return id;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final DBHelperWallet dbHelperWallet = DBHelperWallet.getInstance();
        final Transfer transfer = get(id);
        dbHelperWallet.addAmount(transfer.getFromWalletId(), transfer.getFromAmount());
        dbHelperWallet.takeAmount(transfer.getToWalletId(), transfer.getToAmount());
        return db.delete(TableQueryGenerator.getTableName(Transfer.class), Transfer.ID + " = " + id, null);
    }

    @Override
    public int deleteAll() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TableQueryGenerator.getTableName(Transfer.class), null, null);
    }

    public List<Transfer> getAllToListByDates(final Long fromDate, final Long toDate) {
        return null;
    }

    public List<Transfer> getAllToListByWalletId(final Long id) {
        final List<Transfer> transfers = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) +
                " WHERE " + Transfer.FROM_WALLET_ID + " = " + id + " OR " + Transfer.TO_WALLET_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                final Transfer transfer = new Transfer();
                transfer.setId(cursor.getLong(cursor.getColumnIndex(Transfer.ID)));
                transfer.setName(cursor.getString(cursor.getColumnIndex(Transfer.NAME)));
                transfer.setDate(cursor.getLong(cursor.getColumnIndex(Transfer.DATE)));
                transfer.setFromWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.FROM_WALLET_ID)));
                transfer.setToWalletId(cursor.getLong(cursor.getColumnIndex(Transfer.TO_WALLET_ID)));
                transfer.setFromAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT)));
                transfer.setToAmount(cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT)));
                transfers.add(transfer);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return transfers;
    }

    public List<Transfer> getAllToListByCategoryId(final Long id) {
        return null;
    }
}
