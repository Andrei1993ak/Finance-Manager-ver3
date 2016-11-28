package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.util.ContextHolder;


import java.util.List;

public class WalletExecutor extends PojoExecutor<Wallet> {

    private DBHelperWallet dbHelperWallet = (DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class);

    public static final int KEY_RESULT_ADD = 301;
    public static final int KEY_RESULT_EDIT = 302;
    public static final int KEY_RESULT_DELETE = 303;
    public static final int KEY_RESULT_GET = 304;
    public static final int KEY_RESULT_GET_ALL = 305;
    public static final int KEY_RESULT_DELETE_ALL = 306;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 307;

    public WalletExecutor(final IOnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Wallet> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperWallet.get(id));
    }

    @Override
    public Result<Long> addPojo(final Wallet wallet) {
        return new Result<>(KEY_RESULT_ADD, dbHelperWallet.add(wallet));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelperWallet.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Wallet wallet) {
        return new Result<>(KEY_RESULT_EDIT, dbHelperWallet.update(wallet));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelperWallet.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, dbHelperWallet.deleteAll());
    }

    @Override
    public Result<List<Wallet>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelperWallet.getAllToList());
    }
}
