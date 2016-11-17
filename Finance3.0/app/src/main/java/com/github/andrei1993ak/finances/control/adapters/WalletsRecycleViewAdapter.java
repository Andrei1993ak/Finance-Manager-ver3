package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.andrei1993ak.finances.control.viewHolders.WalletsHolder;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.R;

import java.util.Locale;

public class WalletsRecycleViewAdapter extends RecyclerView.Adapter<WalletsHolder> {
    private final Cursor cursor;
    private final Context context;

    //TODO wallet
    public WalletsRecycleViewAdapter(final Cursor cursor, final Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public WalletsHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        //TODO move layoutInflater to member
        final View view = LayoutInflater.from(context).inflate(R.layout.wallet_listitem, null);
        return new WalletsHolder(view);
    }

    @Override
    public void onBindViewHolder(final WalletsHolder holder, final int position) {
        cursor.moveToPosition(position);
        //TODO create CursorUtils method CursorUtils.getDouble(cursor, key);
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Wallet.AMOUNT));
        holder.walletAmount.setText(String.format(Locale.US, "%.2f", amount));
        holder.walletName.setText(cursor.getString(cursor.getColumnIndex(Wallet.NAME)));
        holder.walletCurrency.setText(cursor.getString(cursor.getColumnIndex(DBHelperWallet.CURRENCY_NAME)));
        holder.setWalletId(cursor.getLong(cursor.getColumnIndex(Wallet.ID)));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
