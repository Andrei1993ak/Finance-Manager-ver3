package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;


import java.util.List;
//
public class WalletExecutor extends PojoExecutor<Wallet> {

    public static final int KEY_RESULT_ADD = 301;
    public static final int KEY_RESULT_EDIT = 302;
    public static final int KEY_RESULT_DELETE = 303;
    public static final int KEY_RESULT_GET = 304;
    public static final int KEY_RESULT_GET_ALL = 305;
    public static final int KEY_RESULT_DELETE_ALL = 306;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 307;

    public WalletExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Wallet> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperWallet.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(final Wallet wallet) {
        return new Result<>(KEY_RESULT_ADD, DBHelperWallet.getInstance().add(wallet));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperWallet.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Wallet wallet) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperWallet.getInstance().update(wallet));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperWallet.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, DBHelperWallet.getInstance().deleteAll());
    }

    @Override
    public Result<List<Wallet>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, DBHelperWallet.getInstance().getAllToList());
    }
}
