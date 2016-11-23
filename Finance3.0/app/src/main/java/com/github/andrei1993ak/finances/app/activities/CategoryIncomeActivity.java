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
import com.github.andrei1993ak.finances.app.addEditActivities.IncomeCategoryAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.IncomeCategoryEditActivity;
import com.github.andrei1993ak.finances.control.adapters.ExpListAdapter;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.IncomeCategoryExecutor;
import com.github.andrei1993ak.finances.control.loaders.IncomeCategoryCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.util.Constants;

public class CategoryIncomeActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private ExpandableListView incomeCategoryExpListView;
    private ExpListAdapter adapter;
    private MenuInflater inflater;
    private int deleteGroupId;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        setTitle(R.string.incomeCategories);
        initFields();
        registerForContextMenu(incomeCategoryExpListView);
        getSupportLoaderManager().restartLoader(Constants.EXP_LIST_ROOT_LOADER_ID, null, this);
    }

    private void initFields() {
        this.incomeCategoryExpListView = (ExpandableListView) findViewById(R.id.CategoryExpListView);
        this.inflater = getMenuInflater();
        this.deleteGroupId = -1;
        final String[] parentsFrom = {IncomeCategory.NAME};
        final int[] parentsTo = {android.R.id.text1};
        final String[] childFrom = {IncomeCategory.NAME};
        final int[] childTo = {android.R.id.text1};
        adapter = new ExpListAdapter(this, null, android.R.layout.simple_expandable_list_item_1, parentsFrom, parentsTo,
                android.R.layout.simple_expandable_list_item_1, childFrom, childTo);
        incomeCategoryExpListView.setAdapter(adapter);
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
        for (int i = 0; i < adapter.getGroupCount(); i++)
            incomeCategoryExpListView.collapseGroup(i);
        super.onStop();
    }

    public void addCategory() {
        final Intent intent = new Intent(this, IncomeCategoryAddActivity.class);
        startActivityForResult(intent, Constants.ADD_REQUEST);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        inflater.inflate(R.menu.context_menu, menu);
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
                    deleteGroupId = adapter.getIdToPos().get(groupPosition);
                }
                new IncomeCategoryExecutor(this).execute(new RequestAdapter().delete(info.id));
                return true;
            case R.id.cm_edit:
                final Intent intent = new Intent(this, IncomeCategoryEditActivity.class);
                intent.putExtra(IncomeCategory.ID, info.id);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new IncomeCategoryCursorLoader(this, id);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        final int id = loader.getId();
        if (id != Constants.EXP_LIST_ROOT_LOADER_ID) {
            final int groupPos = adapter.getPosToId().get(id);
            adapter.setChildrenCursor(groupPos, data);
        } else {
            adapter.setGroupCursor(data);
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
                    final IncomeCategory newIncomeCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(IncomeCategory.class));
                    new IncomeCategoryExecutor(this).execute(new RequestAdapter<IncomeCategory>().add(newIncomeCategory));
                    break;
                case Constants.EDIT_REQUEST:
                    final IncomeCategory editIncomeCategory = data.getParcelableExtra(TableQueryGenerator.getTableName(IncomeCategory.class));
                    new IncomeCategoryExecutor(this).execute(new RequestAdapter<IncomeCategory>().edit(editIncomeCategory));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        final Loader<IncomeCategory> loader = getSupportLoaderManager().getLoader(Constants.EXP_LIST_ROOT_LOADER_ID);
        switch (id) {
            case IncomeCategoryExecutor.KEY_RESULT_DELETE:
                if (deleteGroupId == -1 && loader != null) {
                    if ((Integer) result.getObject() == Constants.CATEGORY_HAS_CHILDS) {
                        Toast.makeText(this, R.string.hasChilds, Toast.LENGTH_LONG).show();
                    } else if ((Integer) result.getObject() == Constants.CATEGORY_USABLE) {
                        Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                    } else {
                        loader.forceLoad();
                    }
                } else {
                    final Loader<Object> innerLoader = getSupportLoaderManager().getLoader(deleteGroupId);
                    if (loader != null
                            && innerLoader != null) {
                        if ((Integer) result.getObject() == Constants.CATEGORY_HAS_CHILDS) {
                            Toast.makeText(this, R.string.categoryUsable, Toast.LENGTH_LONG).show();
                        } else {
                            innerLoader.forceLoad();
                            loader.forceLoad();
                            deleteGroupId = -1;
                        }
                    }
                }
                break;
            case IncomeCategoryExecutor.KEY_RESULT_ADD:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case IncomeCategoryExecutor.KEY_RESULT_EDIT:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            default:
                break;
        }
    }
}
