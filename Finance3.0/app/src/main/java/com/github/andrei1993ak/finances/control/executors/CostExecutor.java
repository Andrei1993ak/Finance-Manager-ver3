package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.universalLoader.ImageNameGenerator;
import com.github.andrei1993ak.finances.util.universalLoader.loaders.BitmapLoader;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.model.models.Cost;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class CostExecutor extends PojoExecutor<Cost> {

    private DBHelperCost dbHelperCost = ((DBHelperCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Cost.class));
    public static final int KEY_RESULT_ADD = 701;
    public static final int KEY_RESULT_EDIT = 702;
    public static final int KEY_RESULT_DELETE = 703;
    public static final int KEY_RESULT_GET = 704;
    public static final int KEY_RESULT_GET_ALL = 705;
    public static final int KEY_RESULT_DELETE_ALL = 706;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 707;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 708;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID = 709;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 710;

    public CostExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Cost> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperCost.get(id));
    }

    @Override
    public Result<Long> addPojo(final Cost cost) {
        return new Result<>(KEY_RESULT_ADD, dbHelperCost.add(cost));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {

        final String path = ImageNameGenerator.getImagePath(DBHelper.getInstance(ContextHolder.getInstance().getContext()).getNextCostId());
        if (dbHelperCost.get(id).getPhoto() == 1) {
            final File file = new File(path);
            try {
                BitmapLoader.getInstance(ContextHolder.getInstance().getContext()).clearCashes(file.toURI().toURL().toString());
                file.delete();
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new Result<>(KEY_RESULT_DELETE, dbHelperCost.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Cost cost) {
        final String path = ImageNameGenerator.getImagePath(DBHelper.getInstance(ContextHolder.getInstance().getContext()).getNextCostId());        try {
            final File file = new File(path);
            BitmapLoader.getInstance(ContextHolder.getInstance().getContext()).clearCashes(file.toURI().toURL().toString());
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return new Result<>(KEY_RESULT_EDIT, dbHelperCost.update(cost));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelperCost.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperCost.deleteAll());
    }

    @Override
    public Result<List<Cost>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperCost.getAllToList());
    }

    @Override
    protected Result<List<Cost>> getAllToListByDates(final ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES, dbHelperCost.getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Cost>> getAllToListByWalletId(final Long walletId) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID, dbHelperCost.getAllToListByWalletId(walletId));
    }

    @Override
    protected Result<List<Cost>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID, dbHelperCost.getAllToListByCategoryId(id));
    }
}
