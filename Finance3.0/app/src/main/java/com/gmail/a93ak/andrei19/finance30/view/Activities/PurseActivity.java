package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseEditActivity;

public class PurseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {
//
    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_PURSE_REQUEST = 1;
    private static final int EDIT_PURSE_REQUEST = 2;

    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestHolder<Purse> requestHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_activity);
        requestHolder = new RequestHolder<>();
        ListView purseListVIew = (ListView) findViewById(R.id.purseListView);
        String[] from = new String[]{DBHelper.PURSES_KEY_NAME, DBHelper.PURSES_KEY_AMOUNT, DBHelperPurse.CURRENCY_NAME};
        int[] to = new int[]{R.id.purseName, R.id.purseAmount, R.id.purseCurrency};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.purse_listitem, null, from, to, 0);
        purseListVIew.setAdapter(simpleCursorAdapter);
        registerForContextMenu(purseListVIew);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void addPurse(View view) {
        Intent intent = new Intent(this, PurseAddActivity.class);
        startActivityForResult(intent, ADD_PURSE_REQUEST);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.rename);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                requestHolder.setDeleteRequest(info.id);
                new PurseExecutor(this).execute(requestHolder.getDeleteRequest());
                break;
            case CM_EDIT_ID:
                Intent intent = new Intent(this, PurseEditActivity.class);
                intent.putExtra(DBHelper.PURSES_KEY_ID, info.id);
                startActivityForResult(intent, EDIT_PURSE_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_PURSE_REQUEST:
                    Purse newPurse = new Purse();
                    newPurse.setName(data.getStringExtra(DBHelper.PURSES_KEY_NAME));
                    newPurse.setAmount(data.getDoubleExtra(DBHelper.PURSES_KEY_AMOUNT, -1.0));
                    newPurse.setCurrency_id(data.getLongExtra(DBHelper.PURSES_KEY_CURRENCY_ID, -1));
                    requestHolder.setAddRequest(newPurse);
                    new PurseExecutor(this).execute(requestHolder.getAddRequest());
                    break;
                case EDIT_PURSE_REQUEST:
                    Purse editPurse = new Purse();
                    editPurse.setId(data.getLongExtra(DBHelper.PURSES_KEY_ID, -1));
                    editPurse.setName(data.getStringExtra(DBHelper.PURSES_KEY_NAME));
                    editPurse.setAmount(data.getDoubleExtra(DBHelper.PURSES_KEY_AMOUNT, -1.0));
                    editPurse.setCurrency_id(data.getLongExtra(DBHelper.PURSES_KEY_CURRENCY_ID, -1));
                    requestHolder.setEditRequest(editPurse);
                    new PurseExecutor(this).execute(requestHolder.getEditRequest());
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(Result result) {
        int id = result.getId();
        switch (id) {
            case PurseExecutor.KEY_RESULT_DELETE:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            default:
                Log.e("FinancePMError", "Unknown result code: " + id);
        }
    }
}
