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

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import java.util.List;

public class PurseAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private AppCompatSpinner spinnerCurrencies;
    private List<Currency> currencies;
    private EditText newPurseName;
    private EditText newPurseAmount;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_add_activity);
        findViewsById();
        new CurrencyExecutor(this).execute(new RequestHolder<Currency>().getAllToList(RequestHolder.SELECTION_ALL));
    }

    private void findViewsById() {
        spinnerCurrencies = (AppCompatSpinner) findViewById(R.id.spinnerCurrencies);
        newPurseName = (EditText) findViewById(R.id.new_purse_name);
        newPurseAmount = (EditText) findViewById(R.id.new_purse_amount);
    }

    public void addNewPurse(final View view) {
        final Purse purse = checkFields();
        if (purse != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Purse.class), purse);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private Purse checkFields() {
        final Purse purse = new Purse();
        boolean flag = true;
        try {
            final double amount = Double.parseDouble(newPurseAmount.getText().toString());
            if (amount < 0) {
                newPurseAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                purse.setAmount(amount);
                newPurseAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            }
        } catch (final NumberFormatException e) {
            newPurseAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        }
        if (newPurseName.getText().toString().length() == 0) {
            newPurseName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            purse.setName(newPurseName.getText().toString());
            newPurseName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        purse.setCurrencyId(currencies.get(spinnerCurrencies.getSelectedItemPosition()).getId());
        if (!flag) {
            return null;
        } else {
            return purse;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case CurrencyExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                currencies = (List<Currency>) result.getObject();
                final String[] names = new String[currencies.size()];
                int i = 0;
                for (final Currency currency : currencies) {
                    names[i++] = currency.getName();
                }
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCurrencies.setAdapter(spinnerAdapter);
                break;
        }

    }
}
