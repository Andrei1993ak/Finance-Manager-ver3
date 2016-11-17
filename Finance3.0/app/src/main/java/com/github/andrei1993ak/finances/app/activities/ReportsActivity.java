package com.github.andrei1993ak.finances.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.app.reports.BalanceChartActivity;
import com.github.andrei1993ak.finances.app.reports.PieChartActivity;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
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
