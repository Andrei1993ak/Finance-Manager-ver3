package com.github.andrei1993ak.finances.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.andrei1993ak.finances.model.models.Cost;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financePm";

    private static final int DATABASE_VERSION = 23;

    private static DBHelper instance;


    public static DBHelper getInstance(final Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    private DBHelper(final Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(CurrencyOfficial.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(Currency.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(CostCategory.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(IncomeCategory.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(Wallet.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(Income.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableCreateQuery(Transfer.class));
//        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(Cost.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(Income.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(Transfer.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(IncomeCategory.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(CostCategory.class));
//        sqLiteDatabase.execSQL(TableQueryGenerator.getTableDeleteQuery(Wallet.class));
        onCreate(sqLiteDatabase);
    }

    public int getNextCostId() {
        final String query = "SELECT SEQ FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TableQueryGenerator.getTableName(Cost.class) + "'";
        final Cursor cursor = getReadableDatabase().rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) + 1;
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

}
