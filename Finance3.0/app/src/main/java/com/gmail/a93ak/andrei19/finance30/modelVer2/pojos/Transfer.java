package com.gmail.a93ak.andrei19.finance30.modelVer2.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.a93ak.andrei19.finance30.modelVer2.annotations.Table;
import com.gmail.a93ak.andrei19.finance30.modelVer2.annotations.types.DBDouble;
import com.gmail.a93ak.andrei19.finance30.modelVer2.annotations.types.DBInteger;
import com.gmail.a93ak.andrei19.finance30.modelVer2.annotations.types.DBIntegerPrimaryKey;
import com.gmail.a93ak.andrei19.finance30.modelVer2.annotations.types.DBString;

@Table(name = "transfers")
public final class Transfer implements TableClass,Parcelable {

    @DBIntegerPrimaryKey
    public static String ID = "_id";

    @DBString
    public static String NAME = "name";

    @DBInteger
    public static String DATE = "date";

    @DBInteger
    public static String FROM_PURSE_ID = "fromPurseId";

    @DBInteger
    public static String TO_PURSE_ID = "toPurseId";

    @DBDouble
    public static String FROM_AMOUNT = "fromAmount";

    @DBDouble
    public static String TO_AMOUNT = "toAmount";

    private long _id;
    private String name;
    private long date;
    private long fromPurseId;
    private long toPurseId;
    private double fromAmount;
    private double toAmount;

    public Transfer() {
    }

    public Transfer(String name, long date, long fromPurseId, long toPurseId, double fromAmount, double toAmount) {
        this.name = name;
        this.date = date;
        this.fromPurseId = fromPurseId;
        this.toPurseId = toPurseId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFromPurseId() {
        return fromPurseId;
    }

    public void setFromPurseId(long fromPurseId) {
        this.fromPurseId = fromPurseId;
    }

    public long getToPurseId() {
        return toPurseId;
    }

    public void setToPurseId(long toPurseId) {
        this.toPurseId = toPurseId;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.name);
        dest.writeLong(this.date);
        dest.writeLong(this.fromPurseId);
        dest.writeLong(this.toPurseId);
        dest.writeDouble(this.fromAmount);
        dest.writeDouble(this.toAmount);
    }

    protected Transfer(Parcel in) {
        this._id = in.readLong();
        this.name = in.readString();
        this.date = in.readLong();
        this.fromPurseId = in.readLong();
        this.toPurseId = in.readLong();
        this.fromAmount = in.readDouble();
        this.toAmount = in.readDouble();
    }

    public static final Parcelable.Creator<Transfer> CREATOR = new Parcelable.Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel source) {
            return new Transfer(source);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };
}
