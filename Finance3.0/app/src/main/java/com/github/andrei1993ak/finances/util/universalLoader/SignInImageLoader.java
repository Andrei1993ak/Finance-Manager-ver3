package com.github.andrei1993ak.finances.util.universalLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.github.andrei1993ak.finances.util.universalLoader.BitmapLoader;

public class SignInImageLoader extends BitmapLoader {

    public SignInImageLoader(final Context context) {
        super(context);
    }

    @Override
    public void set(final Bitmap bitmap, final ImageView imageView) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
