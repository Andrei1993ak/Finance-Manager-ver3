package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;

public class CategoryStartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_starting_activity);
    }


    public void selectCategory(View view) {
        switch (view.getId()) {
            case R.id.tvIncome:
                startActivity(new Intent(this, CategoryIncomeActivity.class));
                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, CategryCostActivity.class));
                break;
        }
    }
}
