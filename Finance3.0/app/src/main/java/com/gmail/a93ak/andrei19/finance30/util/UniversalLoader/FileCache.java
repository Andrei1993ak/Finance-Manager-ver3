package com.gmail.a93ak.andrei19.finance30.util.universalLoader;

import android.content.Context;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URLEncoder;

class FileCache {

    private static final String IMAGES_CACHE = "imagesCache";
    private static FileCache instance;
    private final File cacheDir;

    public static FileCache getInstance(final Context context) {
        if (instance == null)
            instance = new FileCache(context);
        return instance;
    }

    private FileCache(final Context context) {
        cacheDir = new File(context.getCacheDir(), IMAGES_CACHE);
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    File getFile(final String url) {
        final String filename = URLEncoder.encode(url);
        return new File(cacheDir, filename);
    }

    void clear() {
        final File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (final File f : files)
            f.delete();
    }

    void clear(final String url) {
        final String filename = URLEncoder.encode(url);
        final File[] file = cacheDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equals(filename);
            }
        });
        if (file.length != 0) {
            file[0].delete();
        }
    }
}
