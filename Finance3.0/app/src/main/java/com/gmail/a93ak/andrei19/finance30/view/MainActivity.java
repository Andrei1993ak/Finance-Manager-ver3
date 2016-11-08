package com.gmail.a93ak.andrei19.finance30.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PursesRecycleViewAdapter;
import com.gmail.a93ak.andrei19.finance30.view.activities.CategoryStartingActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.CostActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.CurrencyActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.ImagesActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.IncomeActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.PurseActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.ReportsActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.TransferActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 0;
    private RecyclerView recyclerView;
    PursesRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecViewPursesSwissHelper(this, LOADER_ID));
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        new HelloEndpoint().execute(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(LOADER_ID) != null) {
            getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
        }
    }

    public void selectCategory(final View view) {
        switch (view.getId()) {
            case R.id.tvCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
            case R.id.tvPurse:
                startActivity(new Intent(this, PurseActivity.class));
                break;
            case R.id.tvCategories:
                startActivity(new Intent(this, CategoryStartingActivity.class));
                break;
            case R.id.tvIncomes:
                startActivity(new Intent(this, IncomeActivity.class));
                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, CostActivity.class));
                break;
            case R.id.tvSettings:
                startActivity(new Intent(this, ImagesActivity.class));
                break;
            case R.id.tvTransfers:
                startActivity(new Intent(this, TransferActivity.class));
                break;
            case R.id.tvReports:
                startActivity(new Intent(this, ReportsActivity.class));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter = new PursesRecycleViewAdapter(data, this);
        recyclerView.swapAdapter(adapter, true);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.swapAdapter(null, true);
    }

}
