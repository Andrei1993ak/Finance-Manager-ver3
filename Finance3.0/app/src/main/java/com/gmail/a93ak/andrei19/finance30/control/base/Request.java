package com.gmail.a93ak.andrei19.finance30.control.base;

public class Request {
//
    private int id;
    private Object o;

    public Request(int id, Object o) {
        this.id = id;
        this.o = o;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
