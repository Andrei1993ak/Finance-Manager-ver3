package com.gmail.a93ak.andrei19.finance30.control.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.IncomePieCategory;

import java.util.ArrayList;
import java.util.Locale;

public class PieIncomeAdapter extends BaseAdapter {

    private final ArrayList<IncomePieCategory> list;
    private final LayoutInflater layoutInflater;

    public PieIncomeAdapter(final Context context, final ArrayList<IncomePieCategory> list) {
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
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.incom_pie_listitem, parent, false);
        }
        final IncomePieCategory inc = (IncomePieCategory) getItem(position);
        ((TextView) view.findViewById(R.id.incomePieName)).setText(inc.getCategoryName());
        Double amount = inc.getAmount();
        String amountString = String.format(Locale.US, "%.2f", amount);
        ((TextView) view.findViewById(R.id.incomePieAmount)).setText(amountString);
        return view;
    }
}
