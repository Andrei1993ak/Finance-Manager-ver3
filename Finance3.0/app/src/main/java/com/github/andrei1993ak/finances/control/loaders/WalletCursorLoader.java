package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class WalletCursorLoader extends CursorLoader{
//
    private final DBHelperWallet dbHelperWallet;

    public WalletCursorLoader(final Context context) {
        super(context);
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperWallet.getAll();
    }
}
