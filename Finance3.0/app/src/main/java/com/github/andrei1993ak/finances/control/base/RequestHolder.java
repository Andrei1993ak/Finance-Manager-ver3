package com.github.andrei1993ak.finances.control.base;

import java.util.ArrayList;

public class RequestHolder<Model> {

    public static final int SELECTION_ALL = 0;
    public static final int SELECTION_PARENT_CATEGORIES = 1
            ;
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

    public Request get(final Long id) {
        return new Request<>(KEY_GET, id);
    }

    public Request delete(final Long id) {
        return new Request<>(KEY_DELETE, id);
    }

    public Request add(final Model model) {
        return new Request<>(KEY_ADD, model);
    }

    public Request edit(final Model model) {
        return new Request<>(KEY_EDIT, model);
    }

    public Request getAll() {
        return new Request<>(KEY_GET_ALL, null);
    }

    public Request deleteAll() {
        return new Request<>(KEY_DELETE_ALL, null);
    }

    public Request getAllToList(final Integer selection) {
        return new Request<>(KEY_GET_ALL_TO_LIST, selection);
    }

    public Request getAllToListByDates(final ArrayList<Long> dates) {
        return new Request<>(KEY_GET_ALL_TO_LIST_BY_DATES, dates);
    }

    public Request getAllToListByPurse(final Long purseId) {
        return new Request<>(KEY_GET_ALL_TO_LIST_BY_PURSE_ID, purseId);
    }

    public Request getAllToListByCategory(final Long categoryId) {
        return new Request<>(KEY_GET_ALL_TO_LIST_BY_CATEGORY_ID, categoryId);
    }
}
