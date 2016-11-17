package com.github.andrei1993ak.finances.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;

@Table(name = "purses")
public class Purse extends TableClass implements Parcelable {

    @DBDouble
    public static String AMOUNT = "amount";

    @DBInteger
    public static String CURRENCY_ID = "currency_id";

    private long id;
    private String name;
    private long currencyId;
    private double amount;

    public Purse() {

    }

    public Purse(final String name, final long currencyId, final double amount) {
        this.name = name;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public Purse(final long id, final String name, final long currencyId, final double amount) {
        this.id = id;
        this.name = name;
        this.currencyId = currencyId;
        this.amount = amount;

    }

    public Purse(final String name, final long currencyId) {
        this.name = name;
        this.currencyId = currencyId;
        this.amount = 0.0;
    }

    protected Purse(final Parcel in) {
        id = in.readLong();
        name = in.readString();
        currencyId = in.readLong();
        amount = in.readDouble();
    }

    public static final Creator<Purse> CREATOR = new Creator<Purse>() {
        @Override
        public Purse createFromParcel(final Parcel in) {
            return new Purse(in);
        }

        @Override
        public Purse[] newArray(final int size) {
            return new Purse[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(final long currency_id) {
        this.currencyId = currency_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(currencyId);
        dest.writeDouble(amount);
    }
}

