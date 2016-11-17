package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class IncomeCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final DBHelperWallet dbHelperWallet;
    private final DBHelperCurrency helperCurrency;
    private final DBHelperCategoryIncome helperIncomeCategory;

    public IncomeCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelperWallet = DBHelperWallet.getInstance();
        helperCurrency = DBHelperCurrency.getInstance();
        helperIncomeCategory = DBHelperCategoryIncome.getInstance();
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.income_listitem, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.LIIncomeName);
        final String name = cursor.getString(cursor.getColumnIndex(Income.NAME));
        textViewName.setText(name);

        final TextView textViewDate = (TextView) view.findViewById(R.id.LIIncomeDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Long time = Long.parseLong(cursor.getString(cursor.getColumnIndex(Income.DATE)));
        final String date = dateFormatter.format(time);
        textViewDate.setText(date);

        final TextView tvAmount = (TextView) view.findViewById(R.id.LIIncomeWalletAmount);
        final Wallet wallet = dbHelperWallet.get(cursor.getLong(cursor.getColumnIndex(Income.WALLET_ID)));
        final Currency currency = helperCurrency.get(wallet.getCurrencyId());
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Income.AMOUNT));
        String amountString = String.format(Locale.US, "%.2f", amount);
        amountString += " ";
        amountString += currency.getCode();
        tvAmount.setText(amountString);

        final TextView category = (TextView) view.findViewById(R.id.LiIncomeCategory);
        final long CategoryId = cursor.getLong(cursor.getColumnIndex(Income.CATEGORY_ID));
        category.setText(helperIncomeCategory.get(CategoryId).getName());

    }
}
