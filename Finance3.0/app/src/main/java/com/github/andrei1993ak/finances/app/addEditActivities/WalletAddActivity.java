package com.github.andrei1993ak.finances.app.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;

import java.util.List;

public class WalletAddActivity extends BaseActivity implements IOnTaskCompleted {

    private AppCompatSpinner spinnerCurrencies;
    private List<Currency> currencies;
    private EditText newWalletName;
    private EditText newWalletAmount;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_add_activity);
        setTitle(R.string.newWallet);

        initFields();

        new CurrencyExecutor(this).execute(new RequestAdapter<Currency>().getAllToList(RequestAdapter.SELECTION_ALL));
    }

    private void initFields() {
        this.spinnerCurrencies = (AppCompatSpinner) findViewById(R.id.spinnerCurrencies);
        this.newWalletName = (EditText) findViewById(R.id.new_wallet_name);
        this.newWalletAmount = (EditText) findViewById(R.id.new_wallet_amount);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_pur_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addNewWallet();
            }
        });
    }

    public void addNewWallet() {
        final Wallet wallet = checkFields();
        if (wallet != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Wallet.class), wallet);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private Wallet checkFields() {
        final Wallet wallet = new Wallet();
        boolean flag = true;
        try {
            final double amount = Double.parseDouble(newWalletAmount.getText().toString());
            if (amount < 0) {
                newWalletAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                wallet.setAmount(amount);
                newWalletAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            }
        } catch (final NumberFormatException e) {
            newWalletAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        }
        if (newWalletName.getText().toString().length() == 0) {
            newWalletName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            wallet.setName(newWalletName.getText().toString());
            newWalletName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        wallet.setCurrencyId(currencies.get(spinnerCurrencies.getSelectedItemPosition()).getId());
        if (!flag) {
            return null;
        } else {
            return wallet;
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
