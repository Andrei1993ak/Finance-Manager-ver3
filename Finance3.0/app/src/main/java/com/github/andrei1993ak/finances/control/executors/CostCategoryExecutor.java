package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.util.ContextHolder;


import java.util.List;

public class CostCategoryExecutor extends PojoExecutor<CostCategory> {

    public static final int KEY_RESULT_ADD = 501;
    public static final int KEY_RESULT_EDIT = 502;
    public static final int KEY_RESULT_DELETE = 503;
    public static final int KEY_RESULT_GET = 504;
    public static final int KEY_RESULT_GET_ALL = 505;
    public static final int KEY_RESULT_DELETE_ALL = 506;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 507;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID = 508;

    private final DBHelperCategoryCost dbHelperCategoryCost = ((DBHelperCategoryCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CostCategory.class));

    public CostCategoryExecutor(final IOnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<CostCategory> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperCategoryCost.get(id));
    }

    @Override
    public Result<Long> addPojo(final CostCategory costCategory) {
        return new Result<>(KEY_RESULT_ADD, dbHelperCategoryCost.add(costCategory));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelperCategoryCost.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final CostCategory costCategory) {
        return new Result<>(KEY_RESULT_EDIT, dbHelperCategoryCost.update(costCategory));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelperCategoryCost.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperCategoryCost.deleteAll());
    }

    @Override
    public Result<List<CostCategory>> getAllToList(final int selection) {
        if (selection == 0) {
//            all categories
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelperCategoryCost.getAllToList());
        } else if ( selection == 1 ) {
//            parents only
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelperCategoryCost.getAllToListByParentId(-1));
        }
            else return null;
    }

    @Override
    protected Result<List<CostCategory>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID, dbHelperCategoryCost.getAllToListByParentId(id));
    }
}
