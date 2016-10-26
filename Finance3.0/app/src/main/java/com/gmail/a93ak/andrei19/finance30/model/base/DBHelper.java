package com.gmail.a93ak.andrei19.finance30.model.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
//
    private static final String DATABASE_NAME = "financePm";

    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_CURRENCIES = "currencies";
    public static final String CURRENCY_KEY_ID = "_id";
    public static final String CURRENCY_KEY_CODE = "code";
    public static final String CURRENCY_KEY_NAME = "name";

    public static final String TABLE_CURRENCIES_FROM_WEB = "currencies_from_web";
    public static final String CURRENCY_FROM_WEB_KEY_ID = "_id";
    public static final String CURRENCY_FROM_WEB_KEY_CODE = "code";
    public static final String CURRENCY_FROM_WEB_KEY_NAME = "name";

    public static final String TABLE_PURSES = "purses";
    public static final String PURSES_KEY_ID = "_id";
    public static final String PURSES_KEY_NAME = "name";
    public static final String PURSES_KEY_CURRENCY_ID = "currency_id";
    public static final String PURSES_KEY_AMOUNT = "amount";

    public static final String TABLE_INCOME_CATEGORIES = "incomeCategories";
    public static final String INCOME_CATEGORY_KEY_ID = "_id";
    public static final String INCOME_CATEGORY_KEY_NAME = "name";
    public static final String INCOME_CATEGORY_KEY_PARENT_ID = "parent_id";

    public static final String TABLE_COST_CATEGORIES = "costCategories";
    public static final String COST_CATEGORY_KEY_ID = "_id";
    public static final String COST_CATEGORY_KEY_NAME = "name";
    public static final String COST_CATEGORY_KEY_PARENT_ID = "parent_id";

    public static final String TABLE_INCOMES = "incomes";
    public static final String INCOME_KEY_ID = "_id";
    public static final String INCOME_KEY_NAME = "name";
    public static final String INCOME_KEY_PURSE_ID = "purse_id";
    public static final String INCOME_KEY_AMOUNT = "amount";
    public static final String INCOME_KEY_CATEGORY_ID = "category_id";
    public static final String INCOME_KEY_DATE = "date";

//    public static final String TABLE_TRANSFERS = "transfers";
//    public static final String TRANSFER_KEY_ID = "_id";
//    public static final String TRANSFER_KEY_NAME = "name";
//    public static final String TRANSFER_KEY_DATE = "date";
//    public static final String TRANSFER_KEY_FROM_PURSE = "fromPurse";
//    public static final String TRANSFER_KEY_TO_PURSE = "toPurse";
//    public static final String TRANSFER_KEY_FROM_AMOUNT = "fromAmount";
//    public static final String TRANSFER_KEY_TO_AMOUNT = "toAmount";
//


    private static DBHelper instance;


    public static DBHelper getInstance(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    private DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CURRENCIES_TABLE = "CREATE TABLE " + TABLE_CURRENCIES + " ("
                + CURRENCY_KEY_ID + " INTEGER PRIMARY KEY, " + CURRENCY_KEY_CODE + " TEXT, "
                + CURRENCY_KEY_NAME + " TEXT" + ")";

        String CREATE_CURRENCIES_FROM_WEB_TABLE = "CREATE TABLE " + TABLE_CURRENCIES_FROM_WEB + " ("
                + CURRENCY_FROM_WEB_KEY_ID + " INTEGER PRIMARY KEY, " + CURRENCY_FROM_WEB_KEY_CODE + " TEXT, "
                + CURRENCY_FROM_WEB_KEY_NAME + " TEXT" + ")";

        String CREATE_PURSES_TABLE = "CREATE TABLE " + TABLE_PURSES + " ("
                + PURSES_KEY_ID + " INTEGER PRIMARY KEY, " + PURSES_KEY_NAME + " TEXT, "
                + PURSES_KEY_CURRENCY_ID + " INTEGER, " + PURSES_KEY_AMOUNT + " REAL" + ")";

        String CREATE_INCOME_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_INCOME_CATEGORIES + " ("
                + INCOME_CATEGORY_KEY_ID + " INTEGER PRIMARY KEY, " + INCOME_CATEGORY_KEY_NAME + " TEXT,"
                + INCOME_CATEGORY_KEY_PARENT_ID + " INTEGER" + ")";

        String CREATE_COST_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_COST_CATEGORIES + " ("
                + COST_CATEGORY_KEY_ID + " INTEGER PRIMARY KEY, " + COST_CATEGORY_KEY_NAME + " TEXT,"
                + COST_CATEGORY_KEY_PARENT_ID + " INTEGER" + ")";

        String CREATE_INCOMES_TABLE = "CREATE TABLE " + TABLE_INCOMES + " ("
                + INCOME_KEY_ID + " INTEGER PRIMARY KEY, " + INCOME_KEY_NAME + " TEXT, "
                + INCOME_KEY_PURSE_ID + " INTEGER, " + INCOME_KEY_AMOUNT + " REAL, "
                + INCOME_KEY_CATEGORY_ID + " INTEGER, " + INCOME_KEY_DATE + " INTEGER" + ")";

//
//        String CREATE_TRANSFER_TABLE = "CREATE TABLE " + TABLE_TRANSFERS + " ("
//                + TRANSFER_KEY_ID + " INTEGER PRIMARY KEY, " + TRANSFER_KEY_NAME + " TEXT, "
//                + TRANSFER_KEY_DATE + " INTEGER, " + TRANSFER_KEY_FROM_PURSE + " INTEGER, "
//                + TRANSFER_KEY_TO_PURSE + " INTEGER, " + TRANSFER_KEY_FROM_AMOUNT + " INTEGER, "
//                + TRANSFER_KEY_TO_AMOUNT + " INTEGER" + ")";
//


//        sqLiteDatabase.execSQL(CREATE_CURRENCIES_TABLE);
//        sqLiteDatabase.execSQL(CREATE_CURRENCIES_FROM_WEB_TABLE);
//        sqLiteDatabase.execSQL(CREATE_PURSES_TABLE);
//        sqLiteDatabase.execSQL(CREATE_INCOME_CATEGORIES_TABLE);
//        sqLiteDatabase.execSQL(CREATE_COST_CATEGORIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_INCOMES_TABLE);
//            sqLiteDatabase.execSQL(CREATE_TRANSFER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PURSES);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME_PARENTS_CATEGORY);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME_CATEGORIES);

        onCreate(sqLiteDatabase);

    }

}
