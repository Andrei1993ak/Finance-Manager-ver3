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
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.IncomeCategory;

import java.util.List;

public class IncomeCategoryEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText editCategoryName;
    private AppCompatSpinner parentCategories;
    private RequestHolder<IncomeCategory> requestHolder;
    private List<IncomeCategory> parentsList;
    private long id;
    private IncomeCategory incomeCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_category_add_edit_activity);
        editCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        ((TextView)findViewById(R.id.title_income_category)).setText(R.string.editing);
        requestHolder = new RequestHolder<>();
        id = getIntent().getLongExtra(DBHelper.INCOME_CATEGORY_KEY_ID, -1);
        requestHolder.setGetRequest(id);
        new IncomeCategoryExecutor(this).execute(requestHolder.getGetRequest());
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<IncomeCategory>) result.getT();
                String[] names = new String[parentsList.size() + 1];
                int i = 0;
                int position = 0;
                for (IncomeCategory category : parentsList) {
                    if(incomeCategory.getParent_id()==category.getId()) {
                        position = i;
                    }
                    names[i++] = category.getName();
                }
                names[i] = "-";
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parentCategories.setAdapter(spinnerAdapter);
                parentCategories.setSelection(position);
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET:
                incomeCategory = (IncomeCategory) result.getT();
                editCategoryName.setText(incomeCategory.getName());
                if (incomeCategory.getParent_id() == -1) {
                    parentCategories.setBackground(getResources().getDrawable(R.drawable.shape_gray_field));
                    parentCategories.setEnabled(false);
                } else {
                    requestHolder.setGetAllToListRequest(1);
                    new IncomeCategoryExecutor(this).execute(requestHolder.getGetAllToListRequest());
                }
                break;
            default:
                break;
        }
    }

    public void addEditCategory(View view) {
        String name = editCategoryName.getText().toString();
        if (name.length() == 0) {
            editCategoryName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            return;
        } else {
            if (incomeCategory != null) {
                Intent intent = new Intent();
                intent.putExtra(DBHelper.INCOME_CATEGORY_KEY_ID,id);
                intent.putExtra(DBHelper.INCOME_CATEGORY_KEY_NAME, name);
                if (incomeCategory.getParent_id()==-1){
                    intent.putExtra(DBHelper.INCOME_CATEGORY_KEY_PARENT_ID, -1);
                }else {
                    intent.putExtra(DBHelper.INCOME_CATEGORY_KEY_PARENT_ID, parentsList.get(parentCategories.getSelectedItemPosition()).getId());
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
