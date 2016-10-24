package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;

public class CurrencyEditActivity extends AppCompatActivity implements OnTaskCompleted {

    public static final String ID = "Id";
    public static final String NAME = "name";
    public static final String CODE = "code";

    private TextView editCurrencyCode;
    private EditText editCurrencyName;
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_edit_activity);
        editCurrencyCode = (TextView) findViewById(R.id.edit_currency_code);
        editCurrencyName = (EditText) findViewById(R.id.edit_currency_name);
        RequestHolder<Currency> requestHolder = new RequestHolder<>();
        long id = getIntent().getLongExtra(ID, -1);
        if (id != -1) {
            requestHolder.setGetRequest(id);
            new CurrencyExecutor(this).execute(requestHolder.getGetRequest());
        }

    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case CurrencyExecutor.KEY_RESULT_GET:
                currency = (Currency) result.getT();
                editCurrencyCode.setText(currency.getCode());
                editCurrencyName.setText(currency.getName());
                break;
        }
    }

    public void editCurrency(View view) {
        String name = editCurrencyName.getText().toString();
        if(currency!=null && name.length()>0) {
            Intent intent = new Intent();
            intent.putExtra(ID,currency.getId());
            intent.putExtra(NAME, name);
            intent.putExtra(CODE, currency.getCode());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if(!(name.length()>0))
                editCurrencyName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
        }

    }
}
