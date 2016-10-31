package com.gmail.a93ak.andrei19.finance30.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCategoryCost;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Currency;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CostCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private DBHelperPurse helperPurse;
    private DBHelperCurrency helperCurrency;
    private DBHelperCategoryCost dbHelperCategoryCost;

    public CostCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBHelper dbHelper = DBHelper.getInstance(context);
        helperPurse = DBHelperPurse.getInstance(dbHelper);
        helperCurrency = DBHelperCurrency.getInstance(dbHelper);
        dbHelperCategoryCost = DBHelperCategoryCost.getInstance(dbHelper);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.cost_listitem,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView)view.findViewById(R.id.LICostName);
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.COST_KEY_NAME));
        textViewName.setText(name);

        TextView textViewDate = (TextView)view.findViewById(R.id.LICostDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Long time = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBHelper.COST_KEY_DATE)));
        String date = dateFormatter.format(time);
        textViewDate.setText(date);

        TextView tvAmount = (TextView)view.findViewById(R.id.LICostPurseAmount);
        Purse purse = helperPurse.get(cursor.getLong(cursor.getColumnIndex(DBHelper.COST_KEY_PURSE_ID)));
        Currency currency = helperCurrency.get(purse.getCurrency_id());
        Double amount = cursor.getDouble(cursor.getColumnIndex(DBHelper.COST_KEY_AMOUNT));
        String amountString = String.format("%.2f",amount);
        amountString+=" ";
        amountString+=currency.getCode();
        tvAmount.setText(amountString);

        TextView category = (TextView)view.findViewById(R.id.LiCostCategory);
        long CategoryId = cursor.getLong(cursor.getColumnIndex(DBHelper.COST_KEY_CATEGORY_ID));
        category.setText(dbHelperCategoryCost.get(CategoryId).getName());

    }
}
