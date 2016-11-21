package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CostCategoryAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.CostCategoryEditActivity;
import com.github.andrei1993ak.finances.control.adapters.ExpListAdapter;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CostCategoryExecutor;
import com.github.andrei1993ak.finances.control.loaders.CostCategoryCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.util.Constants;

public class CategoryCostActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private ExpandableListView costCategoryExpListView;
    private ExpListAdapter expListAdapter;
    private MenuInflater menuInflater;

    private int deleteGroupId;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        setTitle(R.string.costCategories);
        initFields();
        registerForContextMenu(costCategoryExpListView);
        getSupportLoaderManager().restartLoader(Constants.EXP_LIST_ROOT_LOADER_ID, null, this);
    }

    private void initFields() {
        this.costCategoryExpListView = (ExpandableListView) findViewById(R.id.CategoryExpListView);
        this.deleteGroupId = -1;
        this.menuInflater = getMenuInflater();
        final String[] parentsFrom = {CostCategory.NAME};
        final int[] parentsTo = {android.R.id.text1};
        final String[] childFrom = {CostCategory.NAME};
        final int[] childTo = {android.R.id.text1};
        expListAdapter = new ExpListAdapter(this, null, android.R.layout.simple_expandable_list_item_1, parentsFrom, parentsTo,
                android.R.layout.simple_expandable_list_item_1, childFrom, childTo);
        costCategoryExpListView.setAdapter(expListAdapter);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_cat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addCategory();
            }
        });
    }

    @Override
    protected void onStop() {
        for (int i = 0; i < expListAdapter.getGroupCount(); i++)
            costCategoryExpListView.collapseGroup(i);
        super.onStop();
    }

    public void addCategory() {
        final Intent intent = new Intent(this, CostCategoryAddActivity.class);
        startActivityForResult(intent, Constants.ADD_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        menuInflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        final int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        final int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        switch (item.getItemId()) {
            case R.id.cm_delete:
                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    deleteGroupId = expListAdapter.getIdToPos().get(groupPosition);
                }
                new CostCategoryExecutor(this).execute(new RequestAdapter().delete(info.id));
                return true;
            case R.id.cm_edit:
                final Intent intent = new Intent(this, CostCategoryEditActivity.class);
                intent.putExtra(CostCategory.ID, info.id);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CostCategoryCursorLoader(this, id);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        final int id = loader.getId();
        if (id != Constants.EXP_LIST_ROOT_LOADER_ID) {
            final int groupPos = expListAdapter.getPosToId().get(id);
            expListAdapter.setChildrenCursor(groupPos, data);
        } else {
            expListAdapter.setGroupCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_REQUEST:
                    final CostCategory newCostCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(CostCategory.class));
                    new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().add(newCostCategory));
                    break;
                case Constants.EDIT_REQUEST:
                    final CostCategory editCostCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(CostCategory.class));
                    new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().edit(editCostCategory));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        final Loader<CostCategory> loader = getSupportLoaderManager().getLoader(Constants.EXP_LIST_ROOT_LOADER_ID);
        switch (id) {
            case CostCategoryExecutor.KEY_RESULT_DELETE:
                if (deleteGroupId == -1 && loader != null) {
                    if ((Integer) result.getObject() == DBHelperCategoryCost.hasChildrens) {
                        Toast.makeText(this, R.string.hasChilds, Toast.LENGTH_LONG).show();
                    } else if ((Integer) result.getObject() == DBHelperCategoryCost.usable) {
                        Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                    } else {
                        loader.forceLoad();
                    }
                } else if (loader != null && getSupportLoaderManager().getLoader(deleteGroupId) != null) {
                    if ((Integer) result.getObject() == DBHelperCategoryCost.usable) {
                        Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                    } else {
                        getSupportLoaderManager().getLoader(deleteGroupId).forceLoad();
                        loader.forceLoad();
                        deleteGroupId = -1;
                    }
                }
                break;
            case CostCategoryExecutor.KEY_RESULT_ADD:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case CostCategoryExecutor.KEY_RESULT_EDIT:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            default:
                break;
        }
    }
}
