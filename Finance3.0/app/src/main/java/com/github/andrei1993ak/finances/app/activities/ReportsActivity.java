package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.reportsActivities.BalanceChartActivity;
import com.github.andrei1993ak.finances.app.reportsActivities.PieChartActivity;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;

public class ReportsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        setTitle(R.string.reports);
    }

    public void onSelectReportViewClicked(final View view) {
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
