package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperTransfer;
import com.github.andrei1993ak.finances.model.models.Transfer;


import java.util.ArrayList;
import java.util.List;

public class TransferExecutor extends PojoExecutor<Transfer> {

    //TODO create enum
    //TODO or @IntDef annotations
    public static final int KEY_RESULT_ADD = 801;
    public static final int KEY_RESULT_EDIT = 802;
    public static final int KEY_RESULT_DELETE = 803;
    public static final int KEY_RESULT_GET = 804;
    public static final int KEY_RESULT_GET_ALL = 805;
    public static final int KEY_RESULT_DELETE_ALL = 806;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 807;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 808;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID = 809;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 810;

    public TransferExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Transfer> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).get(id));
    }

    @Override
    public Result<Long> addPojo(final Transfer transfer) {
        return new Result<>(KEY_RESULT_ADD, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).add(transfer));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Transfer transfer) {
        return new Result<>(KEY_RESULT_EDIT, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).update(transfer));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).deleteAll());
    }

    @Override
    public Result<List<Transfer>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, ((DBHelperTransfer)App.getDbHelper(Transfer.class)).getAllToList());
    }

    @Override
    protected Result<List<Transfer>> getAllToListByDates(final ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES,((DBHelperTransfer)App.getDbHelper(Transfer.class)).getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Transfer>> getAllToListByWalletId(final Long walletId) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID,((DBHelperTransfer)App.getDbHelper(Transfer.class)).getAllToListByWalletId(walletId));
    }

    @Override
    protected Result<List<Transfer>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID,((DBHelperTransfer)App.getDbHelper(Transfer.class)).getAllToListByCategoryId(id));
    }
}
