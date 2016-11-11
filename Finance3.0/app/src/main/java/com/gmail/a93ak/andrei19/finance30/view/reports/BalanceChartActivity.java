package com.gmail.a93ak.andrei19.finance30.view.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class BalanceChartActivity extends AppCompatActivity implements OnTaskCompleted {

    private AppCompatSpinner spinner;
    private List<Purse> pursesList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_activity);
        spinner = (AppCompatSpinner) findViewById(R.id.pursesNamesBalanceChart);
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
//        createChart();
    }

    private void createChart() {

        final int days = 100;
        int sum = 500;
        int max = 0;
        int min = 500;

        final TimeSeries series = new TimeSeries("Balance");
        final Random r = new Random();
        for (int i = 0; i < days; i++) {
            final GregorianCalendar gc = new GregorianCalendar(2016, 10, i + 1);
            sum -= 7 * r.nextDouble();
            if (i % 30 == 0) {
                sum += 250 * r.nextDouble();
            }
            if (sum > max) {
                max = sum;
            }
            if (sum < min) {
                min = sum;
            }
            series.add(gc.getTime(), sum);
        }

        final XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(series);

        final XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(1);
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
        panLimits[0] = new GregorianCalendar(2016, 10, 1).getTimeInMillis();
        panLimits[1] = new GregorianCalendar(2016, 10, 1 + days).getTimeInMillis();
        final double[] zoomLimits = new double[4];
        zoomLimits[0] = new GregorianCalendar(2016, 10, 1).getTimeInMillis();
        zoomLimits[1] = new GregorianCalendar(2016, 10, 1 + days).getTimeInMillis();
        mRenderer.setPanLimits(panLimits);
        mRenderer.setZoomLimits(zoomLimits);

        mRenderer.setZoomInLimitX(600000000);
        mRenderer.setLabelsTextSize(35);
        mRenderer.setAxisTitleTextSize(35);
        mRenderer.setShowLegend(false);
        mRenderer.setShowGrid(true);
        mRenderer.setYAxisMin(min);
        mRenderer.setYAxisMax(max);
        final GraphicalView chartView = ChartFactory.getTimeChartView(this, dataSet, mRenderer, "dd-MMM-yyyy");
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
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                    createChart();
                }

                @Override
                public void onNothingSelected(final AdapterView<?> parent) {

                }
            });
        }
    }
}
