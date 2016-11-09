package com.gmail.a93ak.andrei19.finance30.view.reports;

import android.graphics.Color;
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

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Loaders.PieReportLoader;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PieIncomeAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.IncomePieCategory;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class ReportIncomePieActivity extends AppCompatActivity implements OnTaskCompleted, LoaderManager.LoaderCallbacks<ArrayList<IncomePieCategory>> {

    private GraphicalView mChartView;
    private List<Purse> pursesList;
    private AppCompatSpinner spinner;
    private ListView incomeListView;
    private PieIncomeAdapter adapter;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie);
        spinner = (AppCompatSpinner) findViewById(R.id.pursesNames);
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));

    }

    protected void onResume() {
        super.onResume();
        if (mChartView != null) {
            mChartView.repaint();
        }
    }

    public GraphicalView buildView(final String[] bars, final Double[] values) {
        final int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA,
                Color.YELLOW, Color.CYAN, Color.RED};
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
                    args.putLong("id", purseId);
                    getSupportLoaderManager().restartLoader(0, args, ReportIncomePieActivity.this);
                    final Loader<Object> loader = ReportIncomePieActivity.this.getSupportLoaderManager().getLoader(0);
                    loader.forceLoad();
                }

                @Override
                public void onNothingSelected(final AdapterView<?> parent) {

                }
            });
        }
    }

    @Override
    public Loader<ArrayList<IncomePieCategory>> onCreateLoader(final int id, final Bundle args) {
        return new PieReportLoader(this, (Long) args.get("id"));
    }

    @Override
    public void onLoadFinished(final Loader<ArrayList<IncomePieCategory>> loader, final ArrayList<IncomePieCategory> data) {
        incomeListView = (ListView) findViewById(R.id.incomePieListView);
        adapter = new PieIncomeAdapter(this, data);
        incomeListView.setAdapter(adapter);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.removeAllViews();
        final String[] names = new String[data.size()];
        final Double[] values = new Double[data.size()];
        int i = 0;
        for (final IncomePieCategory incomePieCategory : data) {
            names[i] = incomePieCategory.getCategoryName();
            values[i++] = incomePieCategory.getAmount();
        }
        mChartView = buildView(names, values);
        layout.addView(mChartView, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onLoaderReset(final Loader<ArrayList<IncomePieCategory>> loader) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.removeAllViews();
    }
}
