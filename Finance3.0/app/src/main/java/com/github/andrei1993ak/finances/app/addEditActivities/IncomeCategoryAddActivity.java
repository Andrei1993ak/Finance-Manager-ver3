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
import com.github.andrei1993ak.finances.control.executors.IncomeCategoryExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;

import java.util.List;

public class IncomeCategoryAddActivity extends BaseActivity implements OnTaskCompleted {

    private EditText newCategoryName;
    private AppCompatSpinner parentCategories;
    private List<IncomeCategory> parentsList;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        setTitle(R.string.newCategory);
        initFields();
        new IncomeCategoryExecutor(this).execute(new RequestAdapter<IncomeCategory>().getAllToList(RequestAdapter.SELECTION_PARENT_CATEGORIES));
    }

    private void initFields() {
        newCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cat_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addCategory();
            }
        });
    }

    @Nullable
    private IncomeCategory checkFields() {
        final IncomeCategory incomeCategory = new IncomeCategory();
        boolean flag = true;
        if (newCategoryName.getText().toString().length() == 0) {
            newCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            incomeCategory.setName(newCategoryName.getText().toString());
        }
        if (spinnerAdapter == null) {
            parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            if (parentCategories.getSelectedItemPosition() == spinnerAdapter.getCount() - 1) {
                incomeCategory.setParentId(-1L);
            } else {
                incomeCategory.setParentId(parentsList.get(parentCategories.getSelectedItemPosition()).getId());
            }
        }
        if (!flag) {
            return null;
        } else {
            return incomeCategory;
        }
    }

    public void addCategory() {
        final IncomeCategory incomeCategory = checkFields();
        if (incomeCategory != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(IncomeCategory.class), incomeCategory);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<IncomeCategory>) result.getObject();
                final String[] names = new String[parentsList.size() + 1];
                int i = 0;
                for (final IncomeCategory incomeCategory : parentsList) {
                    names[i++] = incomeCategory.getName();
                }
                names[i] = "-";
                spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parentCategories.setAdapter(spinnerAdapter);
                parentCategories.setSelection(spinnerAdapter.getCount() - 1);
                break;
        }
    }
}
