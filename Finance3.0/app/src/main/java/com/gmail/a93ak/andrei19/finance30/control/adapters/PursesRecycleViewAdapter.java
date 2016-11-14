package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.viewHolders.PursesHolder;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

import java.util.Locale;

public class PursesRecycleViewAdapter extends RecyclerView.Adapter<PursesHolder> {
    private final Cursor cursor;
    private final Context context;

    //TODO wallet
    public PursesRecycleViewAdapter(final Cursor cursor, final Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public PursesHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        //TODO move layoutInflater to member
        final View view = LayoutInflater.from(context).inflate(R.layout.purse_listitem, null);
        return new PursesHolder(view);
    }

    @Override
    public void onBindViewHolder(final PursesHolder holder, final int position) {
        cursor.moveToPosition(position);
        //TODO create CursorUtils method CursorUtils.getDouble(cursor, key);
        final Double amount = cursor.getDouble(cursor.getColumnIndex(Purse.AMOUNT));
        holder.purseAmount.setText(String.format(Locale.US, "%.2f", amount));
        holder.purseName.setText(cursor.getString(cursor.getColumnIndex(Purse.NAME)));
        holder.purseCurrency.setText(cursor.getString(cursor.getColumnIndex(DBHelperPurse.CURRENCY_NAME)));
        holder.setPurseId(cursor.getLong(cursor.getColumnIndex(Purse.ID)));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
