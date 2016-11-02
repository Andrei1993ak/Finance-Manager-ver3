package com.gmail.a93ak.andrei19.finance30.model.pojos;

public class Cost {

    public static final int PHOTO_IN_STORAGE = -1;
    public static final int PHOTO_IN_FTP = 1;

    private long _id;
    private String name;
    private long purse_id;
    private double amount;
    private long category_id;
    private long date;
    private int photo;

    public Cost() {
    }

    public Cost(String name, long purse_id, double amount, long category_id, long date, int photo) {
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

    public void setId(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPurse_id() {
        return purse_id;
    }

    public void setPurse_id(long purse_id) {
        this.purse_id = purse_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
