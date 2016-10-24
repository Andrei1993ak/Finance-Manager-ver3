package com.gmail.a93ak.andrei19.finance30.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CategoryActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CurrencyActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.PurseActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectCategory(View view) {
        switch (view.getId()) {
            case R.id.tvCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
            case R.id.tvPurse:
                startActivity(new Intent(this, PurseActivity.class));
                break;
            case R.id.tvCategories:
                startActivity(new Intent(this, CategoryActivity.class));
                break;
        }
    }
}
