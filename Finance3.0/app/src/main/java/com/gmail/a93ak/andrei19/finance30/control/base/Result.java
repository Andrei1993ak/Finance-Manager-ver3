package com.gmail.a93ak.andrei19.finance30.control.base;

public class Result<T> {

    private int id;
    private T t;

    public Result(int id, T t) {
        this.id = id;
        this.t = t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
