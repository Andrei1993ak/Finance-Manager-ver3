package com.github.andrei1993ak.finances;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.github.andrei1993ak.finances.model.DBHelpersManager;
import com.github.andrei1993ak.finances.model.dbHelpers.IDBHelperForModel;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.universalLoader.IUniversalLoader;


public class App extends Application {

    private DBHelpersManager dbHelpersManager;
    private IUniversalLoader<Bitmap, ImageView> loader;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelpersManager = new DBHelpersManager();
        ContextHolder.getInstance().setContext(this);
    }



    @Nullable
    public IDBHelperForModel getDbHelper(final Class<? extends TableClass> clazz) {
        return dbHelpersManager.getDBHelper(clazz);
    }

    public IUniversalLoader<Bitmap, ImageView> getImageLoader(Context context) {
        if (loader == null) {
            loader = IUniversalLoader.Impl.newInstance(context);
        }
        return loader;
    }

}
