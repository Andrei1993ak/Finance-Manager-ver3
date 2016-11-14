package com.gmail.a93ak.andrei19.finance30;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;


public class App extends Application {

    private static String imagePath;
    private static String tempImagePath;

    //TODO move to Constants.class
    public static String PREFS = "themePrefs";
    public static String THEME = "theme";

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
        //TODO use getFilesDir()
        imagePath = getApplicationInfo().dataDir + "/files/images/";
        //TODO use getCacheDir()
        tempImagePath = Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
    }

    //TODO ImageNameGenerator
    public static String getImagePath(long id) {
        return imagePath + String.valueOf(id) + ".jpg";
    }

    public static String getTempImagePath() {
        return tempImagePath;
    }

}
