package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperTransfer;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class TransferCursorLoader extends CursorLoader {

    private DBHelperTransfer dbHelperTransfer;

    public TransferCursorLoader(Context context) {
        super(context);
        this.dbHelperTransfer = ((DBHelperTransfer)((App) ContextHolder.getInstance().getContext()).getDbHelper(Transfer.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperTransfer.getAll();
    }
}
