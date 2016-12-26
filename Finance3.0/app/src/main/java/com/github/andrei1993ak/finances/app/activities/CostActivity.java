package com.github.andrei1993ak.finances.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CostAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CostEditActivity;
import com.github.andrei1993ak.finances.control.adapters.CostCursorAdapter;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CostExecutor;
import com.github.andrei1993ak.finances.control.loaders.CostCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.universalLoader.IUniversalLoader;
import com.github.andrei1993ak.finances.util.universalLoader.ImageNameGenerator;

import java.io.File;
import java.net.MalformedURLException;

public class CostActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, IOnTaskCompleted {

    private CostCursorAdapter costCursorAdapter;
    private RequestAdapter<Cost> requestAdapter;
    private MenuInflater menuInflater;

    private long selectedItemId;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        setTitle(R.string.costs);

        initFields();

        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields() {
        this.selectedItemId = Constants.NOT_SELECTED;
        this.menuInflater = getMenuInflater();
        this.requestAdapter = new RequestAdapter<>();
        this.costCursorAdapter = new CostCursorAdapter(this, null);

        final ListView costListView = (ListView) findViewById(R.id.standardListView);
        costListView.setAdapter(costCursorAdapter);
        costListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                selectedItemId = id;
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(CostActivity.this, CostAddActivity.class);
                startActivityForResult(intent, Constants.ADD_REQUEST);
            }
        });

        registerForContextMenu(costListView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menuInflater.inflate(R.menu.menu_cost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (selectedItemId != Constants.NOT_SELECTED) {
            if (id == R.id.cost_action_edit) {
                final Intent intent = new Intent(this, CostEditActivity.class);
                intent.putExtra(Cost.ID, selectedItemId);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            } else if (id == R.id.cost_action_delete) {
                new CostExecutor(this).execute(requestAdapter.delete(selectedItemId));
                selectedItemId = Constants.NOT_SELECTED;
                return true;
            } else if (id == R.id.cost_action_photo) {
                showPhoto(selectedItemId);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        menuInflater.inflate(R.menu.context_menu, menu);
        menu.add(0, Constants.CM_PHOTO_ID, 0, R.string.showPhoto);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.cm_delete:
                new CostExecutor(this).execute(requestAdapter.delete(info.id));

                break;
            case R.id.cm_edit:
                final Intent intent = new Intent(this, CostEditActivity.class);
                intent.putExtra(Cost.ID, info.id);
                startActivityForResult(intent, Constants.EDIT_REQUEST);

                break;
            case Constants.CM_PHOTO_ID:
                showPhoto(info.id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showPhoto(final long id) {
        final Cost cost = ((DBHelperCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Cost.class)).get(id);
        if (cost.getPhoto() == Constants.COST_HAS_NOT_PHOTO) {
            Toast.makeText(this, R.string.noPhoto, Toast.LENGTH_LONG).show();
        } else if (cost.getPhoto() == Constants.COST_HAS_PHOTO) {
            final String filePath = ImageNameGenerator.getImagePath(id);
            final File file = new File(filePath);
            final Dialog builder = new Dialog(this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final ImageView imageView = new ImageView(this);
            final IUniversalLoader<Bitmap, ImageView> bitmapLoader = ((App)getApplicationContext()).getImageLoader(this);
            try {
                String s = file.toURI().toURL().toString();
                bitmapLoader.load(s, imageView);
            } catch (final MalformedURLException e) {
                Toast.makeText(this, R.string.noPhoto, Toast.LENGTH_LONG).show();
            }
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            builder.show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_REQUEST:
                    final Cost newCost = data.getParcelableExtra(TableQueryGenerator.getTableName(Cost.class));
                    new CostExecutor(this).execute(requestAdapter.add(newCost));
                    break;
                case Constants.EDIT_REQUEST:
                    final Cost editCost = data.getParcelableExtra(TableQueryGenerator.getTableName(Cost.class));
                    new CostExecutor(this).execute(requestAdapter.edit(editCost));
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
            final Loader<Cost> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }

}
