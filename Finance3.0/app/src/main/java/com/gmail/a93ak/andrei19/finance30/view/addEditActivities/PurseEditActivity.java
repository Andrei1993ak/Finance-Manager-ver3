package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

public class PurseEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private Purse purse;
    private EditText editPurseName;
    private TextView editPurseAmount;
    private TextView editPurseCurrency;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_edit_activity);
        findViewsById();
        final long purseId = getIntent().getLongExtra(Purse.ID, -1);
        new PurseExecutor(this).execute(new RequestHolder<Purse>().get(purseId));
    }

    private void findViewsById() {
        editPurseName = (EditText) findViewById(R.id.edit_purse_name);
        editPurseAmount = (TextView) findViewById(R.id.edit_purse_amount);
        editPurseCurrency = (TextView) findViewById(R.id.edit_purse_currency);
    }

    public void editPurse(final View view) {
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
        final Purse editPurse = new Purse();
        boolean flag = true;
        if (editPurseName.getText().toString().length() == 0) {
            editPurseName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editPurse.setName(editPurseName.getText().toString());
            editPurseName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        if (purse != null) {
            editPurse.setId(purse.getId());
            editPurse.setAmount(purse.getAmount());
            editPurse.setCurrencyId(purse.getCurrencyId());
        } else {
            return null;
        }
        if (!flag) {
            return null;
        } else {
            return editPurse;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET:
                purse = (Purse) result.getObject();
                editPurseName.setText(purse.getName());
                editPurseAmount.setText(String.valueOf(purse.getAmount()));
                new CurrencyExecutor(this).execute(new RequestHolder<Currency>().get(purse.getCurrencyId()));
                break;
            case CurrencyExecutor.KEY_RESULT_GET:
                final Currency currency = (Currency) result.getObject();
                editPurseCurrency.setText(String.valueOf(currency.getName()));
                break;
        }
    }
}
