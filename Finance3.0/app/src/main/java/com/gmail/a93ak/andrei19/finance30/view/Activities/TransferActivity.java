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
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.IncomeCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.IncomeCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.adapters.TransferCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Income;
import com.gmail.a93ak.andrei19.finance30.modelVer2.pojos.Transfer;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeEditActivity;

public class TransferActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_TRANSFER_REQUEST = 1;
    private static final int EDIT_TRANSFER_REQUEST = 2;

    private TransferCursorAdapter transferCursorAdapter;
    private RequestHolder<Transfer> requestHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imcome_activity);
        requestHolder = new RequestHolder<>();
        transferCursorAdapter = new TransferCursorAdapter(this, null);
        ListView incomeListView = (ListView) findViewById(R.id.incomeListView);
        incomeListView.setAdapter(transferCursorAdapter);
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
                Intent intent = new Intent(this, IncomeEditActivity.class);
                intent.putExtra(DBHelper.INCOME_KEY_ID, info.id);
                startActivityForResult(intent, EDIT_TRANSFER_REQUEST);
                Toast.makeText(this,String.valueOf(info.id),Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_TRANSFER_REQUEST:
                    Income newIncome = new Income();
                    newIncome.setName(data.getStringExtra(DBHelper.INCOME_KEY_NAME));
                    newIncome.setDate(data.getLongExtra(DBHelper.INCOME_KEY_DATE, -1L));
                    newIncome.setAmount(data.getDoubleExtra(DBHelper.INCOME_KEY_AMOUNT, -1.0));
                    newIncome.setPurse_id(data.getLongExtra(DBHelper.INCOME_KEY_PURSE_ID, -1));
                    newIncome.setCategory_id(data.getLongExtra(DBHelper.INCOME_KEY_CATEGORY_ID, -1L));
                    RequestHolder<Income> requestHolder = new RequestHolder<>();
                    requestHolder.setAddRequest(newIncome);
                    new IncomeExecutor(this).execute(requestHolder.getAddRequest());
                    break;
                case EDIT_TRANSFER_REQUEST:
                    Income editIncome = new Income();
                    editIncome.setId(data.getLongExtra(DBHelper.INCOME_KEY_ID, -1L));
                    editIncome.setName(data.getStringExtra(DBHelper.INCOME_KEY_NAME));
                    editIncome.setDate(data.getLongExtra(DBHelper.INCOME_KEY_DATE, -1L));
                    editIncome.setAmount(data.getDoubleExtra(DBHelper.INCOME_KEY_AMOUNT, -1.0));
                    editIncome.setPurse_id(data.getLongExtra(DBHelper.INCOME_KEY_PURSE_ID, -1));
                    editIncome.setCategory_id(data.getLongExtra(DBHelper.INCOME_KEY_CATEGORY_ID, -1L));
                    requestHolder = new RequestHolder<>();
                    requestHolder.setEditRequest(editIncome);
                    new IncomeExecutor(this).execute(requestHolder.getEditRequest());
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
        transferCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        transferCursorAdapter.swapCursor(null);
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
        startActivityForResult(intent, ADD_TRANSFER_REQUEST);
    }
}
