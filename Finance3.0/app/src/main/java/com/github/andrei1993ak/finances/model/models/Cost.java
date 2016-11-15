package com.github.andrei1993ak.finances.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;

@Table(name = "costs")
public class Cost extends TableClass implements Parcelable {

    @DBInteger
    public static final String PURSE_ID = "purse_id";

    @DBDouble
    public static final String AMOUNT = "amount";

    @DBInteger
    public static final String CATEGORY_ID = "category_id";

    @DBInteger
    public static final String DATE = "date";

    @DBInteger
    public static final String PHOTO = "photo";

    private long _id;
    private String name;
    private long purse_id;
    private double amount;
    private long category_id;
    private long date;
    private int photo;

    public Cost() {
    }

    public Cost(final String name, final long purse_id, final double amount, final long category_id,
                final long date, final int photo) {
        this.name = name;
        this.purse_id = purse_id;
        this.amount = amount;
        this.category_id = category_id;
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

    public long getPurseId() {
        return purse_id;
    }

    public void setPurseId(final long purse_id) {
        this.purse_id = purse_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public long getCategoryId() {
        return category_id;
    }

    public void setCategoryId(final long category_id) {
        this.category_id = category_id;
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
        dest.writeLong(this.purse_id);
        dest.writeDouble(this.amount);
        dest.writeLong(this.category_id);
        dest.writeLong(this.date);
        dest.writeInt(this.photo);
    }

    protected Cost(final Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.purse_id = in.readLong();
        this.amount = in.readDouble();
        this.category_id = in.readLong();
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
}
