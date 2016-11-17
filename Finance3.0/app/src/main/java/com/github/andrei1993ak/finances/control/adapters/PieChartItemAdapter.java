package com.github.andrei1993ak.finances.control.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;

import java.util.ArrayList;
import java.util.Locale;

public class PieChartItemAdapter extends BaseAdapter {

    private final ArrayList<PieChartItem> list;
    private final LayoutInflater layoutInflater;

    public PieChartItemAdapter(final Context context, final ArrayList<PieChartItem> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(final int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return list.get(position).getCategoryId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.reports_pie_chart_listitem, parent, false);
        }
        final PieChartItem item = (PieChartItem) getItem(position);
        ((TextView) view.findViewById(R.id.pieChartItemName)).setText(item.getCategoryName());
        final Double amount = item.getAmount();
        final String amountString = String.format(Locale.US, "%.2f", amount);
        ((TextView) view.findViewById(R.id.pieChartItemAmount)).setText(amountString);
        return view;
    }
}
