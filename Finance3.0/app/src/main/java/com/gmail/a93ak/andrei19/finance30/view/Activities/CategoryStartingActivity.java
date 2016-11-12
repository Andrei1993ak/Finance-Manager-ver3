package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;

public class CategoryStartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_starting_activity);
    }


    public void selectCategory(final View view) {
        switch (view.getId()) {
            case R.id.tvIncome:
                startActivity(new Intent(this, CategoryIncomeActivity.class));
                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, CategoryCostActivity.class));
                break;
        }
    }
}
