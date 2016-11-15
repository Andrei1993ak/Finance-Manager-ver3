package com.github.andrei1993ak.finances.util;

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
