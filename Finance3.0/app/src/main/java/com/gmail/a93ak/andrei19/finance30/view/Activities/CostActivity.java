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
import com.gmail.a93ak.andrei19.finance30.control.Executors.CostExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.CostCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.CostCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;

import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostEditActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeEditActivity;

public class CostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_COST_REQUEST = 1;
    private static final int EDIT_COST_REQUEST = 2;

    private CostCursorAdapter costCursorAdapter;
    private RequestHolder<Cost> requestHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_activity);
        requestHolder = new RequestHolder<>();
        costCursorAdapter = new CostCursorAdapter(this, null);
        ListView costListView = (ListView) findViewById(R.id.costListView);
        costListView.setAdapter(costCursorAdapter);
        registerForContextMenu(costListView);
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
                new CostExecutor(this).execute(requestHolder.getDeleteRequest());
                break;
            case CM_EDIT_ID:
                Intent intent = new Intent(this, CostEditActivity.class);
                intent.putExtra(DBHelper.COST_KEY_ID, info.id);
                startActivityForResult(intent, EDIT_COST_REQUEST);
                Toast.makeText(this,String.valueOf(info.id),Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_COST_REQUEST:
                    Cost newCost = new Cost();
                    newCost.setName(data.getStringExtra(DBHelper.COST_KEY_NAME));
                    newCost.setDate(data.getLongExtra(DBHelper.COST_KEY_DATE, -1L));
                    newCost.setAmount(data.getDoubleExtra(DBHelper.COST_KEY_AMOUNT, -1.0));
                    newCost.setPurse_id(data.getLongExtra(DBHelper.COST_KEY_PURSE_ID, -1));
                    newCost.setCategory_id(data.getLongExtra(DBHelper.COST_KEY_CATEGORY_ID, -1L));
                    newCost.setPhoto(data.getIntExtra(DBHelper.COST_KEY_PHOTO,-2));
                    RequestHolder<Cost> requestHolder = new RequestHolder<>();
                    requestHolder.setAddRequest(newCost);
                    new CostExecutor(this).execute(requestHolder.getAddRequest());
                    break;
                case EDIT_COST_REQUEST:
                    Cost editCost = new Cost();
                    editCost.setId(data.getLongExtra(DBHelper.COST_KEY_ID, -1L));
                    editCost.setName(data.getStringExtra(DBHelper.COST_KEY_NAME));
                    editCost.setDate(data.getLongExtra(DBHelper.COST_KEY_DATE, -1L));
                    editCost.setAmount(data.getDoubleExtra(DBHelper.COST_KEY_AMOUNT, -1.0));
                    editCost.setPurse_id(data.getLongExtra(DBHelper.COST_KEY_PURSE_ID, -1));
                    editCost.setCategory_id(data.getLongExtra(DBHelper.COST_KEY_CATEGORY_ID, -1L));
                    editCost.setPhoto(data.getIntExtra(DBHelper.COST_KEY_PHOTO,-2));
                    requestHolder = new RequestHolder<>();
                    requestHolder.setEditRequest(editCost);
                    new CostExecutor(this).execute(requestHolder.getEditRequest());
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CostCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        costCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        costCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(Result result) {
        int id = result.getId();
        switch (id) {
            case CostExecutor.KEY_RESULT_DELETE:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case CostExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case CostExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            default:
                Log.e("FinancePMError", "Unknown result code: " + id);
        }
    }

    public void addCost(View view) {
        Intent intent = new Intent(this, CostAddActivity.class);
        startActivityForResult(intent, ADD_COST_REQUEST);
    }
}
