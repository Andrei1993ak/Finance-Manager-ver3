package com.github.andrei1993ak.finances.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.util.Constants;

public class CategoryStartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            setTheme(R.style.Dark);
        }
        setTitle(R.string.categories);
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
