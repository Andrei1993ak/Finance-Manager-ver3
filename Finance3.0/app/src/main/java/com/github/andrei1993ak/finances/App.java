package com.github.andrei1993ak.finances;

import android.app.Application;
import android.support.annotation.Nullable;

import com.github.andrei1993ak.finances.model.dbHelpers.IDBHelperForModel;
import com.github.andrei1993ak.finances.model.DBHelpersManager;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.CursorUtils;


public class App extends Application {

    private DBHelpersManager dbHelpersManager;
    private CursorUtils cursorUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelpersManager = new DBHelpersManager();
        cursorUtils = new CursorUtils();
        ContextHolder.getInstance().setContext(this);
    }

    @Nullable
    public IDBHelperForModel getDbHelper(final Class<? extends TableClass> clazz) {
        return dbHelpersManager.getDBHelper(clazz);
    }

    public CursorUtils getCursorUtils() {
        return cursorUtils;
    }
}
