package com.github.andrei1993ak.finances.app.reportsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.adapters.PieChartItemAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.control.loaders.PieReportLoader;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.util.Constants;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends BaseActivity implements IOnTaskCompleted, LoaderManager.LoaderCallbacks<ArrayList<PieChartItem>> {

    public static final String POSITION = "position";
    private GraphicalView chartView;
    private List<Wallet> wallets;
    private AppCompatSpinner spinner;
    private int position;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie);
        if (getIntent().getBooleanExtra(Constants.PIE_CHART_TYPE, false)){
            setTitle(R.string.incomesByCategories);
        } else {
            setTitle(R.string.costsByCategories);
        }
        this.spinner = (AppCompatSpinner) findViewById(R.id.walletsNames);
        if (savedInstanceState == null) {
            this.position = 0;
        } else {
            this.position = savedInstanceState.getInt(POSITION);
        }
        new WalletExecutor(this).execute(new RequestAdapter<Wallet>().getAllToList(RequestAdapter.SELECTION_ALL));
    }

    protected void onResume() {
        super.onResume();
        if (chartView != null) {
            chartView.repaint();
        }
    }

    public GraphicalView buildView(final String[] bars, final Double[] values) {
        final int[] colors = getBaseContext().getResources().getIntArray(R.array.pieChartColors);
        final CategorySeries series = new CategorySeries("Pie Chart");
        final DefaultRenderer dr = new DefaultRenderer();
        for (int v = 0; v < bars.length; v++) {
            series.add(bars[v], values[v]);
            final SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[v % colors.length]);
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
        if (id == WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST) {
            wallets = (List<Wallet>) result.getObject();
            final String[] walletsNames = new String[wallets.size()];
            int i = 0;
            for (final Wallet wallet : wallets) {
                walletsNames[i++] = wallet.getName();
            }
            final ArrayAdapter<String> spinnerWalletsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletsNames);
            spinnerWalletsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerWalletsAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                    final long walletId = (wallets.get(position).getId());
                    final Bundle args = new Bundle();
                    args.putLong(Income.WALLET_ID, walletId);
                    args.putBoolean(Constants.PIE_CHART_TYPE, getIntent().getBooleanExtra(Constants.PIE_CHART_TYPE, false));
                    args.putLong(Income.CATEGORY_ID, -1L);
                    getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, args, PieChartActivity.this);
                    final Loader<Object> loader = PieChartActivity.this.getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
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
                intent.putExtra(Constants.PIE_CHART_TYPE, getIntent().getBooleanExtra(Constants.PIE_CHART_TYPE, false));
                intent.putExtra(Income.WALLET_ID, wallets.get(spinner.getSelectedItemPosition()).getId());
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
        chartView = buildView(names, values);
        layout.addView(chartView, new LinearLayout.LayoutParams
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
