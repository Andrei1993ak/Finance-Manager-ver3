package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.models.Transfer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransferCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final DBHelperWallet dbHelperWallet;
    private final DBHelperCurrency helperCurrency;


    public TransferCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelperWallet = DBHelperWallet.getInstance();
        helperCurrency = DBHelperCurrency.getInstance();

    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        return inflater.inflate(R.layout.transfer_listitem, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        //TODO implement custom view
        final TextView textViewName = (TextView) view.findViewById(R.id.LITransferName);
        final String name = cursor.getString(cursor.getColumnIndex(Transfer.NAME));
        textViewName.setText(name);

        final TextView textViewDate = (TextView) view.findViewById(R.id.LiTransferDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Long time = cursor.getLong(cursor.getColumnIndex(Transfer.DATE));
        final String date = dateFormatter.format(time);
        textViewDate.setText(date);

        final TextView textViewWalletFrom = (TextView) view.findViewById(R.id.LITransferFromwWallet);
        final Wallet from = dbHelperWallet.get(cursor.getLong(cursor.getColumnIndex(Transfer.FROM_WALLET_ID)));
        textViewWalletFrom.setText(from.getName());

        final TextView textViewWalletTo = (TextView) view.findViewById(R.id.LITransferToWallet);
        final Wallet to = dbHelperWallet.get(cursor.getLong(cursor.getColumnIndex(Transfer.TO_WALLET_ID)));
        textViewWalletTo.setText(to.getName());

        final TextView fromAmount = (TextView) view.findViewById(R.id.LIFromWalletAmount);
        final Currency currencyFrom = helperCurrency.get(from.getCurrencyId());
        final Double fromAmountDouble = cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT));
        StringBuilder builder = new StringBuilder();
        builder.append("-");
        builder.append(String.format(Locale.US, "%.2f", fromAmountDouble));
        builder.append(" ");
        builder.append(currencyFrom.getCode());
        fromAmount.setText(builder.toString());

        final TextView toAmount = (TextView) view.findViewById(R.id.LIToWalletAmount);
        final Currency currencyTo = helperCurrency.get(to.getCurrencyId());
        final Double toAmountDouble = cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT));
        //TODO prepare string before insert to DB
        builder = new StringBuilder();
        builder.append("+");
        builder.append(String.format(Locale.US, "%.2f", toAmountDouble));
        builder.append(" ");
        builder.append(currencyTo.getCode());
        toAmount.setText(builder.toString());
    }
}
