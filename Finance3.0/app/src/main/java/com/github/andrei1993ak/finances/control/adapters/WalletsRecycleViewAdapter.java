package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.viewHolders.WalletsHolder;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.CursorUtils;

import java.util.Locale;

public class WalletsRecycleViewAdapter extends RecyclerView.Adapter<WalletsHolder> {
    private final Cursor cursor;
    private final LayoutInflater layoutInflater;


    public WalletsRecycleViewAdapter(final Cursor cursor, final Context context) {
        this.cursor = cursor;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public WalletsHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = layoutInflater.inflate(R.layout.adapter_wallet_item, parent, false);
        return new WalletsHolder(view);
    }

    @Override
    public void onBindViewHolder(final WalletsHolder holder, final int position) {
        cursor.moveToPosition(position);
        final Double amount = CursorUtils.getDouble(cursor, Wallet.AMOUNT);
        holder.walletAmount.setText(String.format(Locale.getDefault(), Constants.MAIN_DOUBLE_FORMAT, amount));
        holder.walletName.setText(CursorUtils.getString(cursor, Wallet.NAME));
        holder.walletCurrency.setText(CursorUtils.getString(cursor, Constants.CURRENCY));
        holder.setWalletId(cursor.getLong(cursor.getColumnIndex(Wallet.ID)));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
