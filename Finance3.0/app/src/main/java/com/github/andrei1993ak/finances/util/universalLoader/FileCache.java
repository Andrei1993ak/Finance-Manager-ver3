package com.github.andrei1993ak.finances.util.universalLoader;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
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
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    @Nullable
    File getFile(final String url) {
        final String filename;
        try {
            filename = URLEncoder.encode(url, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return null;
        }
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
        final String filename;
        try {
            filename = URLEncoder.encode(url, "UTF-8");
            final File[] file = cacheDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(final File dir, final String name) {
                    return name.equals(filename);
                }
            });
            if (file.length != 0) {
                file[0].delete();
            }
        } catch (final UnsupportedEncodingException e) {
            clear();
        }


    }
}
