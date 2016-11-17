package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

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

    public TransferExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Transfer> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperTransfer.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(Transfer transfer) {
        return new Result<>(KEY_RESULT_ADD, DBHelperTransfer.getInstance().add(transfer));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperTransfer.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Transfer transfer) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperTransfer.getInstance().update(transfer));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperTransfer.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, DBHelperTransfer.getInstance().deleteAll());
    }

    @Override
    public Result<List<Transfer>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperTransfer.getInstance().getAllToList());
    }

    @Override
    protected Result<List<Transfer>> getAllToListByDates(ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES,DBHelperTransfer.getInstance().getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Transfer>> getAllToListByWalletId(Long walletId) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID,DBHelperTransfer.getInstance().getAllToListByWalletId(walletId));
    }

    @Override
    protected Result<List<Transfer>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID,DBHelperTransfer.getInstance().getAllToListByCategoryId(id));
    }
}
