package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CostCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.models.CostCategory;

import java.util.List;

public class CostCategoryEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText editCategoryName;
    private AppCompatSpinner parentCategories;
    private RequestHolder<CostCategory> requestHolder;
    private List<CostCategory> parentsList;
    private long id;
    private CostCategory costCategory;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        editCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        ((TextView)findViewById(R.id.title_category)).setText(R.string.editing);
        requestHolder = new RequestHolder<>();
        id = getIntent().getLongExtra(CostCategory.ID, -1);
        new CostCategoryExecutor(this).execute(requestHolder.get(id));
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
                    if(costCategory.getParent_id()==category.getId()) {
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
                if (costCategory.getParent_id() == -1) {
                    parentCategories.setBackground(getResources().getDrawable(R.drawable.shape_gray_field));
                    parentCategories.setEnabled(false);
                } else {
                    new CostCategoryExecutor(this).execute(requestHolder.getAllToList(1));
                }
                break;
            default:
                break;
        }
    }

    public void addEditCategory(final View view) {
        final String name = editCategoryName.getText().toString();
        if (name.length() == 0) {
            editCategoryName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            return;
        } else {
            if (costCategory != null) {
                final Intent intent = new Intent();
                intent.putExtra(CostCategory.ID,id);
                intent.putExtra(CostCategory.NAME, name);
                if (costCategory.getParent_id()==-1){
                    intent.putExtra(CostCategory.PARENT_ID, -1);
                }else {
                    if (parentCategories.getSelectedItemPosition() == (spinnerAdapter.getCount() - 1)) {
                        intent.putExtra(CostCategory.PARENT_ID, -1L);
                    }
                    intent.putExtra(CostCategory.PARENT_ID, parentsList.get(parentCategories.getSelectedItemPosition()).getId());
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
