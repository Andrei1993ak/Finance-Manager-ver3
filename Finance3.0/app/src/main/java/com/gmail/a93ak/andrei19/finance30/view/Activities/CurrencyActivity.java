package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.CurrencyCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CurrencyAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CurrencyEditActivity;

public class CurrencyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_CURRENCY_REQUEST = 1;
    private static final int EDIT_CURRENCY_REQUEST = 2;

    public static final String ID = "Id";
    public static final String NAME = "name";
    public static final String CODE = "code";

    private SimpleCursorAdapter simpleCursorAdapter;
    private RequestHolder<Currency> requestHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_activity);
        String[] from = new String[]{DBHelper.CURRENCY_KEY_NAME};
        int[] to = new int[]{R.id.currencyName};
        requestHolder = new RequestHolder<>();
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_listitem, null, from, to, 0);
        ListView lvCurrencies = (ListView) findViewById(R.id.currencyListView);
        lvCurrencies.setAdapter(simpleCursorAdapter);
        registerForContextMenu(lvCurrencies);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void addCurrency(View view) {
        Intent intent = new Intent(this, CurrencyAddActivity.class);
        startActivityForResult(intent, ADD_CURRENCY_REQUEST);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.rename);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                requestHolder.setDeleteRequest(info.id);
                new CurrencyExecutor(this).execute(requestHolder.getDeleteRequest());
                break;
            case CM_EDIT_ID:
                Intent intent = new Intent(this, CurrencyEditActivity.class);
                intent.putExtra(ID, info.id);
                startActivityForResult(intent, EDIT_CURRENCY_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CURRENCY_REQUEST:
                    String name = data.getStringExtra(NAME);
                    String code = data.getStringExtra(CODE);
                    Currency newCurrency = new Currency(code, name);
                    requestHolder.setAddRequest(newCurrency);
                    new CurrencyExecutor(this).execute(requestHolder.getAddRequest());
                    break;
                case EDIT_CURRENCY_REQUEST:
                    Long id = data.getLongExtra(ID, -1);
                    if (id != -1) {
                        name = data.getStringExtra(NAME);
                        code = data.getStringExtra(CODE);
                        Currency editCurrency = new Currency(code, name);
                        editCurrency.setId(id);
                        requestHolder.setEditRequest(editCurrency);
                        new CurrencyExecutor(this).execute(requestHolder.getEditRequest());
                        break;
                    }
                default:
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CurrencyCursorLoader(this);
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
        int id = result.getId();
        switch (id) {
            case CurrencyExecutor.KEY_RESULT_DELETE:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case CurrencyExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            default:
                Log.e("FinancePMError", "Unknown result code: " + id);
        }
    }
}
