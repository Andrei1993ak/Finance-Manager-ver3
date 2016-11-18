package com.github.andrei1993ak.finances.control.base;

public class Request<T> {

    private Operations id;
    private final T object;

    public Request(final Operations id, final T object) {
        this.id = id;
        this.object = object;
    }

    public Operations getId() {
        return id;
    }

    public void setId(final Operations id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

}
