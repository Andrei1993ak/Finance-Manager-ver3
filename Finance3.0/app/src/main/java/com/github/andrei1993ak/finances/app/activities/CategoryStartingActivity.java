package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;

public class CategoryStartingActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_starting_activity);
        setTitle(R.string.categories);
    }


    public void onSelectedCategoryViewClick(final View view) {
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
