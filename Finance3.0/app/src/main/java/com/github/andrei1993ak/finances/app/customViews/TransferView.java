package com.github.andrei1993ak.finances.app.customViews;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.CursorUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransferView extends TableLayout {

    private TextView transferName;
    private TextView fromWallet;
    private TextView fromWalletAmount;
    private TextView transferDate;
    private TextView toWallet;
    private TextView toWalletAmount;

    private DBHelperWallet dbHelperWallet;
    private DBHelperCurrency helperCurrency;

    public TransferView(final Context context) {
        super(context);
        init(context);
    }

    public TransferView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        final View rootView = inflate(context, R.layout.adapter_transfer_item, this);
        this.transferName = (TextView) rootView.findViewById(R.id.LITransferName);
        this.fromWallet = (TextView) rootView.findViewById(R.id.LITransferFromWallet);
        this.fromWalletAmount = (TextView) rootView.findViewById(R.id.LITransferFromWalletAmount);
        this.transferDate = (TextView) rootView.findViewById(R.id.LiTransferDate);
        this.toWallet = (TextView) rootView.findViewById(R.id.LITransferToWallet);
        this.toWalletAmount = (TextView) rootView.findViewById(R.id.LITransferToWalletAmount);
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
        this.helperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
    }

    public void setFields(final Cursor cursor) {

        final String name = CursorUtils.getString(cursor, Transfer.NAME);
        transferName.setText(name);

        final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final Long time = CursorUtils.getLong(cursor, Transfer.DATE);
        final String date = dateFormatter.format(time);
        transferDate.setText(date);

        final Wallet from = dbHelperWallet.get(CursorUtils.getLong(cursor, Transfer.FROM_WALLET_ID));
        fromWallet.setText(from.getName());

        final Wallet to = dbHelperWallet.get(CursorUtils.getLong(cursor, Transfer.TO_WALLET_ID));
        toWallet.setText(to.getName());

        final Currency currencyFrom = helperCurrency.get(from.getCurrencyId());
        final Double fromAmountDouble = CursorUtils.getDouble(cursor, Transfer.FROM_AMOUNT);
        final StringBuilder builder = new StringBuilder();
        builder.append("-");
        builder.append(String.format(Locale.getDefault(), Constants.MAIN_DOUBLE_FORMAT, fromAmountDouble));
        builder.append(" ");
        builder.append(currencyFrom.getCode());
        fromWalletAmount.setText(builder.toString());
        builder.setLength(0);

        final Currency currencyTo = helperCurrency.get(to.getCurrencyId());
        final Double toAmountDouble = CursorUtils.getDouble(cursor, Transfer.TO_AMOUNT);
        builder.append("+");
        builder.append(String.format(Locale.getDefault(), Constants.MAIN_DOUBLE_FORMAT, toAmountDouble));
        builder.append(" ");
        builder.append(currencyTo.getCode());
        toWalletAmount.setText(builder.toString());
        builder.setLength(0);
    }
}
