package com.gmail.a93ak.andrei19.finance30.model.pojos;


public class Purse {
    private long id;
    private String name;
    private long currency_id;
    private double amount;

    public Purse() {

    }

    public Purse(String name, long currency_id, double amount) {
        this.name = name;
        this.currency_id = currency_id;
        this.amount = amount;
    }

    public Purse(long id, String name, long currency_id, double amount) {
        this.id = id;
        this.name = name;
        this.currency_id = currency_id;
        this.amount = amount;

    }

    public Purse(String name, long currency_id) {
        this.name = name;
        this.currency_id = currency_id;
        this.amount = 0.0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(long currency_id) {
        this.currency_id = currency_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}

