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
import android.view.MenuInflater;
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
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.TransferExecutor;
import com.github.andrei1993ak.finances.control.loaders.TransferCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.util.Constants;

public class TransferActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private TransferCursorAdapter transferCursorAdapter;
    private RequestAdapter<Transfer> requestAdapter;
    private MenuInflater inflater;
    private long itemId = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.transfers);
        initFields();
        requestAdapter = new RequestAdapter<>();
        inflater = getMenuInflater();
        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields(){
        transferCursorAdapter = new TransferCursorAdapter(this, null);
        final ListView transferListView = (ListView) findViewById(R.id.standardListView);
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
                startActivityForResult(intent, Constants.ADD_REQUEST);
            }
        });
        registerForContextMenu(transferListView);
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
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            } else {
                new TransferExecutor(this).execute(requestAdapter.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {
        inflater.inflate(R.menu.context_menu,menu);
        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.cm_delete:
                new TransferExecutor(this).execute(requestAdapter.delete(info.id));
                break;
            case R.id.cm_edit:
                final Intent intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(Transfer.ID, info.id);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_REQUEST:
                    final Transfer newTransfer = data.getParcelableExtra(TableQueryGenerator.getTableName(Transfer.class));
                    new TransferExecutor(this).execute(requestAdapter.add(newTransfer));
                    break;
                case Constants.EDIT_REQUEST:
                    final Transfer editTransfer = data.getParcelableExtra(TableQueryGenerator.getTableName(Transfer.class));
                    new TransferExecutor(this).execute(requestAdapter.edit(editTransfer));
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
            final Loader<Transfer> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }
}
