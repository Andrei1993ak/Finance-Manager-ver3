package com.github.andrei1993ak.finances.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.executors.IncomeCategoryExecutor;
import com.github.andrei1993ak.finances.control.executors.PurseExecutor;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.models.Purse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private SimpleDateFormat dateFormatter;
    private ArrayAdapter<String> spinnerSubCategoriesAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        setTitle(R.string.newIncome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_add_activity);
        findViewsBuId();
        setDatePickerDialog();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_income_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addNewIncome();
            }
        });
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
        new IncomeCategoryExecutor(this).execute(new RequestHolder<IncomeCategory>().getAllToList(RequestHolder.SELECTION_PARENT_CATEGORIES));
    }

    private void findViewsBuId() {
        newIncomeName = (EditText) findViewById(R.id.edit_income_name);
        newIncomeAmount = (EditText) findViewById(R.id.edit_income_amount);
        newIncomeDate = (TextView) findViewById(R.id.edit_income_date);
        newIncomePurse = (AppCompatSpinner) findViewById(R.id.edit_income_purse);
        newIncomeCategory = (AppCompatSpinner) findViewById(R.id.edit_income_category);
        newIncomeSubCategory = (AppCompatSpinner) findViewById(R.id.edit_income_subCategory);
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat(getResources().getString(R.string.dateFormat), Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newIncomeDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        newIncomeDate.setText(dateFormatter.format(newCalendar.getTime()));
        newIncomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    public void addNewIncome() {
        final Income income = checkFields();
        if (income != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Income.class), income);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private Income checkFields() {
        final Income income = new Income();
        boolean flag = true;
        if (newIncomeName.getText().toString().length() == 0) {
            newIncomeName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newIncomeName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            income.setName(newIncomeName.getText().toString());
        }
        try {
            income.setDate(dateFormatter.parse(newIncomeDate.getText().toString()).getTime());
            final Double amount = Double.parseDouble(newIncomeAmount.getText().toString());
            if (amount > 0) {
                income.setAmount(amount);
                newIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            } else {
                newIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            }
        } catch (final NumberFormatException e) {
            newIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        income.setPurseId(pursesList.get(newIncomePurse.getSelectedItemPosition()).getId());
        if (newIncomeSubCategory.getVisibility() == View.GONE) {
            income.setCategoryId(categoriesList.get(newIncomeCategory.getSelectedItemPosition()).getId());
        } else {
            if (newIncomeSubCategory.getSelectedItemPosition() != spinnerSubCategoriesAdapter.getCount() - 1) {
                income.setCategoryId(subCategoriesList.get(newIncomeSubCategory.getSelectedItemPosition()).getId());
            } else {
                income.setCategoryId(categoriesList.get(newIncomeCategory.getSelectedItemPosition()).getId());
            }
        }
        if (!flag) {
            return null;
        } else {
            return income;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>) result.getObject();
                final String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                for (final Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                }
                final ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomePurse.setAdapter(spinnerPursesAdapter);
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<IncomeCategory>) result.getObject();
                final String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                for (final IncomeCategory incomeCategory : categoriesList) {
                    categoriesNames[j++] = incomeCategory.getName();
                }
                final ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIncomeCategory.setAdapter(spinnerCategoriesAdapter);
                newIncomeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                        final RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
                        new IncomeCategoryExecutor(IncomeAddActivity.this).execute(categoryRequestHolder.getAllToListByCategory(categoriesList.get(position).getId()));
                    }

                    @Override
                    public void onNothingSelected(final AdapterView<?> parent) {

                    }
                });
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID:
                subCategoriesList = (List<IncomeCategory>) result.getObject();
                final int length = subCategoriesList.size();
                if (length == 0) {
                    newIncomeSubCategory.setVisibility(View.GONE);
                } else {
                    newIncomeSubCategory.setVisibility(View.VISIBLE);
                    final String[] subCategoriesNames = new String[subCategoriesList.size() + 1];
                    int k = 0;
                    for (final IncomeCategory incomeCategory : subCategoriesList) {
                        subCategoriesNames[k++] = incomeCategory.getName();
                    }
                    subCategoriesNames[k] = "-";
                    spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    newIncomeSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                }
                break;
        }

    }
}
