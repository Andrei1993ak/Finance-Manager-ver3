package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.pojos.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

import java.util.List;

public class IncomeAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText newIncomeName;
    private EditText newIncomeAmount;
    private TextView newIncomeDate;
    private AppCompatSpinner newIncomePurse;
    private AppCompatSpinner newIncomeCategory;
    private AppCompatSpinner newIncomeSubCategory;
    private List<Purse> pursesList;
    private List<IncomeCategory> categoriesList;
    private List<IncomeCategory> subCategoriesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoome_add_activity);
        findViewsBuId();
        RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
        purseRequestHolder.setGetAllToListRequest(0);
        new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
        RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
        categoryRequestHolder.setGetAllToListRequest(1);
        new IncomeCategoryExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()){
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>)result.getT();
                String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                for (Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                }
                ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomePurse.setAdapter(spinnerPursesAdapter);
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<IncomeCategory>)result.getT();
                String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                for (IncomeCategory incomeCategory : categoriesList) {
                    categoriesNames[j++] = incomeCategory.getName();
                }
                ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomeCategory.setAdapter(spinnerCategoriesAdapter);
                break;
        }

    }

    private void findViewsBuId() {
        newIncomeName = (EditText) findViewById(R.id.new_income_name);
        newIncomeAmount = (EditText) findViewById(R.id.new_income_amount);
        newIncomeDate = (TextView) findViewById(R.id.new_income_date);
        newIncomePurse = (AppCompatSpinner) findViewById(R.id.new_income_purse);
        newIncomeCategory = (AppCompatSpinner) findViewById(R.id.new_income_category);
        newIncomeSubCategory = (AppCompatSpinner) findViewById(R.id.new_income_subCategory);
    }
}
