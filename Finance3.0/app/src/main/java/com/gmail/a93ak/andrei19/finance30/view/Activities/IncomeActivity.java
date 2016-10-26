package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.IncomeCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.IncomeCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperIncome;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Income;

public class IncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted{

    private IncomeCursorAdapter incomeCursorAdapter;
    private ListView lvIncomes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imcome_activity);
        incomeCursorAdapter = new IncomeCursorAdapter(this, null);
        lvIncomes = (ListView) findViewById(R.id.incomeListView);
        lvIncomes.setAdapter(incomeCursorAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new IncomeCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        incomeCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        incomeCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(Result result) {

    }

    public void addIncome(View view) {
        DBHelperIncome.getInstance(DBHelper.getInstance(this)).deleteAll();
        Income income = new Income();
        income.setName("Зарплата июнь");
        income.setDate(1476835200000L);
        income.setAmount(1000.50);
        income.setPurse_id(1);
        income.setCategory_id(6);
        DBHelperIncome.getInstance(DBHelper.getInstance(this)).add(income);
        income.setCategory_id(7);
        DBHelperIncome.getInstance(DBHelper.getInstance(this)).add(income);
        getSupportLoaderManager().getLoader(0).forceLoad();
    }
}
