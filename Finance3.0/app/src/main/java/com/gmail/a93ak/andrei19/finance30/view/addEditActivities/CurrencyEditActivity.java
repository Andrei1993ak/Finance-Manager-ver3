package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;

public class CurrencyEditActivity extends AppCompatActivity implements OnTaskCompleted {


    private TextView editCurrencyCode;
    private EditText editCurrencyName;
    private Currency currency;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_edit_activity);
        editCurrencyCode = (TextView) findViewById(R.id.edit_currency_code);
        editCurrencyName = (EditText) findViewById(R.id.edit_currency_name);
        final RequestHolder<Currency> requestHolder = new RequestHolder<>();
        final long id = getIntent().getLongExtra(Currency.ID, -1);
        new CurrencyExecutor(this).execute(requestHolder.get(id));
    }

    public void editCurrency(final View view) {
        final Currency currency = checkFields();
        if (currency != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Currency.class), currency);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private Currency checkFields() {
        final Currency editCurrency = new Currency();
        boolean flag = true;
        if (editCurrencyName.getText().toString().length() == 0) {
            editCurrencyName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editCurrencyName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            editCurrency.setName(editCurrencyName.getText().toString());
        }
        if (currency != null) {
            editCurrency.setId(currency.getId());
            editCurrency.setCode(currency.getCode());
        } else {
            return null;
        }
        if (!flag) {
            return null;
        } else {
            return editCurrency;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case CurrencyExecutor.KEY_RESULT_GET:
                currency = (Currency) result.getObject();
                editCurrencyCode.setText(currency.getCode());
                editCurrencyName.setText(currency.getName());
                break;
        }
    }
}
