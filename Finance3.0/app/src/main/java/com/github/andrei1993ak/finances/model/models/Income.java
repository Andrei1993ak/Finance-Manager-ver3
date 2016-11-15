package com.github.andrei1993ak.finances.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;

@Table(name = "incomes")
public class Income extends TableClass implements Parcelable {

    @DBInteger
    public static final String PURSE_ID = "purse_id";

    @DBDouble
    public static final String AMOUNT = "amount";

    @DBInteger
    public static final String CATEGORY_ID = "category_id";

    @DBInteger
    public static final String DATE = "date";

    private long _id;
    private String name;
    private long purse_id;
    private double amount;
    private long category_id;
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

    public long getpurseId() {
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
    }

    public Income() {
    }

    protected Income(final Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.purse_id = in.readLong();
        this.amount = in.readDouble();
        this.category_id = in.readLong();
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
}
