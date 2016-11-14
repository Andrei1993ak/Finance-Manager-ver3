package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.PieChartItem;
import com.gmail.a93ak.andrei19.finance30.view.reports.BalanceChartActivity;
import com.gmail.a93ak.andrei19.finance30.view.reports.PieChartActivity;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        setTitle(R.string.reports);
    }

    public void selectReport(final View view) {
        switch (view.getId()) {
            case R.id.reportIncomesPie:
                Intent intent = new Intent(this, PieChartActivity.class);
                intent.putExtra(PieChartItem.TYPE,true);
                startActivity(intent);
                break;
            case R.id.reportCostPie:
                intent = new Intent(this, PieChartActivity.class);
                intent.putExtra(PieChartItem.TYPE,false);
                startActivity(intent);
                break;
            case R.id.balanceChart:
                startActivity(new Intent(this, BalanceChartActivity.class));
                break;
        }
    }
}
