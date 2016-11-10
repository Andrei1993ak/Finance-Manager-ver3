package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.CurrencyCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CurrencyAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CurrencyEditActivity;

public class CurrencyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_CURRENCY_REQUEST = 1;
    private static final int EDIT_CURRENCY_REQUEST = 2;

    public static final int MAIN_LOADER_ID = 0;

    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestHolder<Currency> requestHolder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_activity);
        final String[] from = new String[]{Currency.NAME};
        final int[] to = new int[]{R.id.currencyName};
        requestHolder = new RequestHolder<>();
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        final ListView lvCurrencies = (ListView) findViewById(R.id.currencyListView);
        lvCurrencies.setAdapter(simpleCursorAdapter);
        registerForContextMenu(lvCurrencies);
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }

    public void addCurrency(final View view) {
        final Intent intent = new Intent(this, CurrencyAddActivity.class);
        startActivityForResult(intent, ADD_CURRENCY_REQUEST);
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
                new CurrencyExecutor(this).execute(requestHolder.delete(info.id));
                break;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, CurrencyEditActivity.class);
                intent.putExtra(Currency.ID, info.id);
                startActivityForResult(intent, EDIT_CURRENCY_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CURRENCY_REQUEST:
                    final Currency newCurrency = data.getParcelableExtra(TableQueryGenerator.getTableName(Currency.class));
                    new CurrencyExecutor(this).execute(requestHolder.add(newCurrency));
                    break;
                case EDIT_CURRENCY_REQUEST:
                    final Currency editCurrency = data.getParcelableExtra(TableQueryGenerator.getTableName(Currency.class));
                    new CurrencyExecutor(this).execute(requestHolder.edit(editCurrency));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CurrencyCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        switch (id) {
            case CurrencyExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeleteCur, Toast.LENGTH_LONG).show();
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
