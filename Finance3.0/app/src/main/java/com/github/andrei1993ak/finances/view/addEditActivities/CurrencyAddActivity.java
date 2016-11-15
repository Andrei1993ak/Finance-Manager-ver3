package com.github.andrei1993ak.finances.view.addEditActivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.loaders.CurrencyAllCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.executors.CurrencyOfficialExecutor;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;

public class CurrencyAddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    public static final int MAIN_LOADER_ID = 0;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setTitle(R.string.allCurrencies);
        setContentView(R.layout.currency_add_activity);
        final String[] from = new String[]{CurrencyOfficial.NAME};
        final int[] to = new int[]{R.id.currencyName};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        final ListView lvAllCurrencies = (ListView) findViewById(R.id.lvAllCurrencies);
        lvAllCurrencies.setAdapter(simpleCursorAdapter);
        lvAllCurrencies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                new CurrencyOfficialExecutor(CurrencyAddActivity.this).execute(new RequestHolder<CurrencyOfficial>().get(id));
                return true;

            }
        });
        getSupportLoaderManager().initLoader(MAIN_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CurrencyAllCursorLoader(this);
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
        switch (result.getId()) {
            case CurrencyOfficialExecutor.KEY_RESULT_GET:
                final CurrencyOfficial currencyOfficial = (CurrencyOfficial) result.getObject();
                final Currency currency = new Currency();
                currency.setCode(currencyOfficial.getCode());
                currency.setName(currencyOfficial.getName());
                final Intent intent = new Intent();
                intent.putExtra(TableQueryGenerator.getTableName(Currency.class), currency);
                setResult(RESULT_OK, intent);
                finish();
        }
    }
}
