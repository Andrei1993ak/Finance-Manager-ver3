package com.github.andrei1993ak.finances.control.base;

import java.util.ArrayList;

public class RequestAdapter<Model> {

    public static final int SELECTION_ALL = 0;
    public static final int SELECTION_PARENT_CATEGORIES = 1;

    public Request get(final Long id) {
        return new Request<>(Operations.KEY_GET, id);
    }

    public Request delete(final Long id) {
        return new Request<>(Operations.KEY_DELETE, id);
    }

    public Request add(final Model model) {
        return new Request<>(Operations.KEY_ADD, model);
    }

    public Request edit(final Model model) {
        return new Request<>(Operations.KEY_EDIT, model);
    }

    public Request getAll() {
        return new Request<>(Operations.KEY_GET_ALL, null);
    }

    public Request deleteAll() {
        return new Request<>(Operations.KEY_DELETE_ALL, null);
    }

    public Request getAllToList(final Integer selection) {
        return new Request<>(Operations.KEY_GET_ALL_TO_LIST, selection);
    }

    public Request getAllToListByDates(final ArrayList<Long> dates) {
        return new Request<>(Operations.KEY_GET_ALL_TO_LIST_BY_DATES, dates);
    }

    public Request getAllToListByWallet(final Long walletID) {
        return new Request<>(Operations.KEY_GET_ALL_TO_LIST_BY_WALLET_ID, walletID);
    }

    public Request getAllToListByCategory(final Long categoryId) {
        return new Request<>(Operations.KEY_GET_ALL_TO_LIST_BY_CATEGORY_ID, categoryId);
    }
}
