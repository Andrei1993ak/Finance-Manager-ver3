package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CostCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.CostCategoryCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.ExpListAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryCost;
import com.gmail.a93ak.andrei19.finance30.model.models.CostCategory;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostCategoryAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.CostCategoryEditActivity;

public class CategoryCostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_CATEGORY_REQUEST = 1;
    private static final int EDIT_CATEGORY_REQUEST = 2;

    private ExpandableListView costCategoryExpListView;
    private ExpListAdapter adapter;

    private int deleteGroupId = -1;

    public static final int ROOT_LOADER_ID = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        costCategoryExpListView = (ExpandableListView) findViewById(R.id.CategoryExpListView);
        ((TextView) findViewById(R.id.categoriesTitle)).setText(R.string.costCategories);
        final String[] parentsFrom = {CostCategory.NAME};
        final int[] parentsTo = {android.R.id.text1};
        final String[] childFrom = {CostCategory.NAME};
        final int[] childTo = {android.R.id.text1};
        adapter = new ExpListAdapter(this, null, android.R.layout.simple_expandable_list_item_1, parentsFrom, parentsTo,
                android.R.layout.simple_expandable_list_item_1, childFrom, childTo);
        costCategoryExpListView.setAdapter(adapter);
        getSupportLoaderManager().restartLoader(ROOT_LOADER_ID, null, this);
        registerForContextMenu(costCategoryExpListView);
    }

    @Override
    protected void onStop() {
        for (int i = 0; i < adapter.getGroupCount(); i++)
            costCategoryExpListView.collapseGroup(i);
        super.onStop();
    }

    public void addCategory(final View view) {
        final Intent intent = new Intent(this, CostCategoryAddActivity.class);
        startActivityForResult(intent, ADD_CATEGORY_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        final int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        final int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    deleteGroupId = adapter.getIdToPos().get(groupPosition);
                }
                new CostCategoryExecutor(this).execute(new RequestHolder().delete(info.id));
                return true;
            case CM_EDIT_ID:
                final Intent intent = new Intent(this, CostCategoryEditActivity.class);
                intent.putExtra(CostCategory.ID, info.id);
                startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
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
        if (id != ROOT_LOADER_ID) {
            final int groupPos = adapter.getPosToId().get(id);
            adapter.setChildrenCursor(groupPos, data);
        } else {
            adapter.setGroupCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
//        int id = loader.getId();
//        if (id != -1) {
//            int groupPos = adapter.getPosToId().get(id);
//            adapter.setChildrenCursor(groupPos, null);
//        } else {
//            adapter.setGroupCursor(null);
//        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CATEGORY_REQUEST:
                    final CostCategory newCostCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(CostCategory.class));
                    new CostCategoryExecutor(this).execute(new RequestHolder<CostCategory>().add(newCostCategory));
                    break;
                case EDIT_CATEGORY_REQUEST:
                    final CostCategory editCostCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(CostCategory.class));
                    new CostCategoryExecutor(this).execute(new RequestHolder<CostCategory>().edit(editCostCategory));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        switch (id) {
            case CostCategoryExecutor.KEY_RESULT_DELETE:
                if (deleteGroupId == -1 && getSupportLoaderManager().getLoader(ROOT_LOADER_ID) != null) {
                    if ((Integer) result.getObject() == DBHelperCategoryCost.hasChildrens) {
                        Toast.makeText(this, R.string.hasChilds, Toast.LENGTH_LONG).show();
                    } else if ((Integer) result.getObject() == DBHelperCategoryCost.usable) {
                        Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                    } else {
                        getSupportLoaderManager().getLoader(ROOT_LOADER_ID).forceLoad();
                    }
                } else if (getSupportLoaderManager().getLoader(ROOT_LOADER_ID) != null
                        && getSupportLoaderManager().getLoader(deleteGroupId) != null) {
                    if ((Integer) result.getObject() == DBHelperCategoryCost.usable) {
                        Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                    } else {
                        getSupportLoaderManager().getLoader(deleteGroupId).forceLoad();
                        getSupportLoaderManager().getLoader(ROOT_LOADER_ID).forceLoad();
                        deleteGroupId = -1;
                    }
                }
                break;
            case CostCategoryExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(ROOT_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(ROOT_LOADER_ID).forceLoad();
                }
                break;
            case CostCategoryExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(ROOT_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(ROOT_LOADER_ID).forceLoad();
                }
                break;
            default:
                break;
        }
    }
}
