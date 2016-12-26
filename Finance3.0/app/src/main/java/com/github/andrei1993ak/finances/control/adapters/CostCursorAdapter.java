package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.CursorUtils;
import com.github.andrei1993ak.finances.util.universalLoader.IUniversalLoader;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CostCursorAdapter extends CursorAdapter {

    private final String uriTemplate = "file:/data/data/com.github.andrei1993ak.finances/files/images/%s.jpg";
    private final LayoutInflater inflater;
    private final DBHelperWallet dbHelperWallet;
    private final DBHelperCurrency helperCurrency;
    private final DBHelperCategoryCost dbHelperCategoryCost;
    private final IUniversalLoader<Bitmap, ImageView> bitmapLoader;


    public CostCursorAdapter(final Context context, final Cursor c) {
        super(context, c, 0);
        this.bitmapLoader = ((App) ContextHolder.getInstance().getContext()).getImageLoader(context);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbHelperWallet = ((DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class));
        this.helperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
        this.dbHelperCategoryCost = ((DBHelperCategoryCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CostCategory.class));
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.adapter_cost_tem, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.LICostName);
        final String name = CursorUtils.getString(cursor, Cost.NAME);
        textViewName.setText(name);
        ImageView imageView = (ImageView) view.findViewById(R.id.LiCostPhoto);
        if (CursorUtils.getInteger(cursor, Cost.PHOTO) == Constants.COST_HAS_PHOTO) {
            imageView.getLayoutParams().height = 1000;
            final String imageUri = String.format(uriTemplate, String.valueOf(CursorUtils.getLong(cursor, Cost.ID)));
            bitmapLoader.load(imageUri, imageView);
        } else {
            imageView.setImageResource(0);
            imageView.getLayoutParams().height = 0;
        }
        final TextView textViewDate = (TextView) view.findViewById(R.id.LICostDate);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final String date = dateFormatter.format(CursorUtils.getLong(cursor, Cost.DATE));
        textViewDate.setText(date);

        final TextView tvAmount = (TextView) view.findViewById(R.id.LICostWalletAmount);
        final Wallet wallet = dbHelperWallet.get(CursorUtils.getLong(cursor, Cost.WALLET_ID));
        final Currency currency = helperCurrency.get(wallet.getCurrencyId());
        final Double amount = CursorUtils.getDouble(cursor, Cost.AMOUNT);
        String amountString = String.format(Locale.US, Constants.MAIN_DOUBLE_FORMAT, amount);
        amountString += " ";
        amountString += currency.getCode();
        tvAmount.setText(amountString);

        final TextView category = (TextView) view.findViewById(R.id.LiCostCategory);
        final long CategoryId = CursorUtils.getLong(cursor, Cost.CATEGORY_ID);
        category.setText(dbHelperCategoryCost.get(CategoryId).getName());

    }
}
