package com.github.andrei1993ak.finances.model.dbHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DBHelperTransfer implements IDBHelperForModel<Transfer> {

    private final DBHelper dbHelper;
    private final DBHelperWallet dbHelperWallet;

    public DBHelperTransfer() {
        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
    }

    @Override
    public long add(final Transfer transfer) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = transfer.convertToContentValues();
        long id = -1;
        try {
            db.beginTransaction();
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

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return new Transfer().convertFromCursor(cursor);
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
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public List<Transfer> getAllToList() {
        final List<Transfer> transfers = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Transfer transfer = new Transfer().convertFromCursor(cursor);
                    transfers.add(transfer);
                } while (cursor.moveToNext());
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return transfers;
    }

    @Override
    public int update(final Transfer transfer) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Transfer oldTransfer = get(transfer.getId());
        final ContentValues values = transfer.convertToContentValues();
        int count;
        try {
            db.beginTransaction();
            count = db.update(TableQueryGenerator.getTableName(Transfer.class), values, Transfer.ID + "=?", new String[]{String.valueOf(transfer.getId())});
            final Transfer newTransfer = get(transfer.getId());
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Transfer transfer = get(id);
        int count;
        try {
            db.beginTransaction();
            dbHelperWallet.addAmount(transfer.getFromWalletId(), transfer.getFromAmount());
            dbHelperWallet.takeAmount(transfer.getToWalletId(), transfer.getToAmount());
            count = db.delete(TableQueryGenerator.getTableName(Transfer.class), Transfer.ID + " = " + id, null);
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
            count = db.delete(TableQueryGenerator.getTableName(Transfer.class), null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public List<Transfer> getAllToListByDates(final Long fromDate, final Long toDate) {
        return null;
    }

    public List<Transfer> getAllToListByWalletId(final Long id) {
        final List<Transfer> transfers = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) +
                " WHERE " + Transfer.FROM_WALLET_ID + " = " + id + " OR " + Transfer.TO_WALLET_ID + " = " + id;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Transfer transfer = new Transfer().convertFromCursor(cursor);
                    transfers.add(transfer);
                } while (cursor.moveToNext());
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return transfers;
    }

    public List<Transfer> getAllToListByCategoryId(final Long id) {
        return null;
    }
}
