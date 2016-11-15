package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperPurse;
import com.github.andrei1993ak.finances.model.models.Purse;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;


import java.util.List;
//
public class PurseExecutor extends PojoExecutor<Purse> {

    public static final int KEY_RESULT_ADD = 301;
    public static final int KEY_RESULT_EDIT = 302;
    public static final int KEY_RESULT_DELETE = 303;
    public static final int KEY_RESULT_GET = 304;
    public static final int KEY_RESULT_GET_ALL = 305;
    public static final int KEY_RESULT_DELETE_ALL = 306;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 307;

    public PurseExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Purse> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperPurse.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(Purse purse) {
        return new Result<>(KEY_RESULT_ADD, DBHelperPurse.getInstance().add(purse));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperPurse.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Purse purse) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperPurse.getInstance().update(purse));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperPurse.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, DBHelperPurse.getInstance().deleteAll());
    }

    @Override
    public Result<List<Purse>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, DBHelperPurse.getInstance().getAllToList());
    }
}
