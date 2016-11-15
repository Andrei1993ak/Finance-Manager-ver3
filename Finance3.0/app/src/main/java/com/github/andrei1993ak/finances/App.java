package com.github.andrei1993ak.finances;

import android.app.Application;
import android.os.Environment;

import com.github.andrei1993ak.finances.util.ContextHolder;


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
