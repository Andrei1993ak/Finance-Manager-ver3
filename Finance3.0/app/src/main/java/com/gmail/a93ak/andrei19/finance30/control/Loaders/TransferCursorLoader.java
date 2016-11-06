package com.gmail.a93ak.andrei19.finance30.control.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperTransfer;

public class TransferCursorLoader extends CursorLoader {

    private DBHelperTransfer dbHelperTransfer;

    public TransferCursorLoader(Context context) {
        super(context);
        this.dbHelperTransfer = DBHelperTransfer.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperTransfer.getAll();
    }
}
