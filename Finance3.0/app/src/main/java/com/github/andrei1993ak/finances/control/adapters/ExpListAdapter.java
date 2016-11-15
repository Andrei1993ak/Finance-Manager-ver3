package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleCursorTreeAdapter;

import com.github.andrei1993ak.finances.model.models.IncomeCategory;

import java.util.HashMap;

public class ExpListAdapter extends SimpleCursorTreeAdapter {

    private final Context context;
    private final HashMap<Integer, Integer> idToPos;
    private final HashMap<Integer, Integer> posToId;


    public ExpListAdapter(final Context context, final Cursor c, final int parentsLayout,
                          final String[] parentsFrom, final int[] parentsTo, final int childLayout,
                          final String[] childFrom, final int[] childTo) {
        super(context, c, parentsLayout, parentsFrom, parentsTo, childLayout, childFrom, childTo);
        this.context = context;
        posToId = new HashMap<>();
        idToPos = new HashMap<>();

    }

    @Override
    protected Cursor getChildrenCursor(final Cursor parentCursor) {
        final int groupPos = parentCursor.getPosition();
        final int groupId = parentCursor.getInt(parentCursor.getColumnIndex(IncomeCategory.ID));
        final LoaderManager supportLoaderManager = ((AppCompatActivity) context).getSupportLoaderManager();
        final Loader loader = supportLoaderManager.getLoader(groupId);
        posToId.put(groupId, groupPos);
        idToPos.put(groupPos, groupId);
        if (loader != null && !loader.isReset()) {
            supportLoaderManager.restartLoader(groupId, null, (LoaderManager.LoaderCallbacks<Cursor>) context);
        } else {
            supportLoaderManager.initLoader(groupId, null, (LoaderManager.LoaderCallbacks<Cursor>) context);
        }
        return null;
    }

    public HashMap<Integer, Integer> getIdToPos() {
        return idToPos;
    }

    public HashMap<Integer, Integer> getPosToId() {
        return posToId;
    }
}
