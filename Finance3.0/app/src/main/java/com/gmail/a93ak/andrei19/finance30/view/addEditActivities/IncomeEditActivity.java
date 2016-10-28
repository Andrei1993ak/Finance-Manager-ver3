package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.IncomeExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Income;
import com.gmail.a93ak.andrei19.finance30.model.pojos.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class IncomeEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private Income income;
    private EditText newIncomeName;
    private EditText newIncomeAmount;
    private TextView newIncomeDate;
    private AppCompatSpinner newIncomePurse;
    private AppCompatSpinner newIncomeCategory;
    private AppCompatSpinner newIncomeSubCategory;
    private List<Purse> pursesList;
    private List<IncomeCategory> categoriesList;
    private List<IncomeCategory> subCategoriesList;
    private SimpleDateFormat dateFormatter;
    private long parentId;
    private long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoome_edit_activity);

        findViewsBuId();
        RequestHolder<Income> requestHolder = new RequestHolder<>();
        id = getIntent().getLongExtra(DBHelper.INCOME_KEY_ID, -1);
        requestHolder.setGetRequest(id);
        new IncomeExecutor(this).execute(requestHolder.getGetRequest());
        setDatePicekDialog();
    }

    private void setDatePicekDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newIncomeDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        newIncomeDate.setText(dateFormatter.format(newCalendar.getTime()));
        newIncomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {

            case IncomeExecutor.KEY_RESULT_GET:
                income = (Income) result.getT();
                newIncomeName.setText(income.getName());
                newIncomeAmount.setText(String.valueOf(income.getAmount()));
                newIncomeDate.setText(dateFormatter.format(income.getDate()));
                RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
                purseRequestHolder.setGetAllToListRequest(0);
                new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
                RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
                categoryRequestHolder.setGetRequest(income.getCategory_id());
                new IncomeCategoryExecutor(this).execute(categoryRequestHolder.getGetRequest());
                break;

            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>) result.getT();
                String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                int position = -1;
                for (Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                    if (purse.getId() == income.getPurse_id()) {
                        position = i - 1;
                    }
                }
                ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomePurse.setAdapter(spinnerPursesAdapter);
                newIncomePurse.setSelection(position);
                break;

            case IncomeCategoryExecutor.KEY_RESULT_GET:
                IncomeCategory category = (IncomeCategory) result.getT();
                parentId = category.getParent_id();
                RequestHolder<IncomeCategory> incomeCategoryRequestHolder = new RequestHolder<>();
                incomeCategoryRequestHolder.setGetAllToListRequest(1);
                new IncomeCategoryExecutor(this).execute(incomeCategoryRequestHolder.getGetAllToListRequest());
                break;

            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<IncomeCategory>) result.getT();
                String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                int categoryPosition = -1;
                for (IncomeCategory incomeCategory : categoriesList) {
                    categoriesNames[j++] = incomeCategory.getName();
                    if (parentId == -1) {
                        if (incomeCategory.getId() == income.getCategory_id()) {
                            categoryPosition = j - 1;
                        }
                    } else {
                        if (incomeCategory.getId() == parentId) {
                            categoryPosition = j - 1;
                        }
                    }
                }
                ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomeCategory.setAdapter(spinnerCategoriesAdapter);
                newIncomeCategory.setSelection(categoryPosition);
                newIncomeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
                        categoryRequestHolder.setGetAllToListByCategoryId(categoriesList.get(position).getId());
                        new IncomeCategoryExecutor(IncomeEditActivity.this).execute(categoryRequestHolder.getGetAllToListByCategoryId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;


            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID:
                subCategoriesList = (List<IncomeCategory>) result.getT();
                int length = subCategoriesList.size();
                if (length == 0) {
                    newIncomeSubCategory.setVisibility(View.GONE);
                } else {
                    newIncomeSubCategory.setVisibility(View.VISIBLE);
                    String[] subCategoriesNames = new String[subCategoriesList.size()];
                    int k = 0;
                    int subCatPosition = 0;
                    for (IncomeCategory incomeCategory : subCategoriesList) {
                        subCategoriesNames[k++] = incomeCategory.getName();
                        if (incomeCategory.getId() == income.getCategory_id()) {
                            subCatPosition = k - 1;
                        }
                    }
                    ArrayAdapter<String> spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    newIncomeSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                    newIncomeSubCategory.setSelection(subCatPosition);
                }
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

    public void editIncome(View view) {
        if (checkFields()) {
            try {
                Intent intent = new Intent();
                intent.putExtra(DBHelper.INCOME_KEY_ID, id);
                intent.putExtra(DBHelper.INCOME_KEY_NAME, newIncomeName.getText().toString());
                intent.putExtra(DBHelper.INCOME_KEY_AMOUNT, Double.parseDouble(newIncomeAmount.getText().toString()));
                intent.putExtra(DBHelper.INCOME_KEY_DATE, dateFormatter.parse(newIncomeDate.getText().toString()).getTime());
                intent.putExtra(DBHelper.INCOME_KEY_PURSE_ID, pursesList.get(newIncomePurse.getSelectedItemPosition()).getId());
                if (newIncomeSubCategory.getVisibility() == View.GONE) {
                    intent.putExtra(DBHelper.INCOME_KEY_CATEGORY_ID, categoriesList.get(newIncomeCategory.getSelectedItemPosition()).getId());
                } else {
                    intent.putExtra(DBHelper.INCOME_KEY_CATEGORY_ID, subCategoriesList.get(newIncomeSubCategory.getSelectedItemPosition()).getId());
                }
                setResult(RESULT_OK, intent);
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private boolean checkFields() {
        boolean flag = true;
        if (newIncomeName.getText().toString().length() == 0) {
            newIncomeName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newIncomeName.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        if (newIncomeAmount.getText().length() == 0) {
            newIncomeAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newIncomeAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        return flag;
    }
}
