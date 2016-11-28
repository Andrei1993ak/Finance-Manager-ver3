package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.CursorUtils;

import java.util.Locale;

public class WalletCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;


    public WalletCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.adapter_wallet_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.walletName);
        final String name = CursorUtils.getString(cursor, Wallet.NAME);
        textViewName.setText(name);

        final TextView tvAmount = (TextView) view.findViewById(R.id.walletAmount);
        final Double amount = CursorUtils.getDouble(cursor, Wallet.AMOUNT);
        final String amountString = String.format(Locale.getDefault(), Constants.MAIN_DOUBLE_FORMAT, amount);
        tvAmount.setText(amountString);

        final TextView tvCurrencyCode = (TextView) view.findViewById(R.id.walletCurrency);
        final String code = CursorUtils.getString(cursor, Constants.CURRENCY);
        tvCurrencyCode.setText(code);

    }
}
