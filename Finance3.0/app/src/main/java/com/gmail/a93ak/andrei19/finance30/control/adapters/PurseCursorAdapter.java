package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import java.util.Locale;

public class PurseCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;


    public PurseCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.purse_listitem, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.purseName);
        final String name = cursor.getString(cursor.getColumnIndex(Purse.NAME));
        textViewName.setText(name);

        final TextView tvAmount = (TextView) view.findViewById(R.id.purseAmount);
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Purse.AMOUNT));
        final String amountString = String.format(Locale.US, "%.2f", amount);
        tvAmount.setText(amountString);

        final TextView tvCurrencyCode = (TextView) view.findViewById(R.id.purseCurrency);
        final String code = cursor.getString(cursor.getColumnIndex(DBHelperPurse.CURRENCY_NAME));
        tvCurrencyCode.setText(code);

    }
}
