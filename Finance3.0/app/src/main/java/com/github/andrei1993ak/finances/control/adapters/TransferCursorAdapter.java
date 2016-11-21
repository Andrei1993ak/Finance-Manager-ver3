package com.github.andrei1993ak.finances.control.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.github.andrei1993ak.finances.app.customViews.TransferView;

public class TransferCursorAdapter extends CursorAdapter {


    public TransferCursorAdapter(final Context context, final Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        return new TransferView(context);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TransferView transferView = (TransferView) view;
        transferView.setFields(cursor);
    }
}
