package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerAutoIncrement;
import com.github.andrei1993ak.finances.util.CursorUtils;

@Table(name = "costs")
public class Cost extends TableClass implements Parcelable {

    @DBIntegerAutoIncrement
    public static final String ID = "_id";

    @DBInteger
    public static final String WALLET_ID = "walletId";

    @DBDouble
    public static final String AMOUNT = "amount";

    @DBInteger
    public static final String CATEGORY_ID = "categoryId";

    @DBInteger
    public static final String DATE = "date";

    @DBInteger
    public static final String PHOTO = "photo";

    private long _id;
    private String name;
    private long walletId;
    private double amount;
    private long categoryId;
    private long date;
    private int photo;

    public Cost() {
    }

    public Cost(final String name, final long walletId, final double amount, final long categoryId,
                final long date, final int photo) {
        this.name = name;
        this.walletId = walletId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
        this.photo = photo;
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

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(final int photo) {
        this.photo = photo;
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
        dest.writeInt(this.photo);
    }

    protected Cost(final Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.walletId = in.readLong();
        this.amount = in.readDouble();
        this.categoryId = in.readLong();
        this.date = in.readLong();
        this.photo = in.readInt();
    }

    public static final Parcelable.Creator<Cost> CREATOR = new Parcelable.Creator<Cost>() {
        @Override
        public Cost createFromParcel(final Parcel source) {
            return new Cost(source);
        }

        @Override
        public Cost[] newArray(final int size) {
            return new Cost[size];
        }
    };

    @Override
    public Cost convertFromCursor(final Cursor cursor) {
        this._id = CursorUtils.getLong(cursor, ID);
        this.name = CursorUtils.getString(cursor, NAME);
        this.amount = CursorUtils.getDouble(cursor, AMOUNT);
        this.walletId = CursorUtils.getLong(cursor, WALLET_ID);
        this.categoryId = CursorUtils.getLong(cursor, CATEGORY_ID);
        this.date = CursorUtils.getLong(cursor, DATE);
        this.photo = CursorUtils.getLong(cursor, PHOTO).intValue();
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
        contentValues.put(PHOTO, this.photo);
        return contentValues;
    }
}
