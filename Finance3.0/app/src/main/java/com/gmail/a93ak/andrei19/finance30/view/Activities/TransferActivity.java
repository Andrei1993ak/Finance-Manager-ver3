package com.gmail.a93ak.andrei19.finance30.view.activities;

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

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.TransferExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.TransferCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.TransferCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Transfer;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.TransferAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.TransferEditActivity;

public class TransferActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_TRANSFER_REQUEST = 1;
    private static final int EDIT_TRANSFER_REQUEST = 2;

    private static final int MAIN_LOADER_ID = 0;

    private TransferCursorAdapter transferCursorAdapter;
    private RequestHolder<Transfer> requestHolder;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity);
        transferCursorAdapter = new TransferCursorAdapter(this, null);
        requestHolder = new RequestHolder<>();
        final ListView transferListView = (ListView) findViewById(R.id.transferListView);
        transferListView.setAdapter(transferCursorAdapter);
        registerForContextMenu(transferListView);
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }

    public void addTransfer(final View view) {
        final Intent intent = new Intent(this, TransferAddActivity.class);
        startActivityForResult(intent, ADD_TRANSFER_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                new TransferExecutor(this).execute(requestHolder.delete(info.id));
                break;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(Transfer.ID, info.id);
                startActivityForResult(intent, EDIT_TRANSFER_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_TRANSFER_REQUEST:
                    final Transfer newTransfer = data.getParcelableExtra(TableQueryGenerator.getTableName(Transfer.class));
                    new TransferExecutor(this).execute(requestHolder.add(newTransfer));
                    break;
                case EDIT_TRANSFER_REQUEST:
                    final Transfer editTransfer = data.getParcelableExtra(TableQueryGenerator.getTableName(Transfer.class));
                    new TransferExecutor(this).execute(requestHolder.edit(editTransfer));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new TransferCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        transferCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        transferCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        if (id == TransferExecutor.KEY_RESULT_DELETE || id == TransferExecutor.KEY_RESULT_ADD || id == TransferExecutor.KEY_RESULT_EDIT) {
            if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
            }
        }
    }
}
