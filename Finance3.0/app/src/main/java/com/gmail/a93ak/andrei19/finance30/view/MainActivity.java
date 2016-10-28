package com.gmail.a93ak.andrei19.finance30.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.ItemsTouchHeplers.RecViewPursesSwissHelper;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PursesRecycleViewAdapter;
import com.gmail.a93ak.andrei19.finance30.model.pojos.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CategoryStartingActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CurrencyActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.ImagesActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.IncomeActivity;
import com.gmail.a93ak.andrei19.finance30.view.Activities.PurseActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//TODO исправить баг с редатированием суммы дохода
    public static final int LOADER_ID = 0;
    private RecyclerView recyclerView;
    PursesRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecViewPursesSwissHelper(this, LOADER_ID));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(LOADER_ID) != null) {
            getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
        }
    }

    public void selectCategory(View view) {
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
            case R.id.tvSettings:
                startActivity(new Intent(this, ImagesActivity.class));
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
