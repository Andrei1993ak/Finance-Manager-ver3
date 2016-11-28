package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.CursorUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class IncomeCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final DBHelperWallet dbHelperWallet;
    private final DBHelperCurrency helperCurrency;
    private final DBHelperCategoryIncome helperIncomeCategory;

    public IncomeCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
        this.helperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
        this.helperIncomeCategory = ((DBHelperCategoryIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(IncomeCategory.class));
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.adapter_income_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.LIIncomeName);
        final String name = CursorUtils.getString(cursor, Income.NAME);
        textViewName.setText(name);

        final TextView textViewDate = (TextView) view.findViewById(R.id.LIIncomeDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final Long time = CursorUtils.getLong(cursor, Income.DATE);
        final String date = dateFormatter.format(time);
        textViewDate.setText(date);

        final TextView tvAmount = (TextView) view.findViewById(R.id.LIIncomeWalletAmount);
        final Wallet wallet = dbHelperWallet.get(CursorUtils.getLong(cursor, Income.WALLET_ID));
        final Currency currency = helperCurrency.get(wallet.getCurrencyId());
        final Double amount = CursorUtils.getDouble(cursor, Income.AMOUNT);
        String amountString = String.format(Locale.getDefault(), Constants.MAIN_DOUBLE_FORMAT, amount);
        amountString += " ";
        amountString += currency.getCode();
        tvAmount.setText(amountString);

        final TextView category = (TextView) view.findViewById(R.id.LiIncomeCategory);
        final long CategoryId = CursorUtils.getLong(cursor, Income.CATEGORY_ID);
        category.setText(helperIncomeCategory.get(CategoryId).getName());

    }
}
