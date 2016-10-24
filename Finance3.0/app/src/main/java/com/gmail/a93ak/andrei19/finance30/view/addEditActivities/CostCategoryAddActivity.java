package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CostCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CostCategory;

import java.util.List;

public class CostCategoryAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText newCategoryName;
    private AppCompatSpinner parentCategories;
    private List<CostCategory> parentsList;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        newCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);
        ((Button)findViewById(R.id.button_add_edit_category)).setText(R.string.add_button_text);
        RequestHolder<CostCategory> requestHolder = new RequestHolder<>();
        requestHolder.setGetAllToListRequest(1);
        new CostCategoryExecutor(this).execute(requestHolder.getGetAllToListRequest());
    }

    public void addEditCategory(View view) {
        String name = newCategoryName.getText().toString();
        if (name.length() == 0) {
            newCategoryName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            return;
        } else {
            if (spinnerAdapter != null) {
                Intent intent = new Intent();
                intent.putExtra(DBHelper.COST_CATEGORY_KEY_NAME, name);
                if (parentCategories.getSelectedItemPosition() == spinnerAdapter.getCount() - 1) {
                    intent.putExtra(DBHelper.COST_CATEGORY_KEY_PARENT_ID, -1L);
                } else {
                    intent.putExtra(DBHelper.COST_CATEGORY_KEY_PARENT_ID,parentsList.get(parentCategories.getSelectedItemPosition()).getId());
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<CostCategory>) result.getT();
                String[] names = new String[parentsList.size() + 1];
                int i = 0;
                for (CostCategory costCategory : parentsList) {
                    names[i++] = costCategory.getName();
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
