package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.IncomeCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.IncomeCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Income;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeAddActivity;

public class IncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted{

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_INCOME_REQUEST = 1;
    private static final int EDIT_INCOME_REQUEST = 2;

    private IncomeCursorAdapter incomeCursorAdapter;
    private RequestHolder<Income> requestHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imcome_activity);
        requestHolder = new RequestHolder<>();
        incomeCursorAdapter = new IncomeCursorAdapter(this, null);
        ListView incomeListView = (ListView) findViewById(R.id.incomeListView);
        incomeListView.setAdapter(incomeCursorAdapter);
        registerForContextMenu(incomeListView);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                requestHolder.setDeleteRequest(info.id);
                new IncomeExecutor(this).execute(requestHolder.getDeleteRequest());
                break;
            case CM_EDIT_ID:
//                Intent intent = new Intent(this, PurseEditActivity.class);
//                intent.putExtra(DBHelper.PURSES_KEY_ID, info.id);
//                startActivityForResult(intent, EDIT_PURSE_REQUEST);
//                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_INCOME_REQUEST:
//                    Purse newPurse = new Purse();
//                    newPurse.setName(data.getStringExtra(DBHelper.PURSES_KEY_NAME));
//                    newPurse.setAmount(data.getDoubleExtra(DBHelper.PURSES_KEY_AMOUNT, -1.0));
//                    newPurse.setCurrency_id(data.getLongExtra(DBHelper.PURSES_KEY_CURRENCY_ID, -1));
//                    requestHolder.setAddRequest(newPurse);
//                    new PurseExecutor(this).execute(requestHolder.getAddRequest());
                    break;
                case EDIT_INCOME_REQUEST:
//                    Purse editPurse = new Purse();
//                    editPurse.setId(data.getLongExtra(DBHelper.PURSES_KEY_ID, -1));
//                    editPurse.setName(data.getStringExtra(DBHelper.PURSES_KEY_NAME));
//                    editPurse.setAmount(data.getDoubleExtra(DBHelper.PURSES_KEY_AMOUNT, -1.0));
//                    editPurse.setCurrency_id(data.getLongExtra(DBHelper.PURSES_KEY_CURRENCY_ID, -1));
//                    requestHolder.setEditRequest(editPurse);
//                    new PurseExecutor(this).execute(requestHolder.getEditRequest());
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new IncomeCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        incomeCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        incomeCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(Result result) {
        int id = result.getId();
        switch (id) {
            case IncomeExecutor.KEY_RESULT_DELETE:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case IncomeExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case IncomeExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            default:
                Log.e("FinancePMError", "Unknown result code: " + id);
        }
    }

    public void addIncome(View view) {
        Intent intent = new Intent(this, IncomeAddActivity.class);
        startActivityForResult(intent, ADD_INCOME_REQUEST);
//        Income income = new Income();
//        income.setName("Зарплата июнь");
//        income.setDate(1476835200000L);
//        income.setAmount(1000.50);
//        income.setPurse_id(1);
//        income.setCategory_id(6);
//        DBHelperIncome.getInstance(DBHelper.getInstance(this)).add(income);
//        getSupportLoaderManager().getLoader(0).forceLoad();
    }
}
