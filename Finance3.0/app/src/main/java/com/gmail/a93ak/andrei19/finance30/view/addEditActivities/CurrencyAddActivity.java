package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

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

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CurrencyOfficialExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.CurrencyAllCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CurrencyOfficial;

public class CurrencyAddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,OnTaskCompleted {

    public static final String NAME = "name";
    public static final String CODE = "code";
    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestHolder<CurrencyOfficial> requestHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_add_activity);
        String[] from = new String[]{DBHelper.CURRENCY_FROM_WEB_KEY_NAME};
        int[] to = new int[]{R.id.currencyName};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        ListView lvAllCurrencies = (ListView) findViewById(R.id.lvAllCurrencies);
        lvAllCurrencies.setAdapter(simpleCursorAdapter);
        requestHolder = new RequestHolder<>();
        lvAllCurrencies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                requestHolder.setGetRequest(id);
                new CurrencyOfficialExecutor(CurrencyAddActivity.this).execute(requestHolder.getGetRequest());
                return true;

            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CurrencyAllCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()){
            case CurrencyOfficialExecutor.KEY_RESULT_GET:
                CurrencyOfficial currencyOfficial = (CurrencyOfficial) result.getT();
                Intent intent = new Intent();
                intent.putExtra(NAME,currencyOfficial.getName());
                intent.putExtra(CODE,currencyOfficial.getCode());
                setResult(RESULT_OK,intent);
                finish();
        }
    }
}
