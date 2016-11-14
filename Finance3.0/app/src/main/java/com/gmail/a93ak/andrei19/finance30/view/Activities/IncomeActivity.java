package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.adapters.IncomeCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.control.executors.IncomeExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.IncomeCursorLoader;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeEditActivity;

public class IncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_INCOME_REQUEST = 1;
    private static final int EDIT_INCOME_REQUEST = 2;

    private static final int MAIN_LOADER_ID = 0;

    private IncomeCursorAdapter incomeCursorAdapter;
    private RequestHolder<Income> requestHolder;
    private ListView incomeListView;
    private long itemId = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setTitle(R.string.incomes);
        setContentView(R.layout.standart_activity);
        requestHolder = new RequestHolder<>();
        incomeCursorAdapter = new IncomeCursorAdapter(this, null);
        incomeListView = (ListView) findViewById(R.id.standartListView);
        incomeListView.setAdapter(incomeCursorAdapter);
        incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                itemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(IncomeActivity.this, IncomeAddActivity.class);
                startActivityForResult(intent, ADD_INCOME_REQUEST);
            }
        });
        registerForContextMenu(incomeListView);
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
                final Intent intent = new Intent(this, IncomeEditActivity.class);
                intent.putExtra(Income.ID, itemId);
                startActivityForResult(intent, EDIT_INCOME_REQUEST);
                return true;
            } else {
                new IncomeExecutor(this).execute(requestHolder.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                new IncomeExecutor(this).execute(requestHolder.delete(info.id));
                break;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, IncomeEditActivity.class);
                intent.putExtra(Income.ID, info.id);
                startActivityForResult(intent, EDIT_INCOME_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_INCOME_REQUEST:
                    final Income newIncome = data.getParcelableExtra(TableQueryGenerator.getTableName(Income.class));
                    new IncomeExecutor(this).execute(requestHolder.add(newIncome));
                    break;
                case EDIT_INCOME_REQUEST:
                    final Income editIncome = data.getParcelableExtra(TableQueryGenerator.getTableName(Income.class));
                    new IncomeExecutor(this).execute(requestHolder.edit(editIncome));
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
            if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
            }
        }
    }
}
