package com.gmail.a93ak.andrei19.finance30;

import android.app.Application;

import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.Loaders.BitmapLoader;


public class App extends Application {
    private BitmapLoader bitmapLoader;

    public BitmapLoader getBitmapLoader(){
        if (bitmapLoader==null){
            bitmapLoader=new BitmapLoader(this);
        }
        return bitmapLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
    }
}
