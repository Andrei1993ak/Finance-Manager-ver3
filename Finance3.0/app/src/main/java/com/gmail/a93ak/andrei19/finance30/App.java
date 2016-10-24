package com.gmail.a93ak.andrei19.finance30;

import android.app.Application;

import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
    }
}
