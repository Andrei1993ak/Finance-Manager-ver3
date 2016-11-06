package com.gmail.a93ak.andrei19.finance30.control.base;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class PojoExecutor<Model> extends AsyncTask<Request<Model>, Void, Result> {
    //
    private static final int KEY_ADD = 1;
    private static final int KEY_EDIT = 2;
    private static final int KEY_DELETE = 3;
    private static final int KEY_GET = 4;
    private static final int KEY_GET_ALL = 5;
    private static final int KEY_DELETE_ALL = 6;
    private static final int KEY_GET_ALL_TO_LIST = 7;
    private static final int KEY_GET_ALL_TO_LIST_BY_CATEGORY_ID = 8;
    private static final int KEY_GET_ALL_TO_LIST_BY_PURSE_ID = 9;
    private static final int KEY_GET_ALL_TO_LIST_BY_DATES = 10;

    private final OnTaskCompleted listener;
    protected Context context;

    public PojoExecutor(final OnTaskCompleted listener) {
        this.listener = listener;
        context = ContextHolder.getInstance().getContext();
    }

    @Override
    protected Result doInBackground(final Request<Model>... requests) {
        Result result = null;
        switch (requests[0].getId()) {
            case KEY_ADD:
                result = addPojo(requests[0].getObject());
                break;
            case KEY_DELETE:
                result = deletePojo((Long) requests[0].getObject());
                break;
            case KEY_EDIT:
                result = updatePojo(requests[0].getObject());
                break;
            case KEY_GET:
                result = getPojo((Long) requests[0].getObject());
                break;
            case KEY_GET_ALL:
                result = getAll();
                break;
            case KEY_DELETE_ALL:
                result = deleteAll();
                break;
            case KEY_GET_ALL_TO_LIST:
                result = getAllToList((Integer) requests[0].getObject());
                break;
            case KEY_GET_ALL_TO_LIST_BY_CATEGORY_ID:
                result = getAllToListByCategoryId((Long) requests[0].getObject());
                break;
            case KEY_GET_ALL_TO_LIST_BY_PURSE_ID:
                result = getAllToListByPurseId((Long) requests[0].getObject());
                break;
            case KEY_GET_ALL_TO_LIST_BY_DATES:
                result = getAllToListByDates((ArrayList<Long>) requests[0].getObject());
        }
        return result;
    }

    protected Result<List<Model>> getAllToListByDates(final ArrayList<Long> dates) {
        return null;
    }

    protected Result<List<Model>> getAllToListByPurseId(final Long purseId) {
        return null;
    }

    protected Result<List<Model>> getAllToListByCategoryId(final Long categoryId) {
        return null;
    }

    abstract public Result<Model> getPojo(long id);

    abstract public Result<Long> addPojo(Model model);

    abstract public Result<Integer> deletePojo(long id);

    abstract public Result<Integer> updatePojo(Model model);

    abstract public Result<Cursor> getAll();

    abstract public Result<Integer> deleteAll();

    abstract public Result<List<Model>> getAllToList(int selection);

    @Override
    protected void onPostExecute(final Result result) {
        if (listener != null)
            listener.onTaskCompleted(result);
    }
}
