package com.gmail.a93ak.andrei19.finance30.model.models;

import com.gmail.a93ak.andrei19.finance30.model.annotations.types.DBIntegerPrimaryKey;
import com.gmail.a93ak.andrei19.finance30.model.annotations.types.DBString;

public abstract class TableClass {

    @DBIntegerPrimaryKey
    public static final String ID = "_id";

    @DBString
    public static final String NAME = "name";
}
