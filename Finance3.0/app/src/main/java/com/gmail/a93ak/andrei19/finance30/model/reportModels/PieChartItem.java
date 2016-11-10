package com.gmail.a93ak.andrei19.finance30.model.reportModels;


public class PieChartItem {
    private long categoryId;
    private String categoryName;
    private double amount;

    public static final String TYPE = "type";

    public PieChartItem(final long categoryId, final String categoryName, final double amount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }
}
