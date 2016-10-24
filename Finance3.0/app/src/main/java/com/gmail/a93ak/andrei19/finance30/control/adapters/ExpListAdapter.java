package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleCursorTreeAdapter;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;

import java.util.HashMap;

public class ExpListAdapter extends SimpleCursorTreeAdapter {

    private Context context;
    private HashMap<Integer, Integer> idToPos;
    private HashMap<Integer, Integer> posToId;


    public ExpListAdapter(Context context, Cursor c, int parentsLayout, String[] parentsFrom, int[] parentsTo,
                          int childLayout, String[] childFrom, int[] childTo) {
        super(context, c, parentsLayout, parentsFrom, parentsTo, childLayout, childFrom, childTo);
        this.context = context;
        posToId = new HashMap<>();
        idToPos = new HashMap<>();

    }

    @Override
    protected Cursor getChildrenCursor(Cursor parentCursor) {
        int groupPos = parentCursor.getPosition();
        int groupId = parentCursor.getInt(parentCursor.getColumnIndex(DBHelper.INCOME_CATEGORY_KEY_ID));
        LoaderManager supportLoaderManager = ((AppCompatActivity) context).getSupportLoaderManager();
        Loader loader = supportLoaderManager.getLoader(groupId);
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
