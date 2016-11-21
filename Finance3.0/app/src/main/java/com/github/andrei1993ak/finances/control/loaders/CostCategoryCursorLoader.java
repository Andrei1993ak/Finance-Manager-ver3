package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class CostCategoryCursorLoader extends CursorLoader {

    private final DBHelperCategoryCost helper = ((DBHelperCategoryCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CostCategory.class));
    private final long id;

    public CostCategoryCursorLoader(final Context context, final long id) {
        super(context);
        this.id = id;
    }

    @Override
    public Cursor loadInBackground() {
        return helper.getAllByParentId(id);
    }
}


