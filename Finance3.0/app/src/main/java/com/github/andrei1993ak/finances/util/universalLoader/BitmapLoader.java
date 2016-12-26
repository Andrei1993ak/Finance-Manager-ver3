package com.github.andrei1993ak.finances.util.universalLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapLoader extends UniversalLoader<Bitmap, ImageView> {


    BitmapLoader(final Context context) {
        super(context);
    }

    @Override
    public int getSizeObj(final Bitmap bitmap) {
        if (bitmap != null) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        } else
            return 0;
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
            return null;
        }
    }

    @Override
    public void set(final Bitmap bitmap, final ImageView imageView) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setBackgroundColor(Color.WHITE);
            imageView.setImageResource(0);
        }
    }
}
