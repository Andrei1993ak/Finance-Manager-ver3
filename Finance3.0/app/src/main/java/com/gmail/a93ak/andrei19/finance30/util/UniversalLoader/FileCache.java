package com.gmail.a93ak.andrei19.finance30.util.UniversalLoader;

import android.content.Context;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URLEncoder;

class FileCache {

    private static final String IMAGES_CACHE = "imagesCache";
    private static FileCache instance;
    private File cacheDir;

    public static FileCache getInstance(Context context) {
        if (instance == null)
            instance = new FileCache(context);
        return instance;
    }

    private FileCache(Context context) {
        cacheDir = new File(context.getCacheDir(), IMAGES_CACHE);
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    File getFile(String url) {
        String filename = URLEncoder.encode(url);
        return new File(cacheDir, filename);
    }

    void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

    void clear(String url){
        final String filename = URLEncoder.encode(url);
        File[] file = cacheDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equals(filename);
            }
        });
        file[0].delete();
    }
}
