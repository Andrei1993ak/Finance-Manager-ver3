package com.gmail.a93ak.andrei19.finance30.view.Activities;

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

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.IncomeCategoryCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.ExpListAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.models.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeCategoryAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.IncomeCategoryEditActivity;

public class CategoryIncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    private static final int ADD_CATEGORY_REQUEST = 1;
    private static final int EDIT_CATEGORY_REQUEST = 2;

    private ExpandableListView incomeCategoryExpListView;
    private ExpListAdapter adapter;

    private int deleteGroupId = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        incomeCategoryExpListView = (ExpandableListView) findViewById(R.id.CategoryExpListView);
        String[] parentsFrom = {IncomeCategory.NAME};
        int[] parentsTo = {android.R.id.text1};
        String[] childrensFrom = {IncomeCategory.NAME};
        int[] childrensTo = {android.R.id.text1};
        adapter = new ExpListAdapter(this, null, android.R.layout.simple_expandable_list_item_1, parentsFrom, parentsTo,
                android.R.layout.simple_expandable_list_item_1, childrensFrom, childrensTo);
        incomeCategoryExpListView.setAdapter(adapter);
        Loader loader = getSupportLoaderManager().getLoader(-1);
        if (loader != null && !loader.isReset()) {
            getSupportLoaderManager().restartLoader(-1, null, this);
        } else {
            getSupportLoaderManager().initLoader(-1, null, this);
        }
        registerForContextMenu(incomeCategoryExpListView);
    }

    @Override
    protected void onStop() {
        for (int i = 0; i < adapter.getGroupCount(); i++)
            incomeCategoryExpListView.collapseGroup(i);
        super.onStop();
    }

    public void addCategory(View view) {
        Intent intent = new Intent(this, IncomeCategoryAddActivity.class);
        startActivityForResult(intent, ADD_CATEGORY_REQUEST);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        final int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        switch (item.getItemId()) {
            case CM_DELETE_ID:
                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    deleteGroupId = adapter.getIdToPos().get(groupPosition);
                }
                new IncomeCategoryExecutor(this).execute(new RequestHolder().delete(info.id));
                return true;
            case CM_EDIT_ID:
                Intent intent = new Intent(this, IncomeCategoryEditActivity.class);
                intent.putExtra(IncomeCategory.ID, info.id);
                startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new IncomeCategoryCursorLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if (id != -1) {
            int groupPos = adapter.getPosToId().get(id);
            adapter.setChildrenCursor(groupPos, data);
        } else {
            adapter.setGroupCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        int id = loader.getId();
//        if (id != -1) {
//            int groupPos = adapter.getPosToId().get(id);
//            adapter.setChildrenCursor(groupPos, null);
//        } else {
//            adapter.setGroupCursor(null);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CATEGORY_REQUEST:
                    Long categoryParentId = data.getLongExtra(IncomeCategory.PARENT_ID, -2);
                    if (categoryParentId == -2) {
                        return;
                    }
                    IncomeCategory newIncomeCategory = new IncomeCategory();
                    newIncomeCategory.setName(data.getStringExtra(IncomeCategory.NAME));
                    newIncomeCategory.setParent_id(categoryParentId);
                    new IncomeCategoryExecutor(this).execute(new RequestHolder<IncomeCategory>().add(newIncomeCategory));
                    break;
                case EDIT_CATEGORY_REQUEST:
                    Long editCategoryId = data.getLongExtra(IncomeCategory.ID, -1);
                    if (editCategoryId == -1) {
                        return;
                    }
                    Long editCategoryParentId = data.getLongExtra(IncomeCategory.PARENT_ID, -2);
                    if (editCategoryParentId == -2) {
                        return;
                    }
                    IncomeCategory editIncomeCategory = new IncomeCategory();
                    editIncomeCategory.setId(editCategoryId);
                    editIncomeCategory.setName(data.getStringExtra(IncomeCategory.NAME));
                    editIncomeCategory.setParent_id(editCategoryParentId);
                    new IncomeCategoryExecutor(this).execute(new RequestHolder<IncomeCategory>().edit(editIncomeCategory));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTaskCompleted(Result result) {
        int id = result.getId();
        switch (id) {
            case IncomeCategoryExecutor.KEY_RESULT_DELETE:
                if (deleteGroupId == -1 && getSupportLoaderManager().getLoader(-1) != null) {
                    getSupportLoaderManager().getLoader(-1).forceLoad();
                } else if (getSupportLoaderManager().getLoader(-1) != null && getSupportLoaderManager().getLoader(deleteGroupId) != null) {
                    getSupportLoaderManager().getLoader(deleteGroupId).forceLoad();
                    getSupportLoaderManager().getLoader(-1).forceLoad();
                    deleteGroupId = -1;
                }
                break;
            case IncomeCategoryExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(-1) != null) {
                    getSupportLoaderManager().getLoader(-1).forceLoad();
                }
                break;
            case IncomeCategoryExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(-1) != null) {
                    getSupportLoaderManager().getLoader(-1).forceLoad();
                }
                break;
            default:
                break;
        }
    }
}
