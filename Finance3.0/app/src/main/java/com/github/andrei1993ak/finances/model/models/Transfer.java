package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.util.CursorUtils;

@Table(name = "transfers")
public final class Transfer extends TableClass implements Parcelable {

    @DBInteger
    public static String DATE = "date";

    @DBInteger
    public static String FROM_WALLET_ID = "fromWalletId";

    @DBInteger
    public static String TO_WALLET_ID = "toWalletId";

    @DBDouble
    public static String FROM_AMOUNT = "fromAmount";

    @DBDouble
    public static String TO_AMOUNT = "toAmount";

    private long _id;
    private String name;
    private long date;
    private long fromWalletId;
    private long toWalletId;
    private double fromAmount;
    private double toAmount;

    public Transfer() {
    }

    public Transfer(final String name, final long date, final long fromWalletId, final long toWalletId,
                    final double fromAmount, final double toAmount) {
        this.name = name;
        this.date = date;
        this.fromWalletId = fromWalletId;
        this.toWalletId = toWalletId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public long getId() {
        return _id;
    }

    public void setId(final long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(final long date) {
        this.date = date;
    }

    public long getFromWalletId() {
        return fromWalletId;
    }

    public void setFromWalletId(final long fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public long getToWalletId() {
        return toWalletId;
    }

    public void setToWalletId(final long toWalletId) {
        this.toWalletId = toWalletId;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(final double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(final double toAmount) {
        this.toAmount = toAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.name);
        dest.writeLong(this.date);
        dest.writeLong(this.fromWalletId);
        dest.writeLong(this.toWalletId);
        dest.writeDouble(this.fromAmount);
        dest.writeDouble(this.toAmount);
    }

    protected Transfer(final Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.date = in.readLong();
        this.fromWalletId = in.readLong();
        this.toWalletId = in.readLong();
        this.fromAmount = in.readDouble();
        this.toAmount = in.readDouble();
    }

    public static final Parcelable.Creator<Transfer> CREATOR = new Parcelable.Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(final Parcel source) {
            return new Transfer(source);
        }

        @Override
        public Transfer[] newArray(final int size) {
            return new Transfer[size];
        }
    };

    @Override
    public Transfer convertFromCursor(final Cursor cursor) {
        final CursorUtils cursorUtils = new CursorUtils();
        this._id = cursorUtils.getLong(cursor, ID);
        this.name = cursorUtils.getString(cursor, NAME);
        this.fromWalletId = cursorUtils.getLong(cursor, FROM_WALLET_ID);
        this.toWalletId = cursorUtils.getLong(cursor, TO_WALLET_ID);
        this.fromAmount = cursorUtils.getDouble(cursor, FROM_AMOUNT);
        this.toAmount = cursorUtils.getDouble(cursor, TO_AMOUNT);
        this.date = cursorUtils.getLong(cursor, DATE);
        return this;
    }

    @Override
    public ContentValues convertToContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, this.name);
        contentValues.put(DATE, this.date);
        contentValues.put(FROM_WALLET_ID, this.fromWalletId);
        contentValues.put(TO_WALLET_ID, this.toWalletId);
        contentValues.put(FROM_AMOUNT, this.fromAmount);
        contentValues.put(TO_AMOUNT, this.toAmount);
        return contentValues;
    }
}
