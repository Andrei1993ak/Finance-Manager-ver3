package com.gmail.a93ak.andrei19.finance30.util;

import android.content.Context;

public class ContextHolder {

    private static ContextHolder instance;

    private Context mContext;

    private ContextHolder() {

    }

    public static ContextHolder getInstance() {
        if (instance == null) {
            instance = new ContextHolder();
        }

        return instance;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(final Context pContext) {
        mContext = pContext;
    }
}
