package com.github.andrei1993ak.finances.app.addEditActivities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyOfficialExecutor;
import com.github.andrei1993ak.finances.control.loaders.CurrencyAllCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.Constants;

public class CurrencyAddActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.allCurrencies);
        setContentView(R.layout.currency_add_activity);
        initFields();
        getSupportLoaderManager().initLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields(){
        final String[] from = new String[]{CurrencyOfficial.NAME};
        final int[] to = new int[]{R.id.currencyName};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        final ListView lvAllCurrencies = (ListView) findViewById(R.id.lvAllCurrencies);
        lvAllCurrencies.setAdapter(simpleCursorAdapter);
        lvAllCurrencies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                new CurrencyOfficialExecutor(CurrencyAddActivity.this).execute(new RequestAdapter<CurrencyOfficial>().get(id));
                return true;

            }
        });
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
