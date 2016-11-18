package com.github.andrei1993ak.finances.app.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CostCategoryExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.CostCategory;

import java.util.List;

public class CostCategoryEditActivity extends BaseActivity implements OnTaskCompleted {

    private EditText editCategoryName;
    private AppCompatSpinner parentCategories;
    private List<CostCategory> parentsList;
    private CostCategory costCategory;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        setTitle(R.string.editing);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        initFields();
        final long costCategoryId = getIntent().getLongExtra(CostCategory.ID, -1);
        new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().get(costCategoryId));
    }

    private void initFields() {
        editCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cat_add);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editCategory();
            }
        });
    }

    public void editCategory() {
        final CostCategory costCategory = checkFields();
        if (costCategory != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(CostCategory.class), costCategory);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private CostCategory checkFields() {
        final CostCategory editCostCategory = new CostCategory();
        boolean flag = true;
        editCostCategory.setId(costCategory.getId());
        if (editCategoryName.getText().toString().length() == 0) {
            editCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            editCostCategory.setName(editCategoryName.getText().toString());
        }
        if (costCategory.getParentId() == -1) {
            editCostCategory.setParentId(-1);
        } else {
            if (parentCategories.getSelectedItemPosition() == (spinnerAdapter.getCount() - 1)) {
                editCostCategory.setParentId(-1);
            } else {
                editCostCategory.setParentId(parentsList.get(parentCategories.getSelectedItemPosition()).getId());
            }
        }
        if (!flag) {
            return null;
        } else {
            return editCostCategory;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<CostCategory>) result.getObject();
                final String[] names = new String[parentsList.size() + 1];
                int i = 0;
                int position = 0;
                for (final CostCategory category : parentsList) {
                    if (costCategory.getParentId() == category.getId()) {
                        position = i;
                    }
                    names[i++] = category.getName();
                }
                names[i] = "-";
                spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parentCategories.setAdapter(spinnerAdapter);
                parentCategories.setSelection(position);
                break;
            case CostCategoryExecutor.KEY_RESULT_GET:
                costCategory = (CostCategory) result.getObject();
                editCategoryName.setText(costCategory.getName());
                if (costCategory.getParentId() == -1) {
                    parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray_field));
                    parentCategories.setEnabled(false);
                } else {
                    new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().getAllToList(RequestAdapter.SELECTION_PARENT_CATEGORIES));
                }
                break;
            default:
                break;
        }
    }
}
