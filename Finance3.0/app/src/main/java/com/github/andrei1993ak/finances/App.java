package com.github.andrei1993ak.finances;

import android.app.Application;

import com.github.andrei1993ak.finances.util.ContextHolder;

import java.io.File;


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
        imagePath = getFilesDir() + "/images/";
        final File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        tempImagePath = getCacheDir() + "/temp.jpg";
    }

    //TODO ImageNameGenerator
    public static String getImagePath(final long id) {
        return imagePath + String.valueOf(id) + ".jpg";
    }

    public static String getTempImagePath() {
        return tempImagePath;
    }

}
