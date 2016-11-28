package com.github.andrei1993ak.finances.model.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBString;
import com.github.andrei1993ak.finances.util.CursorUtils;

@Table(name = "currencies_from_web")
public class CurrencyOfficial extends TableClass implements Parcelable {

    @DBString
    public static final String CODE = "code";

    private long id;
    private String code;
    private String name;

    public CurrencyOfficial() {
    }

    public CurrencyOfficial(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    protected CurrencyOfficial(final Parcel in) {
        id = in.readLong();
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<CurrencyOfficial> CREATOR = new Creator<CurrencyOfficial>() {
        @Override
        public CurrencyOfficial createFromParcel(final Parcel in) {
            return new CurrencyOfficial(in);
        }

        @Override
        public CurrencyOfficial[] newArray(final int size) {
            return new CurrencyOfficial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        dest.writeString(code);
        dest.writeString(name);
    }

    @Override
    public CurrencyOfficial convertFromCursor(Cursor cursor) {
        this.id = CursorUtils.getLong(cursor, ID);
        this.name = CursorUtils.getString(cursor, NAME);
        this.code = CursorUtils.getString(cursor, CODE);
        return this;
    }

    @Override
    public ContentValues convertToContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, this.name);
        contentValues.put(CODE, this.code);
        return contentValues;
    }
}
