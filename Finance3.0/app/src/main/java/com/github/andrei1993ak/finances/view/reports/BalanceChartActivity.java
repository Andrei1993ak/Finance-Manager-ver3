package com.github.andrei1993ak.finances.view.reports;

import android.content.Context;
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

import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.loaders.BalanceChartLoader;
import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.executors.PurseExecutor;
import com.github.andrei1993ak.finances.model.models.Purse;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

public class BalanceChartActivity extends AppCompatActivity implements OnTaskCompleted, LoaderManager.LoaderCallbacks<TimeSeries> {

    public static final int MAIN_LOADER = 0;
    public static final String POSITION = "position";
    private int position;
    private AppCompatSpinner spinner;
    private List<Purse> pursesList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        setTitle(R.string.balanceCHart);
        if (savedInstanceState == null) {
            position = 0;
        } else {
            position = savedInstanceState.getInt(POSITION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_activity);
        spinner = (AppCompatSpinner) findViewById(R.id.pursesNamesBalanceChart);
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
    }

    private GraphicalView buildView(final TimeSeries series) {

        final XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(series);
        final XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        renderer.setDisplayBoundingPoints(false);
        renderer.setPointStyle(PointStyle.POINT);
        renderer.setPointStrokeWidth(2);
        final XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true, false);
        final double[] panLimits = new double[4];
        final double[] zoomLimits = new double[4];
        zoomLimits[0] = panLimits[0] = series.getX(series.getItemCount() - 1);
        zoomLimits[1] = panLimits[1] = series.getX(0);
        mRenderer.setPanLimits(panLimits);
        mRenderer.setZoomLimits(zoomLimits);
        mRenderer.setZoomInLimitX(600000000);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setAxisTitleTextSize(30);
        mRenderer.setShowLegend(false);
        mRenderer.setShowGrid(true);
        mRenderer.setYAxisMin(series.getMinY());
        mRenderer.setYAxisMax(series.getMaxY());
        return ChartFactory.getTimeChartView(this, dataSet, mRenderer, "dd-MMM-yyyy");
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
                    args.putLong(Purse.ID, purseId);
                    getSupportLoaderManager().restartLoader(MAIN_LOADER, args, BalanceChartActivity.this);
                    final Loader<Object> loader = BalanceChartActivity.this.getSupportLoaderManager().getLoader(MAIN_LOADER);
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
    public Loader<TimeSeries> onCreateLoader(final int id, final Bundle args) {
        return new BalanceChartLoader(this, args.getLong(Purse.ID));
    }

    @Override
    public void onLoadFinished(final Loader<TimeSeries> loader, final TimeSeries data) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chartBalance);
        layout.removeAllViews();
        final GraphicalView balanceChartView = buildView(data);
        layout.addView(balanceChartView, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onLoaderReset(final Loader<TimeSeries> loader) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        if (layout != null) {
            layout.removeAllViews();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, spinner.getSelectedItemPosition());
    }
}
