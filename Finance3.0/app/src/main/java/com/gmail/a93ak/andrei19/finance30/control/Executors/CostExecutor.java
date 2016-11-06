package com.gmail.a93ak.andrei19.finance30.control.Executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCost;
import com.gmail.a93ak.andrei19.finance30.model.models.Cost;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.Loaders.BitmapLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class CostExecutor extends PojoExecutor<Cost> {

    public static final int KEY_RESULT_ADD = 701;
    public static final int KEY_RESULT_EDIT = 702;
    public static final int KEY_RESULT_DELETE = 703;
    public static final int KEY_RESULT_GET = 704;
    public static final int KEY_RESULT_GET_ALL = 705;
    public static final int KEY_RESULT_DELETE_ALL = 706;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 707;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 708;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID = 709;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 710;


    public static final String INTERNAL_PATH = "/data/data/com.gmail.a93ak.andrei19.finance30/files/images/";

    public CostExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Cost> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperCost.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(final Cost cost) {
        return new Result<>(KEY_RESULT_ADD, DBHelperCost.getInstance().add(cost));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {

        final DBHelperCost costDbHelper = DBHelperCost.getInstance();
        if (costDbHelper.get(id).getPhoto() == 1) {
            final File file = new File(INTERNAL_PATH + "/" + String.valueOf(id) + ".jpg");
            try {
                BitmapLoader.getInstance(ContextHolder.getInstance().getContext()).clearCashes(file.toURI().toURL().toString());
                file.delete();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new Result<>(KEY_RESULT_DELETE, DBHelperCost.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Cost cost) {
        try {
            final File file = new File(INTERNAL_PATH + "/" + String.valueOf(cost.getId()) + ".jpg");
            BitmapLoader.getInstance(ContextHolder.getInstance().getContext()).clearCashes(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Result<>(KEY_RESULT_EDIT, DBHelperCost.getInstance().update(cost));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperCost.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance().deleteAll());
    }

    @Override
    public Result<List<Cost>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance().getAllToList());
    }

    @Override
    protected Result<List<Cost>> getAllToListByDates(ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES, DBHelperCost.getInstance().getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Cost>> getAllToListByPurseId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID, DBHelperCost.getInstance().getAllToListByPurseId(id));
    }

    @Override
    protected Result<List<Cost>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID, DBHelperCost.getInstance().getAllToListByCategoryId(id));
    }
}
