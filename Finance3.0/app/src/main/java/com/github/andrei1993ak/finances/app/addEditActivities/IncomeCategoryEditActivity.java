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
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.IncomeCategoryExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;

import java.util.List;

public class IncomeCategoryEditActivity extends BaseActivity implements OnTaskCompleted {

    private EditText editCategoryName;
    private AppCompatSpinner parentCategories;
    private List<IncomeCategory> parentsList;
    private IncomeCategory incomeCategory;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        findViewsById();
        setTitle(R.string.editing);
        final long incomeCategoryId = getIntent().getLongExtra(IncomeCategory.ID, -1);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cat_add);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editCategory();
            }
        });
        new IncomeCategoryExecutor(this).execute(new RequestHolder<IncomeCategory>().get(incomeCategoryId));
    }

    private void findViewsById() {
        editCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
    }

    public void editCategory() {
        final IncomeCategory incomeCategory = checkFields();
        if (incomeCategory != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(IncomeCategory.class), incomeCategory);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private IncomeCategory checkFields() {
        final IncomeCategory editIncomeCategory = new IncomeCategory();
        boolean flag = true;
        editIncomeCategory.setId(incomeCategory.getId());
        if (editCategoryName.getText().toString().length() == 0) {
            editCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            editIncomeCategory.setName(editCategoryName.getText().toString());
        }
        if (incomeCategory.getParentId() == -1) {
            editIncomeCategory.setParentId(-1);
        } else {
            if (parentCategories.getSelectedItemPosition() == (spinnerAdapter.getCount() - 1)) {
                editIncomeCategory.setParentId(-1);
            } else {
                editIncomeCategory.setParentId(parentsList.get(parentCategories.getSelectedItemPosition()).getId());
            }
        }
        if (!flag) {
            return null;
        } else {
            return editIncomeCategory;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<IncomeCategory>) result.getObject();
                final String[] names = new String[parentsList.size() + 1];
                int i = 0;
                int position = 0;
                for (final IncomeCategory category : parentsList) {
                    if (incomeCategory.getParentId() == category.getId()) {
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
            case IncomeCategoryExecutor.KEY_RESULT_GET:
                incomeCategory = (IncomeCategory) result.getObject();
                editCategoryName.setText(incomeCategory.getName());
                if (incomeCategory.getParentId() == -1) {
                    parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray_field));
                    parentCategories.setEnabled(false);
                } else {
                    new IncomeCategoryExecutor(this).execute(new RequestHolder<IncomeCategory>().getAllToList(RequestHolder.SELECTION_PARENT_CATEGORIES));
                }
                break;
            default:
                break;
        }
    }
}
