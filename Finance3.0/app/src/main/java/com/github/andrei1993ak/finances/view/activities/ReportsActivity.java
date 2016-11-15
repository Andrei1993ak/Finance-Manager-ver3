package com.github.andrei1993ak.finances.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.view.reports.BalanceChartActivity;
import com.github.andrei1993ak.finances.view.reports.PieChartActivity;

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
