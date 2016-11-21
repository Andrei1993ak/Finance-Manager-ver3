package com.github.andrei1993ak.finances.app.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CurrencyExecutor;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;

public class WalletEditActivity extends BaseActivity implements OnTaskCompleted {

    private Wallet wallet;
    private EditText editWalletName;
    private TextView editWalletAmount;
    private TextView editWalletCurrency;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_edit_activity);
        setTitle(R.string.editing);
        initFields();
        final long walletId = getIntent().getLongExtra(Wallet.ID, -1);
        new WalletExecutor(this).execute(new RequestAdapter<Wallet>().get(walletId));
    }

    private void initFields() {
        this.editWalletName = (EditText) findViewById(R.id.edit_wallet_name);
        this.editWalletAmount = (TextView) findViewById(R.id.edit_wallet_amount);
        this.editWalletCurrency = (TextView) findViewById(R.id.edit_wallet_currency);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_pur_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editWallet();
            }
        });
    }

    public void editWallet() {
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
        final Wallet editWallet = new Wallet();
        boolean flag = true;
        if (editWalletName.getText().toString().length() == 0) {
            editWalletName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editWallet.setName(editWalletName.getText().toString());
            editWalletName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        if (wallet != null) {
            editWallet.setId(wallet.getId());
            editWallet.setAmount(wallet.getAmount());
            editWallet.setCurrencyId(wallet.getCurrencyId());
        } else {
            return null;
        }
        if (!flag) {
            return null;
        } else {
            return editWallet;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case WalletExecutor.KEY_RESULT_GET:
                wallet = (Wallet) result.getObject();
                editWalletName.setText(wallet.getName());
                editWalletAmount.setText(String.valueOf(wallet.getAmount()));
                new CurrencyExecutor(this).execute(new RequestAdapter<Currency>().get(wallet.getCurrencyId()));
                break;
            case CurrencyExecutor.KEY_RESULT_GET:
                final Currency currency = (Currency) result.getObject();
                editWalletCurrency.setText(String.valueOf(currency.getName()));
                break;
        }
    }
}
