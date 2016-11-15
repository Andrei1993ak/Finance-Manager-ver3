package com.github.andrei1993ak.finances.view.addEditActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;

public class CurrencyEditActivity extends AppCompatActivity implements OnTaskCompleted {


    private TextView editCurrencyCode;
    private EditText editCurrencyName;
    private Currency currency;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_edit_activity);
        setTitle(R.string.editing);
        editCurrencyCode = (TextView) findViewById(R.id.edit_currency_code);
        editCurrencyName = (EditText) findViewById(R.id.edit_currency_name);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cur_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Currency currency = checkFields();
                if (currency != null) {
                    final Intent intent = new Intent();
                    intent.putExtra(TableQueryGenerator.getTableName(Currency.class), currency);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        final RequestHolder<Currency> requestHolder = new RequestHolder<>();
        final long id = getIntent().getLongExtra(Currency.ID, -1);
        new CurrencyExecutor(this).execute(requestHolder.get(id));
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
