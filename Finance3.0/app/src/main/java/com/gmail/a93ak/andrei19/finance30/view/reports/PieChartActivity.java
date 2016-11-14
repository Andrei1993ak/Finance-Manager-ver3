package com.gmail.a93ak.andrei19.finance30.view.reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PieChartItemAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.PieReportLoader;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.PieChartItem;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity implements OnTaskCompleted, LoaderManager.LoaderCallbacks<ArrayList<PieChartItem>> {

    public static final int MAIN_LOADER = 0;
    public static final String POSITION = "position";
    private GraphicalView mChartView;
    private List<Purse> pursesList;
    private AppCompatSpinner spinner;
    private int position;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie);
        if (getIntent().getBooleanExtra(PieChartItem.TYPE, false)){
            setTitle(R.string.incomesByCategories);
        } else {
            setTitle(R.string.costsByCategories);
        }
        spinner = (AppCompatSpinner) findViewById(R.id.pursesNames);
        if (savedInstanceState == null) {
            position = 0;
        } else {
            position = savedInstanceState.getInt(POSITION);
        }
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
    }

    protected void onResume() {
        super.onResume();
        if (mChartView != null) {
            mChartView.repaint();
        }
    }

    public GraphicalView buildView(final String[] bars, final Double[] values) {
        final int[] colors = new int[]{0xFFA5EA8C, 0xFFEAA28C, 0xFF8CA5EA,
                0xFFEAE18C, 0xFFEA8CA4, 0xFF80ede2};
        final CategorySeries series = new CategorySeries("Pie Chart");
        final DefaultRenderer dr = new DefaultRenderer();
        for (int v = 0; v < bars.length; v++) {
            series.add(bars[v], values[v]);
            final SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[v % 6]);
            dr.addSeriesRenderer(r);
        }
        dr.setZoomButtonsVisible(false);
        dr.setZoomEnabled(false);
        dr.setPanEnabled(false);
        dr.setShowLegend(false);
        dr.setStartAngle(270);
        dr.setLabelsTextSize(30);
        return ChartFactory.getPieChartView(this, series, dr);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        if (id == PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST) {
            pursesList = (List<Purse>) result.getObject();
            final String[] pursesNames = new String[pursesList.size()];
            int i = 0;
            for (final Purse purse : pursesList) {
                pursesNames[i++] = purse.getName();
            }
            final ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
            spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerPursesAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                    final long purseId = (pursesList.get(position).getId());
                    final Bundle args = new Bundle();
                    args.putLong(Income.PURSE_ID, purseId);
                    args.putBoolean(PieChartItem.TYPE, getIntent().getBooleanExtra(PieChartItem.TYPE, false));
                    args.putLong(Income.CATEGORY_ID, -1L);
                    getSupportLoaderManager().restartLoader(MAIN_LOADER, args, PieChartActivity.this);
                    final Loader<Object> loader = PieChartActivity.this.getSupportLoaderManager().getLoader(MAIN_LOADER);
                    loader.forceLoad();
                }

                @Override
                public void onNothingSelected(final AdapterView<?> parent) {

                }
            });
            spinner.setSelection(position);
        }
    }

    @Override
    public Loader<ArrayList<PieChartItem>> onCreateLoader(final int id, final Bundle args) {
        return new PieReportLoader(this, args);
    }

    @Override
    public void onLoadFinished(final Loader<ArrayList<PieChartItem>> loader, final ArrayList<PieChartItem> data) {
        final ListView listView = (ListView) findViewById(R.id.pieListView);
        final PieChartItemAdapter adapter = new PieChartItemAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final long mId = ((PieChartItem) parent.getItemAtPosition(position)).getCategoryId();
                final Intent intent = new Intent(PieChartActivity.this, PieChartActivityNext.class);
                intent.putExtra(Income.CATEGORY_ID, mId);
                intent.putExtra(PieChartItem.TYPE, getIntent().getBooleanExtra(PieChartItem.TYPE, false));
                intent.putExtra(Income.PURSE_ID, pursesList.get(spinner.getSelectedItemPosition()).getId());
                startActivity(intent);
            }
        });
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.removeAllViews();
        final String[] names = new String[data.size()];
        final Double[] values = new Double[data.size()];
        int i = 0;
        for (final PieChartItem pieChartItem : data) {
            names[i] = pieChartItem.getCategoryName();
            values[i++] = pieChartItem.getAmount();
        }
        mChartView = buildView(names, values);
        layout.addView(mChartView, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onLoaderReset(final Loader<ArrayList<PieChartItem>> loader) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.removeAllViews();

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION,spinner.getSelectedItemPosition());
    }
}
