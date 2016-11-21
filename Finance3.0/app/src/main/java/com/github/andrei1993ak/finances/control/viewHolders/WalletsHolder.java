package com.github.andrei1993ak.finances.control.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;

public class WalletsHolder extends RecyclerView.ViewHolder{

    public TextView walletName;
    public TextView walletAmount;
    public TextView walletCurrency;
    private long walletId;

    public WalletsHolder(final View itemView) {
        super(itemView);
        this.walletName = (TextView)itemView.findViewById(R.id.walletName);
        this.walletAmount = (TextView)itemView.findViewById(R.id.walletAmount);
        this.walletCurrency = (TextView)itemView.findViewById(R.id.walletCurrency);
    }

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(final long walletId) {
        this.walletId = walletId;
    }

}
