package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.ViewHolders.PursesHolder;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperPurse;

public class PursesRecycleViewAdapter extends RecyclerView.Adapter<PursesHolder>{
    private Cursor cursor;
    private Context context;

    public PursesRecycleViewAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public PursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.purse_listitem,null);
        return new PursesHolder(view);
    }

    @Override
    public void onBindViewHolder(PursesHolder holder, int position) {
        cursor.moveToPosition(position);
        Double amount = cursor.getDouble(cursor.getColumnIndex(DBHelper.PURSES_KEY_AMOUNT));
        holder.purseAmount.setText(String.format("%.2f",amount));
        holder.purseName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.PURSES_KEY_NAME)));
        holder.purseCurrency.setText(cursor.getString(cursor.getColumnIndex(DBHelperPurse.CURRENCY_NAME)));
        holder.setPurseId(cursor.getLong(cursor.getColumnIndex(DBHelper.PURSES_KEY_ID)));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
