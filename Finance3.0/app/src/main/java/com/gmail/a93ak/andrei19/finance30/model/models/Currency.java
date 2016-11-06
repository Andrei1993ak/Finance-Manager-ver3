package com.gmail.a93ak.andrei19.finance30.model.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.a93ak.andrei19.finance30.model.annotations.Table;
import com.gmail.a93ak.andrei19.finance30.model.annotations.types.DBString;

@Table(name = "currencies")
public class Currency extends TableClass implements Parcelable {

    @DBString
    public static final String CODE = "code";

    private long id;
    private String code;
    private String name;

    public Currency() {
    }

    public Currency(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.code);
        dest.writeString(this.name);
    }

    protected Currency(final Parcel in) {
        this.id = in.readLong();
        this.code = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        @Override
        public Currency createFromParcel(final Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(final int size) {
            return new Currency[size];
        }
    };
}
