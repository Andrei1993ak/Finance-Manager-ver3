package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.R;

import java.util.Locale;

public class WalletCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;


    public WalletCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.wallet_listitem, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.walletName);
        final String name = cursor.getString(cursor.getColumnIndex(Wallet.NAME));
        textViewName.setText(name);

        final TextView tvAmount = (TextView) view.findViewById(R.id.walletAmount);
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Wallet.AMOUNT));
        final String amountString = String.format(Locale.US, "%.2f", amount);
        tvAmount.setText(amountString);

        final TextView tvCurrencyCode = (TextView) view.findViewById(R.id.walletCurrency);
        final String code = cursor.getString(cursor.getColumnIndex(DBHelperWallet.CURRENCY_NAME));
        tvCurrencyCode.setText(code);

    }
}
