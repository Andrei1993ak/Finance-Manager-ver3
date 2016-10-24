package com.gmail.a93ak.andrei19.finance30.control.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;

public class PursesHolder extends RecyclerView.ViewHolder{
//
    public TextView purseName;
    public TextView purseAmount;
    public TextView purseCurrency;
    private long purseId;

    public PursesHolder(View itemView) {
        super(itemView);
        purseName = (TextView)itemView.findViewById(R.id.purseName);
        purseAmount = (TextView)itemView.findViewById(R.id.purseAmount);
        purseCurrency = (TextView)itemView.findViewById(R.id.purseCurrency);
    }

    public long getPurseId() {
        return purseId;
    }

    public void setPurseId(long purseId) {
        this.purseId = purseId;
    }




}
