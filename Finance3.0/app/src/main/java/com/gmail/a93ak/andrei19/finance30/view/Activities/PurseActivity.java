package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PurseCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseEditActivity;

public class PurseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_PURSE_REQUEST = 1;
    private static final int EDIT_PURSE_REQUEST = 2;

    public static final int MAIN_LOADER_ID = 0;

    private PurseCursorAdapter purseCursorAdapter;
    private RequestHolder<Purse> requestHolder;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_activity);
        requestHolder = new RequestHolder<>();
        final ListView purseListVIew = (ListView) findViewById(R.id.purseListView);
        purseCursorAdapter = new PurseCursorAdapter(this, null);
        purseListVIew.setAdapter(purseCursorAdapter);
        registerForContextMenu(purseListVIew);
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }

    public void addPurse(final View view) {
        final Intent intent = new Intent(this, PurseAddActivity.class);
        startActivityForResult(intent, ADD_PURSE_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.rename);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                new PurseExecutor(this).execute(requestHolder.delete(info.id));
                break;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, PurseEditActivity.class);
                intent.putExtra(Purse.ID, info.id);
                startActivityForResult(intent, EDIT_PURSE_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_PURSE_REQUEST:
                    final Purse newPurse = data.getParcelableExtra(TableQueryGenerator.getTableName(Purse.class));
                    new PurseExecutor(this).execute(requestHolder.add(newPurse));
                    break;
                case EDIT_PURSE_REQUEST:
                    final Purse editPurse = data.getParcelableExtra(TableQueryGenerator.getTableName(Purse.class));
                    new PurseExecutor(this).execute(requestHolder.edit(editPurse));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        purseCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        purseCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        switch (id) {
            case PurseExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeletePurse, Toast.LENGTH_LONG).show();
                } else {
                    if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                        getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                    }
                }
                break;
            default:
                break;
        }
    }
}
