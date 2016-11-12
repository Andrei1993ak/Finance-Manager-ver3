package com.gmail.a93ak.andrei19.finance30.view.activities;

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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CostExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.CostCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.CostCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;

import com.gmail.a93ak.andrei19.finance30.model.models.Cost;
import com.gmail.a93ak.andrei19.finance30.util.universalLoader.loaders.BitmapLoader;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostEditActivity;

import java.io.File;
import java.net.MalformedURLException;

public class CostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;
    private static final int CM_PHOTO_ID = 3;

    private static final int ADD_COST_REQUEST = 1;
    private static final int EDIT_COST_REQUEST = 2;

    private static final int MAIN_LOADER_ID = 0;

    private CostCursorAdapter costCursorAdapter;
    private RequestHolder<Cost> requestHolder;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_activity);
        requestHolder = new RequestHolder<>();
        costCursorAdapter = new CostCursorAdapter(this, null);
        final ListView costListView = (ListView) findViewById(R.id.costListView);
        costListView.setAdapter(costCursorAdapter);
        registerForContextMenu(costListView);
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }

    public void addCost(final View view) {
        final Intent intent = new Intent(this, CostAddActivity.class);
        startActivityForResult(intent, ADD_COST_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
        menu.add(0, CM_PHOTO_ID, 0, R.string.showPhoto);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                new CostExecutor(this).execute(requestHolder.delete(info.id));
                break;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, CostEditActivity.class);
                intent.putExtra(Cost.ID, info.id);
                startActivityForResult(intent, EDIT_COST_REQUEST);
                break;
            case CM_PHOTO_ID:
                final Cost cost = com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCost.getInstance().get(info.id);
                if (cost.getPhoto() == 0) {
                    Toast.makeText(this, R.string.noPhoto, Toast.LENGTH_LONG).show();
                } else if (cost.getPhoto() == 1) {
                    final String filePath = App.getImagePath(info.id);
                    final File file = new File(filePath);
                    final Dialog builder = new Dialog(this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final ImageView imageView = new ImageView(this);
                    final BitmapLoader bitmapLoader = BitmapLoader.getInstance(this);
                    try {
                        bitmapLoader.load(file.toURI().toURL().toString(), imageView);
                    } catch (final MalformedURLException e) {
                        e.printStackTrace();
                    }
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_COST_REQUEST:
                    final Cost newCost = data.getParcelableExtra(TableQueryGenerator.getTableName(Cost.class));
                    new CostExecutor(this).execute(requestHolder.add(newCost));
                    break;
                case EDIT_COST_REQUEST:
                    final Cost editCost = data.getParcelableExtra(TableQueryGenerator.getTableName(Cost.class));
                    new CostExecutor(this).execute(requestHolder.edit(editCost));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CostCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        costCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        costCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        if (id == CostExecutor.KEY_RESULT_DELETE || id == CostExecutor.KEY_RESULT_ADD || id == CostExecutor.KEY_RESULT_EDIT) {
            if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
            }
        }
    }

}
