package com.github.andrei1993ak.finances.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.adapters.PursesRecycleViewAdapter;
import com.github.andrei1993ak.finances.control.loaders.PurseCursorLoader;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.app.activities.CategoryStartingActivity;
import com.github.andrei1993ak.finances.app.activities.CostActivity;
import com.github.andrei1993ak.finances.app.activities.CurrencyActivity;
import com.github.andrei1993ak.finances.app.activities.IncomeActivity;
import com.github.andrei1993ak.finances.app.activities.PurseActivity;
import com.github.andrei1993ak.finances.app.activities.ReportsActivity;
import com.github.andrei1993ak.finances.app.activities.SettingsActivity;
import com.github.andrei1993ak.finances.app.activities.TransferActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //TODO move to Constants.class
    public static final int LOADER_ID = 0;
    public static final int REQUEST_CODE_SETTING = 0;

    private RecyclerView recyclerView;
    private PursesRecycleViewAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Loader<Cursor> loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if (loader != null) {
            loader.forceLoad();
        }
    }

    public void onSelectedCategoryViewClick(final View view) {
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
            case R.id.tvTransfers:
                startActivity(new Intent(this, TransferActivity.class));
                break;
            case R.id.tvReports:
                startActivity(new Intent(this, ReportsActivity.class));
                break;
            case R.id.tvSettings:
                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE_SETTING);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_SETTING) {
            recreate();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        adapter = new PursesRecycleViewAdapter(data, this);
        recyclerView.swapAdapter(adapter, true);

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        recyclerView.swapAdapter(null, true);
    }

}
