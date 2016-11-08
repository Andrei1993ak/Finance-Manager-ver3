package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.view.reports.ReportIncomePieActivity;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
    }

    public void selectReport(final View view) {
        switch (view.getId()) {
            case R.id.reportIncomesPie:
                startActivity(new Intent(this, ReportIncomePieActivity.class));
                break;
            case R.id.reportCostPie:
                startActivity(new Intent(this, ReportIncomePieActivity.class));
                break;
            case R.id.report3:
                startActivity(new Intent(this, ReportIncomePieActivity.class));
                break;
        }
    }
}
