package com.github.andrei1993ak.finances.model.newModel.newDBHelpers;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.model.newModel.UniversalDBHelper;
import com.github.andrei1993ak.finances.util.Constants;

public class newWalletDBHelper extends UniversalDBHelper<Wallet> {

    public newWalletDBHelper() {
        super(Wallet.class);
    }

    @Override
    public Cursor getAll() {
        final String selectQuery = "SELECT " +
                "PU." + Wallet.ID + " as " + Wallet.ID + ", " +
                "PU." + Wallet.NAME + " as " + Wallet.NAME + ", " +
                "PU." + Wallet.AMOUNT + " as " + Wallet.AMOUNT + ", " +
                "CU." + Currency.CODE + " as " + Constants.CURRENCY + " " +
                "from " + TableQueryGenerator.getTableName(Wallet.class)
                + " as PU inner join " + TableQueryGenerator.getTableName(Currency.class) + " as CU "
                + "on PU." + Wallet.CURRENCY_ID + " = CU." + Currency.ID;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int delete(final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String[] querys = {
                "SELECT * FROM " + TableQueryGenerator.getTableName(Cost.class) + " WHERE " + Cost.WALLET_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Income.class) + " WHERE " + Income.WALLET_ID + " = " + id + " LIMIT 1",
                "SELECT * FROM " + TableQueryGenerator.getTableName(Transfer.class) + " WHERE " + Transfer.FROM_WALLET_ID + " = " + id +
                        " or " + Transfer.TO_WALLET_ID + " = " + id + " LIMIT 1"};
        for (final String query : querys) {
            final Cursor cursor = db.rawQuery(query, null);
            try {
                if (cursor.moveToFirst()) {
                    return -1;
                }
            } finally {
                cursor.close();
            }
        }
        return super.delete(id);
    }

}
