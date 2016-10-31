package com.gmail.a93ak.andrei19.finance30.control.Executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCost;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;

import java.util.ArrayList;
import java.util.List;

public class CostExecutor extends PojoExecutor<Cost> {

    public static final int KEY_RESULT_ADD = 701;
    public static final int KEY_RESULT_EDIT = 702;
    public static final int KEY_RESULT_DELETE = 703;
    public static final int KEY_RESULT_GET = 704;
    public static final int KEY_RESULT_GET_ALL = 705;
    public static final int KEY_RESULT_DELETE_ALL = 706;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 707;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 708;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID = 709;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 710;

    public CostExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Cost> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperCost.getInstance(DBHelper.getInstance(context)).get(id));
    }

    @Override
    public Result<Long> addPojo(Cost cost) {
        return new Result<>(KEY_RESULT_ADD, DBHelperCost.getInstance(DBHelper.getInstance(context)).add(cost));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperCost.getInstance(DBHelper.getInstance(context)).delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Cost cost) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperCost.getInstance(DBHelper.getInstance(context)).update(cost));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).deleteAll());
    }

    @Override
    public Result<List<Cost>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToList());
    }

    @Override
    protected Result<List<Cost>> getAllToListByDates(ArrayList<Long> diapason) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES,DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByDates(diapason.get(0),diapason.get(1)));
    }

    @Override
    protected Result<List<Cost>> getAllToListByPurseId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID,DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByPurseId(id));
    }

    @Override
    protected Result<List<Cost>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID,DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByCategoryId(id));
    }
}
