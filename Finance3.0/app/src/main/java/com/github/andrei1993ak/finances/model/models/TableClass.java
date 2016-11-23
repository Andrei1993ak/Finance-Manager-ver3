package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerPrimaryKey;
import com.github.andrei1993ak.finances.model.annotations.types.DBString;

public abstract class TableClass {

    @DBIntegerPrimaryKey
    public static final String ID = "_id";

    @DBString
    public static final String NAME = "name";

    public abstract TableClass convertFromCursor(Cursor cursor);

    public abstract ContentValues convertToContentValues();
}
