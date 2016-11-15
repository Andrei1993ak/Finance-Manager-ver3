//TODO move to app_package.app
package com.github.andrei1993ak.finances.view.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyExecutor;
import com.github.andrei1993ak.finances.control.loaders.CurrencyCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.view.addEditActivities.CurrencyAddActivity;
import com.github.andrei1993ak.finances.view.addEditActivities.CurrencyEditActivity;

public class CurrencyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int ADD_CURRENCY_REQUEST = 1;
    private static final int EDIT_CURRENCY_REQUEST = 2;

    public static final int MAIN_LOADER_ID = 0;

    private long itemId = -1;

    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestHolder<Currency> requestHolder;
    private ListView lvCurrencies;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.currencies);
        final String[] from = new String[]{Currency.NAME};
        final int[] to = new int[]{R.id.currencyName};
        requestHolder = new RequestHolder<>();
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        lvCurrencies = (ListView) findViewById(R.id.standartListView);
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
                startActivityForResult(intent, ADD_CURRENCY_REQUEST);
            }
        });
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
                final Intent intent = new Intent(this, CurrencyEditActivity.class);
                intent.putExtra(Currency.ID, itemId);
                startActivityForResult(intent, EDIT_CURRENCY_REQUEST);
                return true;
            } else {
                new CurrencyExecutor(this).execute(requestHolder.delete(itemId));
                return true;
            }
        }
        return false;
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
