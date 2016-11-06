package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
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
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import java.util.List;

public class PurseAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private AppCompatSpinner spinnerCurrencies;
    private List<Currency> currencies;
    private EditText newPurseName;
    private EditText newPurseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.purse_add_activity);
        spinnerCurrencies = (AppCompatSpinner)findViewById(R.id.spinnerCurrencies);
        newPurseName = (EditText)findViewById(R.id.new_purse_name);
        newPurseAmount = (EditText)findViewById(R.id.new_purse_amount);
        RequestHolder<Currency> requestHolder = new RequestHolder<>();
        new CurrencyExecutor(this).execute(requestHolder.getAllToList(0));
    }

    public void addNewPurse(View view) {
        String name = newPurseName.getText().toString();
        Double amount = -1.0;
        try {
            amount = Double.parseDouble(newPurseAmount.getText().toString());
        } catch (NumberFormatException e){
            newPurseAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
        }
        Long currencyId = currencies.get(spinnerCurrencies.getSelectedItemPosition()).getId();
        if (name.length()==0){
            newPurseName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
        } else if (amount<0){
            newPurseAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
        } else if (currencyId <0){
            return;
        } else {
            Intent intent = new Intent();
            intent.putExtra(Purse.NAME,name);
            intent.putExtra(Purse.AMOUNT, amount);
            intent.putExtra(Purse.CURRENCY_ID, currencyId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()){
            case CurrencyExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                currencies = (List<Currency>) result.getObject();
                String[] names = new String[currencies.size()];
                int i = 0;
                for (Currency currency : currencies) {
                    names[i++] = currency.getName();
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCurrencies.setAdapter(spinnerAdapter);
                break;
        }

    }
}
