package com.github.andrei1993ak.finances.util.universalLoader;

import com.github.andrei1993ak.finances.util.ContextHolder;

import java.io.File;

public class ImageNameGenerator {

    public static String getImagePath(final long id) {
        final File file = new File(ContextHolder.getInstance().getContext().getFilesDir() + "/images");

        if (!file.exists()) {
            file.mkdirs();
        }

        return ContextHolder.getInstance().getContext().getFilesDir() + "/images/" + String.valueOf(id) + ".jpg";
    }

    public static String getTempImagePath() {
        return ContextHolder.getInstance().getContext().getCacheDir() + "/temp.jpg";
    }
}

