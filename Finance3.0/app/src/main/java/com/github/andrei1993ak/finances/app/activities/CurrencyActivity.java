package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CurrencyAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CurrencyEditActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyExecutor;
import com.github.andrei1993ak.finances.control.loaders.CurrencyCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.util.Constants;

public class CurrencyActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestAdapter<Currency> requestAdapter;
    private MenuInflater inflater;
    private long itemId = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.currencies);
        initFields();
        inflater = getMenuInflater();
        requestAdapter = new RequestAdapter<>();
        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields() {
        final String[] from = new String[]{Currency.NAME};
        final int[] to = new int[]{R.id.currencyName};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        final ListView lvCurrencies = (ListView) findViewById(R.id.standardListView);
        lvCurrencies.setAdapter(simpleCursorAdapter);
        lvCurrencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                itemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(CurrencyActivity.this, CurrencyAddActivity.class);
                startActivityForResult(intent, Constants.ADD_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (itemId != -1) {
            if (id == R.id.action_edit) {
                final Intent intent = new Intent(this, CurrencyEditActivity.class);
                intent.putExtra(Currency.ID, itemId);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            } else {
                new CurrencyExecutor(this).execute(requestAdapter.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_REQUEST:
                    final Currency newCurrency = data.getParcelableExtra(TableQueryGenerator.getTableName(Currency.class));
                    new CurrencyExecutor(this).execute(requestAdapter.add(newCurrency));
                    break;
                case Constants.EDIT_REQUEST:
                    final Currency editCurrency = data.getParcelableExtra(TableQueryGenerator.getTableName(Currency.class));
                    new CurrencyExecutor(this).execute(requestAdapter.edit(editCurrency));
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
        final Loader<Currency> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
        switch (id) {
            case CurrencyExecutor.KEY_RESULT_ADD:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_EDIT:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeleteCur, Toast.LENGTH_LONG).show();
                } else {
                    if (loader != null) {
                        loader.forceLoad();
                    }
                }
                break;
            default:
                break;
        }

    }
}
