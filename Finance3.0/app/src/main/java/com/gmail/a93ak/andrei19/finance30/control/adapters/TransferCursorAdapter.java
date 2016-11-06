package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.model.models.Transfer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransferCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final DBHelperPurse helperPurse;
    private final DBHelperCurrency helperCurrency;


    public TransferCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        helperPurse = DBHelperPurse.getInstance();
        helperCurrency = DBHelperCurrency.getInstance();

    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        return inflater.inflate(R.layout.transfer_listitem, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        final TextView textViewName = (TextView) view.findViewById(R.id.LITransferName);
        final String name = cursor.getString(cursor.getColumnIndex(Transfer.NAME));
        textViewName.setText(name);

        final TextView textViewDate = (TextView) view.findViewById(R.id.LiTransferDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Long time = cursor.getLong(cursor.getColumnIndex(Transfer.DATE));
        final String date = dateFormatter.format(time);
        textViewDate.setText(date);

        final TextView textViewPurseFrom = (TextView) view.findViewById(R.id.LITransferFromPurse);
        final Purse from = helperPurse.get(cursor.getLong(cursor.getColumnIndex(Transfer.FROM_PURSE_ID)));
        textViewPurseFrom.setText(from.getName());

        final TextView textViewPurseTo = (TextView) view.findViewById(R.id.LITransferToPurse);
        final Purse to = helperPurse.get(cursor.getLong(cursor.getColumnIndex(Transfer.TO_PURSE_ID)));
        textViewPurseTo.setText(to.getName());

        final TextView fromAmount = (TextView) view.findViewById(R.id.LIFromPurseAmount);
        final Currency currencyFrom = helperCurrency.get(from.getCurrency_id());
        final Double fromAmountDouble = cursor.getDouble(cursor.getColumnIndex(Transfer.FROM_AMOUNT));
        StringBuilder builder = new StringBuilder();
        builder.append("-");
        builder.append(String.format(Locale.US, "%.2f", fromAmountDouble));
        builder.append(" ");
        builder.append(currencyFrom.getCode());
        fromAmount.setText(builder.toString());

        final TextView toAmount = (TextView) view.findViewById(R.id.LIToPurseAmount);
        final Currency currencyTo = helperCurrency.get(to.getCurrency_id());
        final Double toAmountDouble = cursor.getDouble(cursor.getColumnIndex(Transfer.TO_AMOUNT));
        builder = new StringBuilder();
        builder.append("+");
        builder.append(String.format(Locale.US, "%.2f", toAmountDouble));
        builder.append(" ");
        builder.append(currencyTo.getCode());
        toAmount.setText(builder.toString());
    }
}
