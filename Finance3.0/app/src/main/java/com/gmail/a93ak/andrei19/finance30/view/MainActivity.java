package com.gmail.a93ak.andrei19.finance30.view;

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

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PursesRecycleViewAdapter;
import com.gmail.a93ak.andrei19.finance30.control.loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.view.activities.CategoryStartingActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.CostActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.CurrencyActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.IncomeActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.PurseActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.ReportsActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.SettingsActivity;
import com.gmail.a93ak.andrei19.finance30.view.activities.TransferActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //TODO move to Constants.class
    public static final int LOADER_ID = 0;
    public static final int REQUEST_CODE_SETTING = 0;

    private RecyclerView recyclerView;
    //TODO modificator of access
    PursesRecycleViewAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
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
        //TODO move to local final var
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
