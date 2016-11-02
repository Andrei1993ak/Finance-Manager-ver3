package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CostExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.CostCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.CostCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;

import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCost;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;
import com.gmail.a93ak.andrei19.finance30.util.bitmapOperations.BitmapOperations;
import com.gmail.a93ak.andrei19.finance30.util.bitmapOperations.SetBitmap;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.Loaders.BitmapLoader;
import com.gmail.a93ak.andrei19.finance30.util.bitmapOperations.UpdateBitmap;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostEditActivity;

import java.io.File;

public class CostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;
    private static final int CM_PHOTO_ID = 3;

    private static final int ADD_COST_REQUEST = 1;
    private static final int EDIT_COST_REQUEST = 2;

    public static final String FTP_PATH = "ftp://adk:1111111@93.125.42.84:21/images/";

    private CostCursorAdapter costCursorAdapter;
    private RequestHolder<Cost> requestHolder;

    private Cost mNewCost;
    private Cost mEditCost;

    public static final String TEMP_PATH = "/storage/emulated/0/temp.jpg";

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
        menu.add(0, CM_PHOTO_ID, 0, R.string.showPhoto);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                requestHolder.setDeleteRequest(info.id);
                new CostExecutor(this).execute(requestHolder.getDeleteRequest());
                BitmapOperations.deleteBitmap(info.id);
                break;
            case CM_EDIT_ID:
                Intent intent = new Intent(this, CostEditActivity.class);
                intent.putExtra(DBHelper.COST_KEY_ID, info.id);
                startActivityForResult(intent, EDIT_COST_REQUEST);
                Toast.makeText(this, String.valueOf(info.id), Toast.LENGTH_LONG).show();
                break;
            case CM_PHOTO_ID:
                Cost cost = DBHelperCost.getInstance(DBHelper.getInstance(this)).get(info.id);
                if (cost.getPhoto() == 1) {
                    String url = FTP_PATH;
                    url += String.valueOf(info.id);
                    url += ".jpg";
                    Dialog builder = new Dialog(this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ImageView imageView = new ImageView(this);
                    BitmapLoader bitmapLoader = BitmapLoader.getInstance(this);
                    bitmapLoader.load(url, imageView);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                } else if (cost.getPhoto() == 0) {
                    Toast.makeText(this, R.string.noPhoto, Toast.LENGTH_LONG).show();
                } else if (cost.getPhoto() == -1) {
                    Toast.makeText(this, "Еще не сделал", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_COST_REQUEST:
                    ProgressBar pb = (ProgressBar) findViewById(R.id.costProgressBar);
                    pb.setVisibility(View.VISIBLE);
                    mNewCost = new Cost();
                    mNewCost.setName(data.getStringExtra(DBHelper.COST_KEY_NAME));
                    mNewCost.setDate(data.getLongExtra(DBHelper.COST_KEY_DATE, -1L));
                    mNewCost.setAmount(data.getDoubleExtra(DBHelper.COST_KEY_AMOUNT, -1.0));
                    mNewCost.setPurse_id(data.getLongExtra(DBHelper.COST_KEY_PURSE_ID, -1));
                    mNewCost.setCategory_id(data.getLongExtra(DBHelper.COST_KEY_CATEGORY_ID, -1L));
                    if (data.getIntExtra(DBHelper.COST_KEY_PHOTO, -2) == 1) {
                        File file = new File(TEMP_PATH);
                        BitmapOperations.uploadBitmap(file, this);
                        break;
                    } else {
                        mNewCost.setPhoto(0);
                        RequestHolder<Cost> requestHolder = new RequestHolder<>();
                        requestHolder.setAddRequest(mNewCost);
                        new CostExecutor(this).execute(requestHolder.getAddRequest());
                        break;
                    }

                case EDIT_COST_REQUEST:
                    ProgressBar pb2 = (ProgressBar) findViewById(R.id.costProgressBar);
                    pb2.setVisibility(View.VISIBLE);
                    mEditCost = new Cost();
                    long id = data.getLongExtra(DBHelper.COST_KEY_ID, -1L);
                    mEditCost.setId(id);
                    mEditCost.setName(data.getStringExtra(DBHelper.COST_KEY_NAME));
                    mEditCost.setDate(data.getLongExtra(DBHelper.COST_KEY_DATE, -1L));
                    mEditCost.setAmount(data.getDoubleExtra(DBHelper.COST_KEY_AMOUNT, -1.0));
                    mEditCost.setPurse_id(data.getLongExtra(DBHelper.COST_KEY_PURSE_ID, -1));
                    mEditCost.setCategory_id(data.getLongExtra(DBHelper.COST_KEY_CATEGORY_ID, -1L));
                    mEditCost.setPhoto(data.getIntExtra(DBHelper.COST_KEY_PHOTO, -2));
                    if (data.getIntExtra(DBHelper.COST_KEY_PHOTO, -2) == 1) {
                        if (data.getBooleanExtra("isChanged", false)) {
                            File file = new File(TEMP_PATH);
                            BitmapOperations.changeBitmap(id, file, this);
                            break;
                        } else {
                            mNewCost.setPhoto(1);
                            RequestHolder<Cost> requestHolder = new RequestHolder<>();
                            requestHolder.setEditRequest(mEditCost);
                            new CostExecutor(this).execute(requestHolder.getEditRequest());
                            break;
                        }
                    } else {
                        mEditCost.setPhoto(0);
                        RequestHolder<Cost> requestHolder = new RequestHolder<>();
                        requestHolder.setEditRequest(mEditCost);
                        new CostExecutor(this).execute(requestHolder.getEditRequest());
                        break;
                    }
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
                ProgressBar pb = (ProgressBar) findViewById(R.id.costProgressBar);
                pb.setVisibility(View.INVISIBLE);
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case CostExecutor.KEY_RESULT_EDIT:
                pb = (ProgressBar) findViewById(R.id.costProgressBar);
                pb.setVisibility(View.INVISIBLE);
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                break;
            case SetBitmap.FTP_ID:
                mNewCost.setPhoto(1);
                RequestHolder<Cost> requestHolder = new RequestHolder<>();
                requestHolder.setAddRequest(mNewCost);
                new CostExecutor(this).execute(requestHolder.getAddRequest());
                break;
            case SetBitmap.ERROR:
                mNewCost.setPhoto(0);
                requestHolder = new RequestHolder<>();
                requestHolder.setAddRequest(mNewCost);
                new CostExecutor(this).execute(requestHolder.getAddRequest());
                break;
            case UpdateBitmap.FTP_ID:
                mEditCost.setPhoto(1);
                requestHolder = new RequestHolder<>();
                requestHolder.setEditRequest(mEditCost);
                new CostExecutor(this).execute(requestHolder.getEditRequest());
                break;
            case UpdateBitmap.ERROR:
                mEditCost.setPhoto(0);
                requestHolder = new RequestHolder<>();
                requestHolder.setEditRequest(mEditCost);
                new CostExecutor(this).execute(requestHolder.getEditRequest());
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
