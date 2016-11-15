package com.github.andrei1993ak.finances.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;

@Table(name = "incomeCategories")
public class IncomeCategory extends TableClass implements Parcelable {

    @DBInteger
    public static final String PARENT_ID = "parent_id";

    private long id;
    private String name;
    private long parent_id;

    public IncomeCategory() {
    }

    public IncomeCategory(final String name, final long parent_id) {
        this.name = name;
        this.parent_id = parent_id;
    }

    public IncomeCategory(final long id, final String name, final long parent_id) {
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

    protected IncomeCategory(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.parent_id = in.readLong();
    }

    public static final Parcelable.Creator<IncomeCategory> CREATOR = new Parcelable.Creator<IncomeCategory>() {
        @Override
        public IncomeCategory createFromParcel(final Parcel source) {
            return new IncomeCategory(source);
        }

        @Override
        public IncomeCategory[] newArray(final int size) {
            return new IncomeCategory[size];
        }
    };
}
