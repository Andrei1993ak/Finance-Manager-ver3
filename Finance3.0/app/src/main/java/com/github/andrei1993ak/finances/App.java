package com.github.andrei1993ak.finances;

import android.app.Application;
import android.support.annotation.Nullable;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperForModel;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperTransfer;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.util.ContextHolder;


public class App extends Application {

    private  DBHelperTransfer dbHelperTransfer = null;


    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
    }

    @Nullable
    public  DBHelperForModel getDbHelper(final Class<? extends TableClass> clazz) {
        if (clazz.getClass().isAssignableFrom(Transfer.class.getClass())) {
            if (dbHelperTransfer == null) {
                dbHelperTransfer = new DBHelperTransfer();
            }
            return dbHelperTransfer;
        } else {
            return null;
        }
    }
}
