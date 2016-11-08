package com.gmail.a93ak.andrei19.finance30.view.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.gmail.a93ak.andrei19.finance30.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class ReportIncomePieActivity extends AppCompatActivity {

    private GraphicalView mChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_income_pie);

    }

    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            final LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = buildIntent();
            layout.addView(mChartView, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {
            mChartView.repaint();
        }
    }

    public GraphicalView buildIntent() {

        int[] values = new int[] { 75, 50, 25, 15, 5 };        // шаг 2

        String[] bars = new String[] {"Francesca's",  "King of Clubs",

                "Zen Lounge", "Tied House", "Molly Magees"};

        int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA,

                Color.YELLOW, Color.CYAN };



        CategorySeries series = new CategorySeries("Pie Chart");  // шаг 3

        DefaultRenderer dr = new DefaultRenderer();   // шаг 4



        for (int v=0; v<5; v++){    // шаг 5

            series.add(bars[v], values[v]);

            SimpleSeriesRenderer r = new SimpleSeriesRenderer();

            r.setColor(colors[v]);

            dr.addSeriesRenderer(r);

        }
        dr.setZoomButtonsVisible(false);
        dr.setZoomEnabled(false);
        dr.setPanEnabled(false);
        dr.setShowLegend(false);
        dr.setStartAngle(270
         );
        dr.setLabelsTextSize(30);
        return ChartFactory.getPieChartView(this, series, dr);


    }
}
