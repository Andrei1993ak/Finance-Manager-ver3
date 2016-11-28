package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.util.ContextHolder;


import java.util.List;

public class IncomeCategoryExecutor extends PojoExecutor<IncomeCategory> {

    public static final int KEY_RESULT_ADD = 401;
    public static final int KEY_RESULT_EDIT = 402;
    public static final int KEY_RESULT_DELETE = 403;
    public static final int KEY_RESULT_GET = 404;
    public static final int KEY_RESULT_GET_ALL = 405;
    public static final int KEY_RESULT_DELETE_ALL = 406;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 407;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID = 408;

    private final DBHelperCategoryIncome helperCategoryIncome = ((DBHelperCategoryIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(IncomeCategory.class));

    public IncomeCategoryExecutor(final IOnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<IncomeCategory> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, helperCategoryIncome.get(id));
    }

    @Override
    public Result<Long> addPojo(final IncomeCategory incomeCategory) {
        return new Result<>(KEY_RESULT_ADD, helperCategoryIncome.add(incomeCategory));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, helperCategoryIncome.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final IncomeCategory incomeCategory) {
        return new Result<>(KEY_RESULT_EDIT, helperCategoryIncome.update(incomeCategory));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, helperCategoryIncome.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, helperCategoryIncome.deleteAll());
    }

    @Override
    public Result<List<IncomeCategory>> getAllToList(final int selection) {
        if (selection == 0) {
//            all categories
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, helperCategoryIncome.getAllToList());
        } else if ( selection == 1 ) {
//            parents only
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, helperCategoryIncome.getAllToListByParentId(-1));
        }
            else return null;
    }

    @Override
    protected Result<List<IncomeCategory>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID, helperCategoryIncome.getAllToListByParentId(id));
    }
}
