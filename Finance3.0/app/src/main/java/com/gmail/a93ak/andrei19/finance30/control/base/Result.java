package com.gmail.a93ak.andrei19.finance30.control.base;

public class Result<T> {

    private int id;
    private final T object;

    public Result(final int id, final T object) {
        this.id = id;
        this.object = object;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

}
