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
import com.github.andrei1993ak.finances.app.addEditActivities.IncomeAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.IncomeEditActivity;
import com.github.andrei1993ak.finances.control.adapters.IncomeCursorAdapter;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.IncomeExecutor;
import com.github.andrei1993ak.finances.control.loaders.IncomeCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.util.Constants;

public class IncomeActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private IncomeCursorAdapter incomeCursorAdapter;
    private RequestAdapter<Income> requestAdapter;
    private MenuInflater inflater;
    private long selectedItemId;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.incomes);
        initFields();
        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields() {
        this.inflater = getMenuInflater();
        this.requestAdapter = new RequestAdapter<>();
        this.incomeCursorAdapter = new IncomeCursorAdapter(this, null);
        this.selectedItemId = Constants.NOT_SELECTED;
        final ListView incomeListView = (ListView) findViewById(R.id.standardListView);
        incomeListView.setAdapter(incomeCursorAdapter);
        incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                selectedItemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(IncomeActivity.this, IncomeAddActivity.class);
                startActivityForResult(intent, Constants.ADD_REQUEST);
            }
        });
        registerForContextMenu(incomeListView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //TODO disable selection of ListView
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (selectedItemId != Constants.NOT_SELECTED) {
            if (id == R.id.action_edit) {
                final Intent intent = new Intent(this, IncomeEditActivity.class);
                intent.putExtra(Income.ID, selectedItemId);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            } else {
                new IncomeExecutor(this).execute(requestAdapter.delete(selectedItemId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        inflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case Constants.CM_DELETE_ID:
                new IncomeExecutor(this).execute(requestAdapter.delete(info.id));
                break;
            case Constants.CM_EDIT_ID:
                final Intent intent = new Intent(this, IncomeEditActivity.class);
                intent.putExtra(Income.ID, info.id);
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
                    final Income newIncome = data.getParcelableExtra(TableQueryGenerator.getTableName(Income.class));
                    new IncomeExecutor(this).execute(requestAdapter.add(newIncome));
                    break;
                case Constants.EDIT_REQUEST:
                    final Income editIncome = data.getParcelableExtra(TableQueryGenerator.getTableName(Income.class));
                    new IncomeExecutor(this).execute(requestAdapter.edit(editIncome));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new IncomeCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        incomeCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        incomeCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        if (id == IncomeExecutor.KEY_RESULT_DELETE || id == IncomeExecutor.KEY_RESULT_ADD || id == IncomeExecutor.KEY_RESULT_EDIT) {
            final Loader<Income> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }
}
