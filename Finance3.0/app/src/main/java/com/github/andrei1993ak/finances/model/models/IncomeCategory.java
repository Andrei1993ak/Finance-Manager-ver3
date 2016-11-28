package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.util.CursorUtils;

@Table(name = "incomeCategories")
public class IncomeCategory extends TableClass implements Parcelable {

    @DBInteger
    public static final String PARENT_ID = "parentId";

    private long id;
    private String name;
    private long parentId;

    public IncomeCategory() {
    }

    public IncomeCategory(final String name, final long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public IncomeCategory(final long id, final String name, final long parentId) {
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

    protected IncomeCategory(final Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.parentId = in.readLong();
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

    @Override
    public IncomeCategory convertFromCursor(final Cursor cursor) {
        this.id = CursorUtils.getLong(cursor, ID);
        this.name = CursorUtils.getString(cursor, NAME);
        this.parentId = CursorUtils.getLong(cursor, PARENT_ID);
        return this;
    }

    @Override
    public ContentValues convertToContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, this.name);
        contentValues.put(PARENT_ID, this.parentId);
        return contentValues;
    }
}
