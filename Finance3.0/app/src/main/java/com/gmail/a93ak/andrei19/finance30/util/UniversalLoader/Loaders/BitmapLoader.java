package com.gmail.a93ak.andrei19.finance30.util.universalLoader.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.gmail.a93ak.andrei19.finance30.util.universalLoader.UniversalLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapLoader extends UniversalLoader<Bitmap, ImageView> {


    private static BitmapLoader instance;

    public static BitmapLoader getInstance(final Context context) {
        if (instance == null)
            instance = new BitmapLoader(context);
        return instance;
    }

    private BitmapLoader(final Context context) {
        super(context);
    }

    @Override
    public int getSizeObj(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Override
    public Bitmap decodeFromFile(final File file, final int preSize) {
        try {
            final BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < preSize || height_tmp / 2 < preSize)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void set(final Bitmap bitmap, final ImageView imageView) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageDrawable(null);
        }
    }
}
