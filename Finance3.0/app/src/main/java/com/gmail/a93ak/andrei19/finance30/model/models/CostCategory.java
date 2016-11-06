package com.gmail.a93ak.andrei19.finance30.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.a93ak.andrei19.finance30.model.annotations.Table;
import com.gmail.a93ak.andrei19.finance30.model.annotations.types.DBInteger;

@Table(name = "costCategories")
public class CostCategory extends TableClass implements Parcelable {

    @DBInteger
    public static final String PARENT_ID = "parent_id";

    private long id;
    private String name;
    private long parent_id;

    public CostCategory() {
    }

    public CostCategory(final String name, final long parent_id) {
        this.name = name;
        this.parent_id = parent_id;
    }

    public CostCategory(final long id, final String name, final long parent_id) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
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

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(final long parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.parent_id);
    }

    protected CostCategory(final Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.parent_id = in.readLong();
    }

    public static final Parcelable.Creator<CostCategory> CREATOR = new Parcelable.Creator<CostCategory>() {
        @Override
        public CostCategory createFromParcel(final Parcel source) {
            return new CostCategory(source);
        }

        @Override
        public CostCategory[] newArray(final int size) {
            return new CostCategory[size];
        }
    };
}
