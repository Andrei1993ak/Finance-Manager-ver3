package com.github.andrei1993ak.finances.app.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.IncomeCategoryExecutor;
import com.github.andrei1993ak.finances.control.executors.IncomeExecutor;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.models.Wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class IncomeEditActivity extends BaseActivity implements OnTaskCompleted {

    private Income income;
    private EditText editIncomeName;
    private EditText editIncomeAmount;
    private TextView editIncomeDate;
    private AppCompatSpinner editIncomeWallet;
    private AppCompatSpinner editIncomeCategory;
    private AppCompatSpinner editIncomeSubCategory;
    private List<Wallet> wallets;
    private List<IncomeCategory> categoriesList;
    private List<IncomeCategory> subCategoriesList;
    private SimpleDateFormat dateFormatter;
    private long parentId;
    private long id;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_add_edit_activity);
        setTitle(R.string.editing);
        id = getIntent().getLongExtra(Income.ID, -1);
        findViewsBuId();
        setDatePickerDialog();
        new IncomeExecutor(this).execute(new RequestHolder<Income>().get(id));
    }

    private void findViewsBuId() {
        editIncomeName = (EditText) findViewById(R.id.income_name);
        editIncomeAmount = (EditText) findViewById(R.id.income_amount);
        editIncomeDate = (TextView) findViewById(R.id.income_date);
        editIncomeWallet = (AppCompatSpinner) findViewById(R.id.income_wallet);
        editIncomeCategory = (AppCompatSpinner) findViewById(R.id.income_category);
        editIncomeSubCategory = (AppCompatSpinner) findViewById(R.id.income_subCategory);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_income_add_edit);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editIncome();
            }
        });
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat(getResources().getString(R.string.dateFormat), Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editIncomeDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        editIncomeDate.setText(dateFormatter.format(newCalendar.getTime()));
        editIncomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void editIncome() {
        final Income income = checkFields();
        if (income != null) {
            income.setId(id);
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
        if (editIncomeName.getText().toString().length() == 0) {
            editIncomeName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editIncomeName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            income.setName(editIncomeName.getText().toString());
        }
        try {
            income.setDate(dateFormatter.parse(editIncomeDate.getText().toString()).getTime());
            final Double amount = Double.parseDouble(editIncomeAmount.getText().toString());
            if (amount > 0) {
                income.setAmount(amount);
                editIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            } else {
                editIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            }
        } catch (final NumberFormatException e) {
            editIncomeAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        income.setWalletId(wallets.get(editIncomeWallet.getSelectedItemPosition()).getId());
        if (editIncomeSubCategory.getVisibility() == View.GONE) {
            income.setCategoryId(categoriesList.get(editIncomeCategory.getSelectedItemPosition()).getId());
        } else {
            income.setCategoryId(subCategoriesList.get(editIncomeSubCategory.getSelectedItemPosition()).getId());
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
            case IncomeExecutor.KEY_RESULT_GET:
                income = (Income) result.getObject();
                editIncomeName.setText(income.getName());
                editIncomeAmount.setText(String.valueOf(income.getAmount()));
                editIncomeDate.setText(dateFormatter.format(income.getDate()));
                final RequestHolder<Wallet> walletsRequestHolder = new RequestHolder<>();
                new WalletExecutor(this).execute(walletsRequestHolder.getAllToList(RequestHolder.SELECTION_ALL));
                final RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
                new IncomeCategoryExecutor(this).execute(categoryRequestHolder.get(income.getCategoryId()));
                break;
            case WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                wallets = (List<Wallet>) result.getObject();
                final String[] walletssNames = new String[wallets.size()];
                int i = 0;
                int position = -1;
                for (final Wallet wallet : wallets) {
                    walletssNames[i++] = wallet.getName();
                    if (wallet.getId() == income.getWalletId()) {
                        position = i - 1;
                    }
                }
                final ArrayAdapter<String> spinnerWalletsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletssNames);
                spinnerWalletsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editIncomeWallet.setAdapter(spinnerWalletsAdapter);
                editIncomeWallet.setSelection(position);
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET:
                final IncomeCategory category = (IncomeCategory) result.getObject();
                parentId = category.getParentId();
                final RequestHolder<IncomeCategory> incomeCategoryRequestHolder = new RequestHolder<>();
                new IncomeCategoryExecutor(this).execute(incomeCategoryRequestHolder.getAllToList(1));
                break;
            case IncomeCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<IncomeCategory>) result.getObject();
                final String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                int categoryPosition = -1;
                for (final IncomeCategory incomeCategory : categoriesList) {
                    categoriesNames[j++] = incomeCategory.getName();
                    if (parentId == -1) {
                        if (incomeCategory.getId() == income.getCategoryId()) {
                            categoryPosition = j - 1;
                        }
                    } else {
                        if (incomeCategory.getId() == parentId) {
                            categoryPosition = j - 1;
                        }
                    }
                }
                final ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editIncomeCategory.setAdapter(spinnerCategoriesAdapter);
                editIncomeCategory.setSelection(categoryPosition);
                editIncomeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                        final RequestHolder<IncomeCategory> categoryRequestHolder = new RequestHolder<>();
                        new IncomeCategoryExecutor(IncomeEditActivity.this).execute(categoryRequestHolder.getAllToListByCategory(categoriesList.get(position).getId()));
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
                    editIncomeSubCategory.setVisibility(View.GONE);
                } else {
                    editIncomeSubCategory.setVisibility(View.VISIBLE);
                    final String[] subCategoriesNames = new String[subCategoriesList.size()];
                    int k = 0;
                    int subCatPosition = 0;
                    for (final IncomeCategory incomeCategory : subCategoriesList) {
                        subCategoriesNames[k++] = incomeCategory.getName();
                        if (incomeCategory.getId() == income.getCategoryId()) {
                            subCatPosition = k - 1;
                        }
                    }
                    final ArrayAdapter<String> spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    editIncomeSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                    editIncomeSubCategory.setSelection(subCatPosition);
                }
                break;
        }
    }
}
