package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.models.IncomeCategory;


import java.util.List;

public class IncomeCategoryEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText editCategoryName;
    private AppCompatSpinner parentCategories;
    private RequestHolder<IncomeCategory> requestHolder;
    private List<IncomeCategory> parentsList;
    private long id;
    private IncomeCategory incomeCategory;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        editCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        ((TextView) findViewById(R.id.title_category)).setText(R.string.editing);
        requestHolder = new RequestHolder<>();
        id = getIntent().getLongExtra(IncomeCategory.ID, -1);
        new IncomeCategoryExecutor(this).execute(requestHolder.get(id));
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
                    if (incomeCategory.getParent_id() == category.getId()) {
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
                if (incomeCategory.getParent_id() == -1) {
                    parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray_field));
                    parentCategories.setEnabled(false);
                } else {
                    new IncomeCategoryExecutor(this).execute(requestHolder.getAllToList(1));
                }
                break;
            default:
                break;
        }
    }

    public void addEditCategory(final View view) {
        final String name = editCategoryName.getText().toString();
        if (name.length() == 0) {
            editCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            return;
        } else {
            if (incomeCategory != null) {
                final Intent intent = new Intent();
                intent.putExtra(IncomeCategory.ID, id);
                intent.putExtra(IncomeCategory.NAME, name);
                if (incomeCategory.getParent_id() == -1) {
                    intent.putExtra(IncomeCategory.PARENT_ID, -1);
                } else {
                    if (parentCategories.getSelectedItemPosition() == (spinnerAdapter.getCount() - 1)) {
                        intent.putExtra(IncomeCategory.PARENT_ID, -1L);
                    } else {
                        intent.putExtra(IncomeCategory.PARENT_ID, parentsList.get(parentCategories.getSelectedItemPosition()).getId());
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
