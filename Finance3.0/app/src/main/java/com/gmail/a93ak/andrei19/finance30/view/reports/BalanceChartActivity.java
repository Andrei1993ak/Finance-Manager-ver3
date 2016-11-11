package com.gmail.a93ak.andrei19.finance30.view.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;
import java.util.Random;

public class BalanceChartActivity extends AppCompatActivity implements OnTaskCompleted {

    private AppCompatSpinner spinner;
    private List<Purse> pursesList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_activity);
//        spinner = (AppCompatSpinner) findViewById(R.id.pursesNamesBalanceChart);
//        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
        createChart();
    }

    private void createChart() {
        final XYSeries series = new XYSeries("Balance");
        int sum = 500;
        for (int day = 0; day < 500; day++) {
            final Random r = new Random();
            sum -= 2 * r.nextDouble();
            series.add(day++, sum);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        final XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(1);
        renderer.setColor(Color.RED);
        renderer.setDisplayBoundingPoints(false);
        renderer.setPointStyle(PointStyle.POINT);
        renderer.setPointStrokeWidth(2);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setPanEnabled(true, false);
        final double[] panLimits = new double[4];
        panLimits[0] = 0;
        panLimits[1] = 500;
        mRenderer.setPanLimits(panLimits);
        mRenderer.setZoomEnabled(true, false);
        final double[] zoomLimits = new double[4];
        zoomLimits[0] = 0;
        zoomLimits[1] = 500;
        zoomLimits[2] = 0;
        zoomLimits[3] = 0;
        mRenderer.setZoomLimits(zoomLimits);

//        mRenderer.setPointSize(40);
        mRenderer.setLabelsTextSize(40);
        mRenderer.setLegendTextSize(40);
        mRenderer.setAxisTitleTextSize(40);
        mRenderer.setChartTitleTextSize(40);
        mRenderer.setZoomInLimitX(30);
        mRenderer.setYAxisMax(500);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true);
        final GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.chartBalance);
        layout.removeAllViews();
        layout.addView(chartView, 0);
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
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
//                    final long purseId = (pursesList.get(position).getId());
////                    final Bundle args = new Bundle();
////                    args.putLong(Income.PURSE_ID, purseId);
////                    args.putBoolean(PieChartItem.TYPE, getIntent().getBooleanExtra(PieChartItem.TYPE, false));
////                    args.putLong(Income.CATEGORY_ID, -1L);
////                    getSupportLoaderManager().restartLoader(MAIN_LOADER, args, PieChartActivity.this);
////                    final Loader<Object> loader = PieChartActivity.this.getSupportLoaderManager().getLoader(MAIN_LOADER);
////                    loader.forceLoad();
//                }
//
//                @Override
//                public void onNothingSelected(final AdapterView<?> parent) {
//
//                }
//            });
        }
    }
}
