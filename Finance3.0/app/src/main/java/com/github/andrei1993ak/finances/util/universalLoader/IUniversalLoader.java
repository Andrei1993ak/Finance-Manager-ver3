package com.github.andrei1993ak.finances.util.universalLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.github.andrei1993ak.finances.util.ContextHolder;

import java.io.File;

public interface IUniversalLoader<K, V> {

    int getSizeObj(K myObj);

    K decodeFromFile(File file, int PreSize);

    void set(K myObj, V destination);

    void clearCashes(String url);

    void load(String url,V destination);

    final class Impl {

        public static IUniversalLoader<Bitmap, ImageView> newInstance(Context context) {
            return new BitmapLoader(context);
        }
    }
}
