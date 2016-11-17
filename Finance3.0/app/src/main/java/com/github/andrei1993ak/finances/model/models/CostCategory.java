package com.github.andrei1993ak.finances.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;

@Table(name = "costCategories")
public class CostCategory extends TableClass implements Parcelable {

    @DBInteger
    public static final String PARENT_ID = "parent_id";

    private long id;
    private String name;
    private long parentId;

    public CostCategory() {
    }

    public CostCategory(final String name, final long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public CostCategory(final long id, final String name, final long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(final long parentId) {
        this.parentId = parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.parentId);
    }

    protected CostCategory(final Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.parentId = in.readLong();
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
