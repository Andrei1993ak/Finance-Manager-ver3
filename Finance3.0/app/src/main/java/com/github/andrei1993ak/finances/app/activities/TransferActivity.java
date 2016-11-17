package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.TransferAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.TransferEditActivity;
import com.github.andrei1993ak.finances.control.adapters.TransferCursorAdapter;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.TransferExecutor;
import com.github.andrei1993ak.finances.control.loaders.TransferCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;

public class TransferActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_TRANSFER_REQUEST = 1;
    private static final int EDIT_TRANSFER_REQUEST = 2;

    private static final int MAIN_LOADER_ID = 0;

    private TransferCursorAdapter transferCursorAdapter;
    //TODO move to adapter
    private RequestHolder<Transfer> requestHolder;
    private ListView transferListView;
    private long itemId = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.transfers);
        transferCursorAdapter = new TransferCursorAdapter(this, null);
        requestHolder = new RequestHolder<>();
        transferListView = (ListView) findViewById(R.id.standardListView);
        transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                itemId = id;
            }
        });
        transferListView.setAdapter(transferCursorAdapter);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(TransferActivity.this, TransferAddActivity.class);
                startActivityForResult(intent, ADD_TRANSFER_REQUEST);
            }
        });
        registerForContextMenu(transferListView);
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (itemId != -1) {
            if (id == R.id.action_edit) {
                final Intent intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(Transfer.ID, itemId);
                startActivityForResult(intent, EDIT_TRANSFER_REQUEST);
                return true;
            } else {
                new TransferExecutor(this).execute(requestHolder.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        //TODO move to xml
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
