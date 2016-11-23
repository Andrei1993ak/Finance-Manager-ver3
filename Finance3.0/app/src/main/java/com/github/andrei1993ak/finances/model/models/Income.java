package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.util.CursorUtils;

@Table(name = "incomes")
public class Income extends TableClass implements Parcelable {

    @DBInteger
    public static final String WALLET_ID = "walletId";

    @DBDouble
    public static final String AMOUNT = "amount";

    @DBInteger
    public static final String CATEGORY_ID = "categoryId";

    @DBInteger
    public static final String DATE = "date";

    private long _id;
    private String name;
    private long walletId;
    private double amount;
    private long categoryId;
    private long date;

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

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(final long walletId) {
        this.walletId = walletId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final long categoryId) {
        this.categoryId = categoryId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(final long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.name);
        dest.writeLong(this.walletId);
        dest.writeDouble(this.amount);
        dest.writeLong(this.categoryId);
        dest.writeLong(this.date);
    }

    public Income() {
    }

    protected Income(final Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.walletId = in.readLong();
        this.amount = in.readDouble();
        this.categoryId = in.readLong();
        this.date = in.readLong();
    }

    public static final Parcelable.Creator<Income> CREATOR = new Parcelable.Creator<Income>() {
        @Override
        public Income createFromParcel(final Parcel source) {
            return new Income(source);
        }

        @Override
        public Income[] newArray(final int size) {
            return new Income[size];
        }
    };

    @Override
    public Income convertFromCursor(final Cursor cursor) {
        final CursorUtils cursorUtils = new CursorUtils();
        this._id = cursorUtils.getLong(cursor, ID);
        this.name = cursorUtils.getString(cursor, NAME);
        this.amount = cursorUtils.getDouble(cursor, AMOUNT);
        this.walletId = cursorUtils.getLong(cursor,WALLET_ID);
        this.categoryId = cursorUtils.getLong(cursor, CATEGORY_ID);
        this.date = cursorUtils.getLong(cursor, DATE);
        return this;
    }

    @Override
    public ContentValues convertToContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, this.name);
        contentValues.put(WALLET_ID, this.walletId);
        contentValues.put(AMOUNT, this.amount);
        contentValues.put(CATEGORY_ID, this.categoryId);
        contentValues.put(DATE, this.date);
        return contentValues;
    }
}
