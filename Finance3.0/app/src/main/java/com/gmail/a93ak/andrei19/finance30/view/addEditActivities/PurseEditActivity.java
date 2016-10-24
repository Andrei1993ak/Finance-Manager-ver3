package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CurrencyExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

public class PurseEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private Purse purse;
    private EditText editPurseName;
    private TextView editPurseAmount;
    private TextView editPurseCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_edit_activity);
        editPurseName = (EditText)findViewById(R.id.edit_purse_name);
        editPurseAmount = (TextView)findViewById(R.id.edit_purse_amount);
        editPurseCurrency = (TextView)findViewById(R.id.edit_purse_currency);
        RequestHolder<Purse> requestHolder = new RequestHolder<>();
        long id = getIntent().getLongExtra(DBHelper.PURSES_KEY_ID, -1);
        requestHolder.setGetRequest(id);
        new PurseExecutor(this).execute(requestHolder.getGetRequest());
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()){
            case PurseExecutor.KEY_RESULT_GET:
                purse = (Purse)result.getT();
                editPurseName.setText(purse.getName());
                editPurseAmount.setText(String.valueOf(purse.getAmount()));
                RequestHolder<Currency> holder = new RequestHolder<>();
                holder.setGetRequest(purse.getCurrency_id());
                new CurrencyExecutor(this).execute(holder.getGetRequest());
                break;
            case CurrencyExecutor.KEY_RESULT_GET:
                Currency currency = (Currency)result.getT();
                editPurseCurrency.setText(String.valueOf(currency.getName()));
                break;
        }

    }

    public void editPurse(View view) {
        String name = editPurseName.getText().toString();
        if(purse!=null && name.length()>0) {
            Intent intent = new Intent();
            intent.putExtra(DBHelper.PURSES_KEY_ID,purse.getId());
            intent.putExtra(DBHelper.PURSES_KEY_NAME, name);
            intent.putExtra(DBHelper.PURSES_KEY_AMOUNT, purse.getAmount());
            intent.putExtra(DBHelper.PURSES_KEY_CURRENCY_ID, purse.getCurrency_id());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if(!(name.length()>0))
                editPurseName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
        }
    }
}
