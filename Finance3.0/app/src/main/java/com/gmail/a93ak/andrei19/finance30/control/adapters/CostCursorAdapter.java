package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryCost;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.models.Cost;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CostCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final DBHelperPurse helperPurse;
    private final DBHelperCurrency helperCurrency;
    private final DBHelperCategoryCost dbHelperCategoryCost;

    public CostCursorAdapter(final Context context, final Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        helperPurse = DBHelperPurse.getInstance();
        helperCurrency = DBHelperCurrency.getInstance();
        dbHelperCategoryCost = DBHelperCategoryCost.getInstance();
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.cost_listitem, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.LICostName);
        final String name = cursor.getString(cursor.getColumnIndex(Cost.NAME));
        textViewName.setText(name);

        final TextView textViewDate = (TextView) view.findViewById(R.id.LICostDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final String date = dateFormatter.format(cursor.getLong(cursor.getColumnIndex(Cost.DATE)));
        textViewDate.setText(date);

        final TextView tvAmount = (TextView) view.findViewById(R.id.LICostPurseAmount);
        final Purse purse = helperPurse.get(cursor.getLong(cursor.getColumnIndex(Cost.PURSE_ID)));
        final Currency currency = helperCurrency.get(purse.getCurrency_id());
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Cost.AMOUNT));
        String amountString = String.format(Locale.US, "%.2f", amount);
        amountString += " ";
        amountString += currency.getCode();
        tvAmount.setText(amountString);

        final TextView category = (TextView) view.findViewById(R.id.LiCostCategory);
        final long CategoryId = cursor.getLong(cursor.getColumnIndex(Cost.CATEGORY_ID));
        category.setText(dbHelperCategoryCost.get(CategoryId).getName());

    }
}
