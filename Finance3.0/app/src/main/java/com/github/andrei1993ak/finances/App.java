package com.github.andrei1993ak.finances;

import android.app.Application;

import com.github.andrei1993ak.finances.util.ContextHolder;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
    }
}
