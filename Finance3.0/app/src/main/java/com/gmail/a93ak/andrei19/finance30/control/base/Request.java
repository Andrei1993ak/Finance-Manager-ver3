package com.gmail.a93ak.andrei19.finance30.control.base;

public class Request<T> {
//
    private int id;
    private T o;

    public Request(int id, T o) {
        this.id = id;
        this.o = o;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getO() {
        return o;
    }

    public void setO(T o) {
        this.o = o;
    }
}
